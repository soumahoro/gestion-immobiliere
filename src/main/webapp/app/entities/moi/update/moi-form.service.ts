import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMoi, NewMoi } from '../moi.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMoi for edit and NewMoiFormGroupInput for create.
 */
type MoiFormGroupInput = IMoi | PartialWithRequiredKeyOf<NewMoi>;

type MoiFormDefaults = Pick<NewMoi, 'id'>;

type MoiFormGroupContent = {
  id: FormControl<IMoi['id'] | NewMoi['id']>;
  idmois: FormControl<IMoi['idmois']>;
  mois: FormControl<IMoi['mois']>;
};

export type MoiFormGroup = FormGroup<MoiFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MoiFormService {
  createMoiFormGroup(moi: MoiFormGroupInput = { id: null }): MoiFormGroup {
    const moiRawValue = {
      ...this.getFormDefaults(),
      ...moi,
    };
    return new FormGroup<MoiFormGroupContent>({
      id: new FormControl(
        { value: moiRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idmois: new FormControl(moiRawValue.idmois, {
        validators: [Validators.required],
      }),
      mois: new FormControl(moiRawValue.mois),
    });
  }

  getMoi(form: MoiFormGroup): IMoi | NewMoi {
    return form.getRawValue() as IMoi | NewMoi;
  }

  resetForm(form: MoiFormGroup, moi: MoiFormGroupInput): void {
    const moiRawValue = { ...this.getFormDefaults(), ...moi };
    form.reset(
      {
        ...moiRawValue,
        id: { value: moiRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MoiFormDefaults {
    return {
      id: null,
    };
  }
}
