import { IMoi, NewMoi } from './moi.model';

export const sampleWithRequiredData: IMoi = {
  id: 302,
  idmois: 16599,
};

export const sampleWithPartialData: IMoi = {
  id: 25376,
  idmois: 20602,
  mois: 'patientèle',
};

export const sampleWithFullData: IMoi = {
  id: 11745,
  idmois: 11191,
  mois: 'drôlement gigantesque triathlète',
};

export const sampleWithNewData: NewMoi = {
  idmois: 22122,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
