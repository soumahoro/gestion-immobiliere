import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProprietaire, NewProprietaire } from '../proprietaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProprietaire for edit and NewProprietaireFormGroupInput for create.
 */
type ProprietaireFormGroupInput = IProprietaire | PartialWithRequiredKeyOf<NewProprietaire>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProprietaire | NewProprietaire> = Omit<T, 'date'> & {
  date?: string | null;
};

type ProprietaireFormRawValue = FormValueOf<IProprietaire>;

type NewProprietaireFormRawValue = FormValueOf<NewProprietaire>;

type ProprietaireFormDefaults = Pick<NewProprietaire, 'id' | 'date'>;

type ProprietaireFormGroupContent = {
  id: FormControl<ProprietaireFormRawValue['id'] | NewProprietaire['id']>;
  idpro: FormControl<ProprietaireFormRawValue['idpro']>;
  date: FormControl<ProprietaireFormRawValue['date']>;
  nom: FormControl<ProprietaireFormRawValue['nom']>;
  residence: FormControl<ProprietaireFormRawValue['residence']>;
  tel: FormControl<ProprietaireFormRawValue['tel']>;
};

export type ProprietaireFormGroup = FormGroup<ProprietaireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProprietaireFormService {
  createProprietaireFormGroup(proprietaire: ProprietaireFormGroupInput = { id: null }): ProprietaireFormGroup {
    const proprietaireRawValue = this.convertProprietaireToProprietaireRawValue({
      ...this.getFormDefaults(),
      ...proprietaire,
    });
    return new FormGroup<ProprietaireFormGroupContent>({
      id: new FormControl(
        { value: proprietaireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idpro: new FormControl(proprietaireRawValue.idpro, {
        validators: [Validators.required],
      }),
      date: new FormControl(proprietaireRawValue.date),
      nom: new FormControl(proprietaireRawValue.nom),
      residence: new FormControl(proprietaireRawValue.residence),
      tel: new FormControl(proprietaireRawValue.tel),
    });
  }

  getProprietaire(form: ProprietaireFormGroup): IProprietaire | NewProprietaire {
    return this.convertProprietaireRawValueToProprietaire(form.getRawValue() as ProprietaireFormRawValue | NewProprietaireFormRawValue);
  }

  resetForm(form: ProprietaireFormGroup, proprietaire: ProprietaireFormGroupInput): void {
    const proprietaireRawValue = this.convertProprietaireToProprietaireRawValue({ ...this.getFormDefaults(), ...proprietaire });
    form.reset(
      {
        ...proprietaireRawValue,
        id: { value: proprietaireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProprietaireFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertProprietaireRawValueToProprietaire(
    rawProprietaire: ProprietaireFormRawValue | NewProprietaireFormRawValue,
  ): IProprietaire | NewProprietaire {
    return {
      ...rawProprietaire,
      date: dayjs(rawProprietaire.date, DATE_TIME_FORMAT),
    };
  }

  private convertProprietaireToProprietaireRawValue(
    proprietaire: IProprietaire | (Partial<NewProprietaire> & ProprietaireFormDefaults),
  ): ProprietaireFormRawValue | PartialWithRequiredKeyOf<NewProprietaireFormRawValue> {
    return {
      ...proprietaire,
      date: proprietaire.date ? proprietaire.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
