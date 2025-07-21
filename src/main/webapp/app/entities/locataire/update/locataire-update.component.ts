import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppartement } from 'app/entities/appartement/appartement.model';
import { AppartementService } from 'app/entities/appartement/service/appartement.service';
import { ILocataire } from '../locataire.model';
import { LocataireService } from '../service/locataire.service';
import { LocataireFormGroup, LocataireFormService } from './locataire-form.service';

@Component({
  selector: 'jhi-locataire-update',
  templateUrl: './locataire-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LocataireUpdateComponent implements OnInit {
  isSaving = false;
  locataire: ILocataire | null = null;

  appartementsSharedCollection: IAppartement[] = [];

  protected locataireService = inject(LocataireService);
  protected locataireFormService = inject(LocataireFormService);
  protected appartementService = inject(AppartementService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LocataireFormGroup = this.locataireFormService.createLocataireFormGroup();

  compareAppartement = (o1: IAppartement | null, o2: IAppartement | null): boolean => this.appartementService.compareAppartement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locataire }) => {
      this.locataire = locataire;
      if (locataire) {
        this.updateForm(locataire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const locataire = this.locataireFormService.getLocataire(this.editForm);
    if (locataire.id !== null) {
      this.subscribeToSaveResponse(this.locataireService.update(locataire));
    } else {
      this.subscribeToSaveResponse(this.locataireService.create(locataire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocataire>>): void {
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

  protected updateForm(locataire: ILocataire): void {
    this.locataire = locataire;
    this.locataireFormService.resetForm(this.editForm, locataire);

    this.appartementsSharedCollection = this.appartementService.addAppartementToCollectionIfMissing<IAppartement>(
      this.appartementsSharedCollection,
      locataire.appartement,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appartementService
      .query()
      .pipe(map((res: HttpResponse<IAppartement[]>) => res.body ?? []))
      .pipe(
        map((appartements: IAppartement[]) =>
          this.appartementService.addAppartementToCollectionIfMissing<IAppartement>(appartements, this.locataire?.appartement),
        ),
      )
      .subscribe((appartements: IAppartement[]) => (this.appartementsSharedCollection = appartements));
  }
}
