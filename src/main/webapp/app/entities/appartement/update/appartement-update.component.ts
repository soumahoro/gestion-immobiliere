import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IResidence } from 'app/entities/residence/residence.model';
import { ResidenceService } from 'app/entities/residence/service/residence.service';
import { IAppartement } from '../appartement.model';
import { AppartementService } from '../service/appartement.service';
import { AppartementFormGroup, AppartementFormService } from './appartement-form.service';

@Component({
  selector: 'jhi-appartement-update',
  templateUrl: './appartement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppartementUpdateComponent implements OnInit {
  isSaving = false;
  appartement: IAppartement | null = null;

  residencesSharedCollection: IResidence[] = [];

  protected appartementService = inject(AppartementService);
  protected appartementFormService = inject(AppartementFormService);
  protected residenceService = inject(ResidenceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppartementFormGroup = this.appartementFormService.createAppartementFormGroup();

  compareResidence = (o1: IResidence | null, o2: IResidence | null): boolean => this.residenceService.compareResidence(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appartement }) => {
      this.appartement = appartement;
      if (appartement) {
        this.updateForm(appartement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appartement = this.appartementFormService.getAppartement(this.editForm);
    if (appartement.id !== null) {
      this.subscribeToSaveResponse(this.appartementService.update(appartement));
    } else {
      this.subscribeToSaveResponse(this.appartementService.create(appartement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppartement>>): void {
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

  protected updateForm(appartement: IAppartement): void {
    this.appartement = appartement;
    this.appartementFormService.resetForm(this.editForm, appartement);

    this.residencesSharedCollection = this.residenceService.addResidenceToCollectionIfMissing<IResidence>(
      this.residencesSharedCollection,
      appartement.residence,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.residenceService
      .query()
      .pipe(map((res: HttpResponse<IResidence[]>) => res.body ?? []))
      .pipe(
        map((residences: IResidence[]) =>
          this.residenceService.addResidenceToCollectionIfMissing<IResidence>(residences, this.appartement?.residence),
        ),
      )
      .subscribe((residences: IResidence[]) => (this.residencesSharedCollection = residences));
  }
}
