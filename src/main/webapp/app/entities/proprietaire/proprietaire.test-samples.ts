import dayjs from 'dayjs/esm';

import { IProprietaire, NewProprietaire } from './proprietaire.model';

export const sampleWithRequiredData: IProprietaire = {
  id: 22087,
  idpro: "d'avec dessous",
};

export const sampleWithPartialData: IProprietaire = {
  id: 4813,
  idpro: 'hypocrite',
  date: dayjs('2025-07-20T07:27'),
};

export const sampleWithFullData: IProprietaire = {
  id: 9125,
  idpro: 'hi',
  date: dayjs('2025-07-20T00:18'),
  nom: 'longtemps même si',
  residence: "à l'entour de amorphe gens",
  tel: 'vu que toc-toc',
};

export const sampleWithNewData: NewProprietaire = {
  idpro: 'passablement alors que',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
