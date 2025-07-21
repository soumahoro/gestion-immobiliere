import { IResidence, NewResidence } from './residence.model';

export const sampleWithRequiredData: IResidence = {
  id: 30750,
  idres: 'énorme courir',
};

export const sampleWithPartialData: IResidence = {
  id: 29822,
  idres: 'vouh en dépit de ouch',
  observation: 'où',
  ville: 'propre lectorat',
};

export const sampleWithFullData: IResidence = {
  id: 24979,
  idres: 'aussitôt que plus',
  ilot: 'séparer',
  localisation: 'à moins de hi apte',
  observation: 'dès que',
  quartier: 'de manière à électorat auprès de',
  ville: 'où joliment divinement',
};

export const sampleWithNewData: NewResidence = {
  idres: 'buter proche de voler',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
