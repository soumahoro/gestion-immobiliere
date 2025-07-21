import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Role } from 'app/entities/enumerations/role.model';
import { IUtilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';
import { UtilisateurFormGroup, UtilisateurFormService } from './utilisateur-form.service';

@Component({
  selector: 'jhi-utilisateur-update',
  templateUrl: './utilisateur-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UtilisateurUpdateComponent implements OnInit {
  isSaving = false;
  utilisateur: IUtilisateur | null = null;
  roleValues = Object.keys(Role);

  protected utilisateurService = inject(UtilisateurService);
  protected utilisateurFormService = inject(UtilisateurFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UtilisateurFormGroup = this.utilisateurFormService.createUtilisateurFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateur }) => {
      this.utilisateur = utilisateur;
      if (utilisateur) {
        this.updateForm(utilisateur);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilisateur = this.utilisateurFormService.getUtilisateur(this.editForm);
    if (utilisateur.id !== null) {
      this.subscribeToSaveResponse(this.utilisateurService.update(utilisateur));
    } else {
      this.subscribeToSaveResponse(this.utilisateurService.create(utilisateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisateur>>): void {
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

  protected updateForm(utilisateur: IUtilisateur): void {
    this.utilisateur = utilisateur;
    this.utilisateurFormService.resetForm(this.editForm, utilisateur);
  }
}
