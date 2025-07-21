import { IAnnee, NewAnnee } from './annee.model';

export const sampleWithRequiredData: IAnnee = {
  id: 30083,
  an: 23255,
};

export const sampleWithPartialData: IAnnee = {
  id: 16037,
  an: 20875,
  libelle: 'même si',
};

export const sampleWithFullData: IAnnee = {
  id: 28871,
  an: 3647,
  libelle: 'autrement',
};

export const sampleWithNewData: NewAnnee = {
  an: 16184,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
