import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAdministrateur, NewAdministrateur } from '../administrateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdministrateur for edit and NewAdministrateurFormGroupInput for create.
 */
type AdministrateurFormGroupInput = IAdministrateur | PartialWithRequiredKeyOf<NewAdministrateur>;

type AdministrateurFormDefaults = Pick<NewAdministrateur, 'id'>;

type AdministrateurFormGroupContent = {
  id: FormControl<IAdministrateur['id'] | NewAdministrateur['id']>;
  code: FormControl<IAdministrateur['code']>;
};

export type AdministrateurFormGroup = FormGroup<AdministrateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdministrateurFormService {
  createAdministrateurFormGroup(administrateur: AdministrateurFormGroupInput = { id: null }): AdministrateurFormGroup {
    const administrateurRawValue = {
      ...this.getFormDefaults(),
      ...administrateur,
    };
    return new FormGroup<AdministrateurFormGroupContent>({
      id: new FormControl(
        { value: administrateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(administrateurRawValue.code, {
        validators: [Validators.required],
      }),
    });
  }

  getAdministrateur(form: AdministrateurFormGroup): IAdministrateur | NewAdministrateur {
    return form.getRawValue() as IAdministrateur | NewAdministrateur;
  }

  resetForm(form: AdministrateurFormGroup, administrateur: AdministrateurFormGroupInput): void {
    const administrateurRawValue = { ...this.getFormDefaults(), ...administrateur };
    form.reset(
      {
        ...administrateurRawValue,
        id: { value: administrateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AdministrateurFormDefaults {
    return {
      id: null,
    };
  }
}
