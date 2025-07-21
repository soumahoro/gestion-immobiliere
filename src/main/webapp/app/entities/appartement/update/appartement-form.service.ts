import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAppartement, NewAppartement } from '../appartement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppartement for edit and NewAppartementFormGroupInput for create.
 */
type AppartementFormGroupInput = IAppartement | PartialWithRequiredKeyOf<NewAppartement>;

type AppartementFormDefaults = Pick<NewAppartement, 'id'>;

type AppartementFormGroupContent = {
  id: FormControl<IAppartement['id'] | NewAppartement['id']>;
  idapp: FormControl<IAppartement['idapp']>;
  libelle: FormControl<IAppartement['libelle']>;
  loyer: FormControl<IAppartement['loyer']>;
  nbrepieces: FormControl<IAppartement['nbrepieces']>;
  taux: FormControl<IAppartement['taux']>;
  residence: FormControl<IAppartement['residence']>;
};

export type AppartementFormGroup = FormGroup<AppartementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppartementFormService {
  createAppartementFormGroup(appartement: AppartementFormGroupInput = { id: null }): AppartementFormGroup {
    const appartementRawValue = {
      ...this.getFormDefaults(),
      ...appartement,
    };
    return new FormGroup<AppartementFormGroupContent>({
      id: new FormControl(
        { value: appartementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idapp: new FormControl(appartementRawValue.idapp, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(appartementRawValue.libelle),
      loyer: new FormControl(appartementRawValue.loyer),
      nbrepieces: new FormControl(appartementRawValue.nbrepieces),
      taux: new FormControl(appartementRawValue.taux),
      residence: new FormControl(appartementRawValue.residence),
    });
  }

  getAppartement(form: AppartementFormGroup): IAppartement | NewAppartement {
    return form.getRawValue() as IAppartement | NewAppartement;
  }

  resetForm(form: AppartementFormGroup, appartement: AppartementFormGroupInput): void {
    const appartementRawValue = { ...this.getFormDefaults(), ...appartement };
    form.reset(
      {
        ...appartementRawValue,
        id: { value: appartementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppartementFormDefaults {
    return {
      id: null,
    };
  }
}
