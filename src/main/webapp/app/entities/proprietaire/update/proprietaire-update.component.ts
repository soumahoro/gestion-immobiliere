import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProprietaire } from '../proprietaire.model';
import { ProprietaireService } from '../service/proprietaire.service';
import { ProprietaireFormGroup, ProprietaireFormService } from './proprietaire-form.service';

@Component({
  selector: 'jhi-proprietaire-update',
  templateUrl: './proprietaire-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProprietaireUpdateComponent implements OnInit {
  isSaving = false;
  proprietaire: IProprietaire | null = null;

  protected proprietaireService = inject(ProprietaireService);
  protected proprietaireFormService = inject(ProprietaireFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProprietaireFormGroup = this.proprietaireFormService.createProprietaireFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proprietaire }) => {
      this.proprietaire = proprietaire;
      if (proprietaire) {
        this.updateForm(proprietaire);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proprietaire = this.proprietaireFormService.getProprietaire(this.editForm);
    if (proprietaire.id !== null) {
      this.subscribeToSaveResponse(this.proprietaireService.update(proprietaire));
    } else {
      this.subscribeToSaveResponse(this.proprietaireService.create(proprietaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProprietaire>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(proprietaire: IProprietaire): void {
    this.proprietaire = proprietaire;
    this.proprietaireFormService.resetForm(this.editForm, proprietaire);
  }
}
