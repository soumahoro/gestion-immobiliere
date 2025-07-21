import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAnnee } from '../annee.model';
import { AnneeService } from '../service/annee.service';
import { AnneeFormGroup, AnneeFormService } from './annee-form.service';

@Component({
  selector: 'jhi-annee-update',
  templateUrl: './annee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AnneeUpdateComponent implements OnInit {
  isSaving = false;
  annee: IAnnee | null = null;

  protected anneeService = inject(AnneeService);
  protected anneeFormService = inject(AnneeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AnneeFormGroup = this.anneeFormService.createAnneeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annee }) => {
      this.annee = annee;
      if (annee) {
        this.updateForm(annee);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const annee = this.anneeFormService.getAnnee(this.editForm);
    if (annee.id !== null) {
      this.subscribeToSaveResponse(this.anneeService.update(annee));
    } else {
      this.subscribeToSaveResponse(this.anneeService.create(annee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnee>>): void {
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

  protected updateForm(annee: IAnnee): void {
    this.annee = annee;
    this.anneeFormService.resetForm(this.editForm, annee);
  }
}
