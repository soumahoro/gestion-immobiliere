import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ILocataire } from 'app/entities/locataire/locataire.model';
import { LocataireService } from 'app/entities/locataire/service/locataire.service';
import { IMoi } from 'app/entities/moi/moi.model';
import { MoiService } from 'app/entities/moi/service/moi.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ReglementService } from '../service/reglement.service';
import { IReglement } from '../reglement.model';
import { ReglementFormGroup, ReglementFormService } from './reglement-form.service';

@Component({
  selector: 'jhi-reglement-update',
  templateUrl: './reglement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReglementUpdateComponent implements OnInit {
  isSaving = false;
  reglement: IReglement | null = null;

  locatairesSharedCollection: ILocataire[] = [];
  moisSharedCollection: IMoi[] = [];
  utilisateursSharedCollection: IUtilisateur[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reglementService = inject(ReglementService);
  protected reglementFormService = inject(ReglementFormService);
  protected locataireService = inject(LocataireService);
  protected moiService = inject(MoiService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReglementFormGroup = this.reglementFormService.createReglementFormGroup();

  compareLocataire = (o1: ILocataire | null, o2: ILocataire | null): boolean => this.locataireService.compareLocataire(o1, o2);

  compareMoi = (o1: IMoi | null, o2: IMoi | null): boolean => this.moiService.compareMoi(o1, o2);

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reglement }) => {
      this.reglement = reglement;
      if (reglement) {
        this.updateForm(reglement);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('gestionimmobiliereApp.error', { ...err, key: `error.file.${err.key}` }),
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reglement = this.reglementFormService.getReglement(this.editForm);
    if (reglement.id !== null) {
      this.subscribeToSaveResponse(this.reglementService.update(reglement));
    } else {
      this.subscribeToSaveResponse(this.reglementService.create(reglement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReglement>>): void {
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

  protected updateForm(reglement: IReglement): void {
    this.reglement = reglement;
    this.reglementFormService.resetForm(this.editForm, reglement);

    this.locatairesSharedCollection = this.locataireService.addLocataireToCollectionIfMissing<ILocataire>(
      this.locatairesSharedCollection,
      reglement.locataire,
    );
    this.moisSharedCollection = this.moiService.addMoiToCollectionIfMissing<IMoi>(this.moisSharedCollection, reglement.moi);
    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      reglement.utilisateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locataireService
      .query()
      .pipe(map((res: HttpResponse<ILocataire[]>) => res.body ?? []))
      .pipe(
        map((locataires: ILocataire[]) =>
          this.locataireService.addLocataireToCollectionIfMissing<ILocataire>(locataires, this.reglement?.locataire),
        ),
      )
      .subscribe((locataires: ILocataire[]) => (this.locatairesSharedCollection = locataires));

    this.moiService
      .query()
      .pipe(map((res: HttpResponse<IMoi[]>) => res.body ?? []))
      .pipe(map((mois: IMoi[]) => this.moiService.addMoiToCollectionIfMissing<IMoi>(mois, this.reglement?.moi)))
      .subscribe((mois: IMoi[]) => (this.moisSharedCollection = mois));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.reglement?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
