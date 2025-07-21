import { IAppartement, NewAppartement } from './appartement.model';

export const sampleWithRequiredData: IAppartement = {
  id: 224,
  idapp: 'assez',
};

export const sampleWithPartialData: IAppartement = {
  id: 31522,
  idapp: 'circulaire fade concurrence',
  loyer: 858,
  nbrepieces: 20579,
};

export const sampleWithFullData: IAppartement = {
  id: 15398,
  idapp: "aujourd'hui sitôt que de la part de",
  libelle: 'étaler triathlète doucement',
  loyer: 24217,
  nbrepieces: 13822,
  taux: 10868,
};

export const sampleWithNewData: NewAppartement = {
  idapp: 'antagoniste incognito',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
