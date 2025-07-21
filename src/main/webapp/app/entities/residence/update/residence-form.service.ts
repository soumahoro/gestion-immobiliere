import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IResidence, NewResidence } from '../residence.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResidence for edit and NewResidenceFormGroupInput for create.
 */
type ResidenceFormGroupInput = IResidence | PartialWithRequiredKeyOf<NewResidence>;

type ResidenceFormDefaults = Pick<NewResidence, 'id'>;

type ResidenceFormGroupContent = {
  id: FormControl<IResidence['id'] | NewResidence['id']>;
  idres: FormControl<IResidence['idres']>;
  ilot: FormControl<IResidence['ilot']>;
  localisation: FormControl<IResidence['localisation']>;
  observation: FormControl<IResidence['observation']>;
  quartier: FormControl<IResidence['quartier']>;
  ville: FormControl<IResidence['ville']>;
  proprietaire: FormControl<IResidence['proprietaire']>;
};

export type ResidenceFormGroup = FormGroup<ResidenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResidenceFormService {
  createResidenceFormGroup(residence: ResidenceFormGroupInput = { id: null }): ResidenceFormGroup {
    const residenceRawValue = {
      ...this.getFormDefaults(),
      ...residence,
    };
    return new FormGroup<ResidenceFormGroupContent>({
      id: new FormControl(
        { value: residenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idres: new FormControl(residenceRawValue.idres, {
        validators: [Validators.required],
      }),
      ilot: new FormControl(residenceRawValue.ilot),
      localisation: new FormControl(residenceRawValue.localisation),
      observation: new FormControl(residenceRawValue.observation),
      quartier: new FormControl(residenceRawValue.quartier),
      ville: new FormControl(residenceRawValue.ville),
      proprietaire: new FormControl(residenceRawValue.proprietaire),
    });
  }

  getResidence(form: ResidenceFormGroup): IResidence | NewResidence {
    return form.getRawValue() as IResidence | NewResidence;
  }

  resetForm(form: ResidenceFormGroup, residence: ResidenceFormGroupInput): void {
    const residenceRawValue = { ...this.getFormDefaults(), ...residence };
    form.reset(
      {
        ...residenceRawValue,
        id: { value: residenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResidenceFormDefaults {
    return {
      id: null,
    };
  }
}
