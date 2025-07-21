import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILocataire, NewLocataire } from '../locataire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocataire for edit and NewLocataireFormGroupInput for create.
 */
type LocataireFormGroupInput = ILocataire | PartialWithRequiredKeyOf<NewLocataire>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILocataire | NewLocataire> = Omit<T, 'date' | 'datedepart'> & {
  date?: string | null;
  datedepart?: string | null;
};

type LocataireFormRawValue = FormValueOf<ILocataire>;

type NewLocataireFormRawValue = FormValueOf<NewLocataire>;

type LocataireFormDefaults = Pick<NewLocataire, 'id' | 'date' | 'datedepart'>;

type LocataireFormGroupContent = {
  id: FormControl<LocataireFormRawValue['id'] | NewLocataire['id']>;
  idloc: FormControl<LocataireFormRawValue['idloc']>;
  arriere: FormControl<LocataireFormRawValue['arriere']>;
  date: FormControl<LocataireFormRawValue['date']>;
  datedepart: FormControl<LocataireFormRawValue['datedepart']>;
  fonction: FormControl<LocataireFormRawValue['fonction']>;
  loyer: FormControl<LocataireFormRawValue['loyer']>;
  motifdepart: FormControl<LocataireFormRawValue['motifdepart']>;
  nationalite: FormControl<LocataireFormRawValue['nationalite']>;
  nom: FormControl<LocataireFormRawValue['nom']>;
  numpiece: FormControl<LocataireFormRawValue['numpiece']>;
  observation: FormControl<LocataireFormRawValue['observation']>;
  statut: FormControl<LocataireFormRawValue['statut']>;
  telephone: FormControl<LocataireFormRawValue['telephone']>;
  typepiece: FormControl<LocataireFormRawValue['typepiece']>;
  appartement: FormControl<LocataireFormRawValue['appartement']>;
};

export type LocataireFormGroup = FormGroup<LocataireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocataireFormService {
  createLocataireFormGroup(locataire: LocataireFormGroupInput = { id: null }): LocataireFormGroup {
    const locataireRawValue = this.convertLocataireToLocataireRawValue({
      ...this.getFormDefaults(),
      ...locataire,
    });
    return new FormGroup<LocataireFormGroupContent>({
      id: new FormControl(
        { value: locataireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idloc: new FormControl(locataireRawValue.idloc, {
        validators: [Validators.required],
      }),
      arriere: new FormControl(locataireRawValue.arriere),
      date: new FormControl(locataireRawValue.date),
      datedepart: new FormControl(locataireRawValue.datedepart),
      fonction: new FormControl(locataireRawValue.fonction),
      loyer: new FormControl(locataireRawValue.loyer),
      motifdepart: new FormControl(locataireRawValue.motifdepart),
      nationalite: new FormControl(locataireRawValue.nationalite),
      nom: new FormControl(locataireRawValue.nom),
      numpiece: new FormControl(locataireRawValue.numpiece),
      observation: new FormControl(locataireRawValue.observation),
      statut: new FormControl(locataireRawValue.statut),
      telephone: new FormControl(locataireRawValue.telephone),
      typepiece: new FormControl(locataireRawValue.typepiece),
      appartement: new FormControl(locataireRawValue.appartement),
    });
  }

  getLocataire(form: LocataireFormGroup): ILocataire | NewLocataire {
    return this.convertLocataireRawValueToLocataire(form.getRawValue() as LocataireFormRawValue | NewLocataireFormRawValue);
  }

  resetForm(form: LocataireFormGroup, locataire: LocataireFormGroupInput): void {
    const locataireRawValue = this.convertLocataireToLocataireRawValue({ ...this.getFormDefaults(), ...locataire });
    form.reset(
      {
        ...locataireRawValue,
        id: { value: locataireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LocataireFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      datedepart: currentTime,
    };
  }

  private convertLocataireRawValueToLocataire(rawLocataire: LocataireFormRawValue | NewLocataireFormRawValue): ILocataire | NewLocataire {
    return {
      ...rawLocataire,
      date: dayjs(rawLocataire.date, DATE_TIME_FORMAT),
      datedepart: dayjs(rawLocataire.datedepart, DATE_TIME_FORMAT),
    };
  }

  private convertLocataireToLocataireRawValue(
    locataire: ILocataire | (Partial<NewLocataire> & LocataireFormDefaults),
  ): LocataireFormRawValue | PartialWithRequiredKeyOf<NewLocataireFormRawValue> {
    return {
      ...locataire,
      date: locataire.date ? locataire.date.format(DATE_TIME_FORMAT) : undefined,
      datedepart: locataire.datedepart ? locataire.datedepart.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
