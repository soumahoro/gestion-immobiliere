import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReglement, NewReglement } from '../reglement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReglement for edit and NewReglementFormGroupInput for create.
 */
type ReglementFormGroupInput = IReglement | PartialWithRequiredKeyOf<NewReglement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReglement | NewReglement> = Omit<T, 'date'> & {
  date?: string | null;
};

type ReglementFormRawValue = FormValueOf<IReglement>;

type NewReglementFormRawValue = FormValueOf<NewReglement>;

type ReglementFormDefaults = Pick<NewReglement, 'id' | 'date'>;

type ReglementFormGroupContent = {
  id: FormControl<ReglementFormRawValue['id'] | NewReglement['id']>;
  idreg: FormControl<ReglementFormRawValue['idreg']>;
  annee: FormControl<ReglementFormRawValue['annee']>;
  date: FormControl<ReglementFormRawValue['date']>;
  montant: FormControl<ReglementFormRawValue['montant']>;
  montantlettres: FormControl<ReglementFormRawValue['montantlettres']>;
  motif: FormControl<ReglementFormRawValue['motif']>;
  observ1: FormControl<ReglementFormRawValue['observ1']>;
  observ2: FormControl<ReglementFormRawValue['observ2']>;
  observ3: FormControl<ReglementFormRawValue['observ3']>;
  reste: FormControl<ReglementFormRawValue['reste']>;
  locataire: FormControl<ReglementFormRawValue['locataire']>;
  moi: FormControl<ReglementFormRawValue['moi']>;
  utilisateur: FormControl<ReglementFormRawValue['utilisateur']>;
};

export type ReglementFormGroup = FormGroup<ReglementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReglementFormService {
  createReglementFormGroup(reglement: ReglementFormGroupInput = { id: null }): ReglementFormGroup {
    const reglementRawValue = this.convertReglementToReglementRawValue({
      ...this.getFormDefaults(),
      ...reglement,
    });
    return new FormGroup<ReglementFormGroupContent>({
      id: new FormControl(
        { value: reglementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idreg: new FormControl(reglementRawValue.idreg),
      annee: new FormControl(reglementRawValue.annee),
      date: new FormControl(reglementRawValue.date),
      montant: new FormControl(reglementRawValue.montant),
      montantlettres: new FormControl(reglementRawValue.montantlettres),
      motif: new FormControl(reglementRawValue.motif),
      observ1: new FormControl(reglementRawValue.observ1),
      observ2: new FormControl(reglementRawValue.observ2),
      observ3: new FormControl(reglementRawValue.observ3),
      reste: new FormControl(reglementRawValue.reste),
      locataire: new FormControl(reglementRawValue.locataire),
      moi: new FormControl(reglementRawValue.moi),
      utilisateur: new FormControl(reglementRawValue.utilisateur),
    });
  }

  getReglement(form: ReglementFormGroup): IReglement | NewReglement {
    return this.convertReglementRawValueToReglement(form.getRawValue() as ReglementFormRawValue | NewReglementFormRawValue);
  }

  resetForm(form: ReglementFormGroup, reglement: ReglementFormGroupInput): void {
    const reglementRawValue = this.convertReglementToReglementRawValue({ ...this.getFormDefaults(), ...reglement });
    form.reset(
      {
        ...reglementRawValue,
        id: { value: reglementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReglementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertReglementRawValueToReglement(rawReglement: ReglementFormRawValue | NewReglementFormRawValue): IReglement | NewReglement {
    return {
      ...rawReglement,
      date: dayjs(rawReglement.date, DATE_TIME_FORMAT),
    };
  }

  private convertReglementToReglementRawValue(
    reglement: IReglement | (Partial<NewReglement> & ReglementFormDefaults),
  ): ReglementFormRawValue | PartialWithRequiredKeyOf<NewReglementFormRawValue> {
    return {
      ...reglement,
      date: reglement.date ? reglement.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
