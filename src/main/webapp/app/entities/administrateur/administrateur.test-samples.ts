import { IAdministrateur, NewAdministrateur } from './administrateur.model';

export const sampleWithRequiredData: IAdministrateur = {
  id: 14874,
  code: 'très',
};

export const sampleWithPartialData: IAdministrateur = {
  id: 25501,
  code: 'à défaut de',
};

export const sampleWithFullData: IAdministrateur = {
  id: 15432,
  code: 'personnel membre à vie équipe',
};

export const sampleWithNewData: NewAdministrateur = {
  code: 'comme',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
