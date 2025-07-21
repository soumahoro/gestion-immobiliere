import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisateur for edit and NewUtilisateurFormGroupInput for create.
 */
type UtilisateurFormGroupInput = IUtilisateur | PartialWithRequiredKeyOf<NewUtilisateur>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUtilisateur | NewUtilisateur> = Omit<T, 'dateDeNaissance'> & {
  dateDeNaissance?: string | null;
};

type UtilisateurFormRawValue = FormValueOf<IUtilisateur>;

type NewUtilisateurFormRawValue = FormValueOf<NewUtilisateur>;

type UtilisateurFormDefaults = Pick<NewUtilisateur, 'id' | 'dateDeNaissance'>;

type UtilisateurFormGroupContent = {
  id: FormControl<UtilisateurFormRawValue['id'] | NewUtilisateur['id']>;
  iduser: FormControl<UtilisateurFormRawValue['iduser']>;
  login: FormControl<UtilisateurFormRawValue['login']>;
  nom: FormControl<UtilisateurFormRawValue['nom']>;
  prenom: FormControl<UtilisateurFormRawValue['prenom']>;
  dateDeNaissance: FormControl<UtilisateurFormRawValue['dateDeNaissance']>;
  motdepasse: FormControl<UtilisateurFormRawValue['motdepasse']>;
  email: FormControl<UtilisateurFormRawValue['email']>;
  photo: FormControl<UtilisateurFormRawValue['photo']>;
  pwd: FormControl<UtilisateurFormRawValue['pwd']>;
  role: FormControl<UtilisateurFormRawValue['role']>;
};

export type UtilisateurFormGroup = FormGroup<UtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFormService {
  createUtilisateurFormGroup(utilisateur: UtilisateurFormGroupInput = { id: null }): UtilisateurFormGroup {
    const utilisateurRawValue = this.convertUtilisateurToUtilisateurRawValue({
      ...this.getFormDefaults(),
      ...utilisateur,
    });
    return new FormGroup<UtilisateurFormGroupContent>({
      id: new FormControl(
        { value: utilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      iduser: new FormControl(utilisateurRawValue.iduser, {
        validators: [Validators.required],
      }),
      login: new FormControl(utilisateurRawValue.login, {
        validators: [Validators.required],
      }),
      nom: new FormControl(utilisateurRawValue.nom),
      prenom: new FormControl(utilisateurRawValue.prenom),
      dateDeNaissance: new FormControl(utilisateurRawValue.dateDeNaissance),
      motdepasse: new FormControl(utilisateurRawValue.motdepasse),
      email: new FormControl(utilisateurRawValue.email),
      photo: new FormControl(utilisateurRawValue.photo),
      pwd: new FormControl(utilisateurRawValue.pwd),
      role: new FormControl(utilisateurRawValue.role, {
        validators: [Validators.required],
      }),
    });
  }

  getUtilisateur(form: UtilisateurFormGroup): IUtilisateur | NewUtilisateur {
    return this.convertUtilisateurRawValueToUtilisateur(form.getRawValue() as UtilisateurFormRawValue | NewUtilisateurFormRawValue);
  }

  resetForm(form: UtilisateurFormGroup, utilisateur: UtilisateurFormGroupInput): void {
    const utilisateurRawValue = this.convertUtilisateurToUtilisateurRawValue({ ...this.getFormDefaults(), ...utilisateur });
    form.reset(
      {
        ...utilisateurRawValue,
        id: { value: utilisateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilisateurFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDeNaissance: currentTime,
    };
  }

  private convertUtilisateurRawValueToUtilisateur(
    rawUtilisateur: UtilisateurFormRawValue | NewUtilisateurFormRawValue,
  ): IUtilisateur | NewUtilisateur {
    return {
      ...rawUtilisateur,
      dateDeNaissance: dayjs(rawUtilisateur.dateDeNaissance, DATE_TIME_FORMAT),
    };
  }

  private convertUtilisateurToUtilisateurRawValue(
    utilisateur: IUtilisateur | (Partial<NewUtilisateur> & UtilisateurFormDefaults),
  ): UtilisateurFormRawValue | PartialWithRequiredKeyOf<NewUtilisateurFormRawValue> {
    return {
      ...utilisateur,
      dateDeNaissance: utilisateur.dateDeNaissance ? utilisateur.dateDeNaissance.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
