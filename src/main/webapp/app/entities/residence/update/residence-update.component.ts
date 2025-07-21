import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProprietaire } from 'app/entities/proprietaire/proprietaire.model';
import { ProprietaireService } from 'app/entities/proprietaire/service/proprietaire.service';
import { IResidence } from '../residence.model';
import { ResidenceService } from '../service/residence.service';
import { ResidenceFormGroup, ResidenceFormService } from './residence-form.service';

@Component({
  selector: 'jhi-residence-update',
  templateUrl: './residence-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResidenceUpdateComponent implements OnInit {
  isSaving = false;
  residence: IResidence | null = null;

  proprietairesSharedCollection: IProprietaire[] = [];

  protected residenceService = inject(ResidenceService);
  protected residenceFormService = inject(ResidenceFormService);
  protected proprietaireService = inject(ProprietaireService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResidenceFormGroup = this.residenceFormService.createResidenceFormGroup();

  compareProprietaire = (o1: IProprietaire | null, o2: IProprietaire | null): boolean =>
    this.proprietaireService.compareProprietaire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ residence }) => {
      this.residence = residence;
      if (residence) {
        this.updateForm(residence);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const residence = this.residenceFormService.getResidence(this.editForm);
    if (residence.id !== null) {
      this.subscribeToSaveResponse(this.residenceService.update(residence));
    } else {
      this.subscribeToSaveResponse(this.residenceService.create(residence));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResidence>>): void {
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

  protected updateForm(residence: IResidence): void {
    this.residence = residence;
    this.residenceFormService.resetForm(this.editForm, residence);

    this.proprietairesSharedCollection = this.proprietaireService.addProprietaireToCollectionIfMissing<IProprietaire>(
      this.proprietairesSharedCollection,
      residence.proprietaire,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.proprietaireService
      .query()
      .pipe(map((res: HttpResponse<IProprietaire[]>) => res.body ?? []))
      .pipe(
        map((proprietaires: IProprietaire[]) =>
          this.proprietaireService.addProprietaireToCollectionIfMissing<IProprietaire>(proprietaires, this.residence?.proprietaire),
        ),
      )
      .subscribe((proprietaires: IProprietaire[]) => (this.proprietairesSharedCollection = proprietaires));
  }
}
