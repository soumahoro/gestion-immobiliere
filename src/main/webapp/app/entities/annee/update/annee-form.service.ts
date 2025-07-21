import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAnnee, NewAnnee } from '../annee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAnnee for edit and NewAnneeFormGroupInput for create.
 */
type AnneeFormGroupInput = IAnnee | PartialWithRequiredKeyOf<NewAnnee>;

type AnneeFormDefaults = Pick<NewAnnee, 'id'>;

type AnneeFormGroupContent = {
  id: FormControl<IAnnee['id'] | NewAnnee['id']>;
  an: FormControl<IAnnee['an']>;
  libelle: FormControl<IAnnee['libelle']>;
};

export type AnneeFormGroup = FormGroup<AnneeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AnneeFormService {
  createAnneeFormGroup(annee: AnneeFormGroupInput = { id: null }): AnneeFormGroup {
    const anneeRawValue = {
      ...this.getFormDefaults(),
      ...annee,
    };
    return new FormGroup<AnneeFormGroupContent>({
      id: new FormControl(
        { value: anneeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      an: new FormControl(anneeRawValue.an, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(anneeRawValue.libelle),
    });
  }

  getAnnee(form: AnneeFormGroup): IAnnee | NewAnnee {
    return form.getRawValue() as IAnnee | NewAnnee;
  }

  resetForm(form: AnneeFormGroup, annee: AnneeFormGroupInput): void {
    const anneeRawValue = { ...this.getFormDefaults(), ...annee };
    form.reset(
      {
        ...anneeRawValue,
        id: { value: anneeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AnneeFormDefaults {
    return {
      id: null,
    };
  }
}
