import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAdministrateur } from '../administrateur.model';
import { AdministrateurService } from '../service/administrateur.service';
import { AdministrateurFormGroup, AdministrateurFormService } from './administrateur-form.service';

@Component({
  selector: 'jhi-administrateur-update',
  templateUrl: './administrateur-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AdministrateurUpdateComponent implements OnInit {
  isSaving = false;
  administrateur: IAdministrateur | null = null;

  protected administrateurService = inject(AdministrateurService);
  protected administrateurFormService = inject(AdministrateurFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AdministrateurFormGroup = this.administrateurFormService.createAdministrateurFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ administrateur }) => {
      this.administrateur = administrateur;
      if (administrateur) {
        this.updateForm(administrateur);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const administrateur = this.administrateurFormService.getAdministrateur(this.editForm);
    if (administrateur.id !== null) {
      this.subscribeToSaveResponse(this.administrateurService.update(administrateur));
    } else {
      this.subscribeToSaveResponse(this.administrateurService.create(administrateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdministrateur>>): void {
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

  protected updateForm(administrateur: IAdministrateur): void {
    this.administrateur = administrateur;
    this.administrateurFormService.resetForm(this.editForm, administrateur);
  }
}
