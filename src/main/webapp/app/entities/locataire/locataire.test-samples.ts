import dayjs from 'dayjs/esm';

import { ILocataire, NewLocataire } from './locataire.model';

export const sampleWithRequiredData: ILocataire = {
  id: 12384,
  idloc: "pendant que main-d’œuvre d'avec",
};

export const sampleWithPartialData: ILocataire = {
  id: 26347,
  idloc: 'puisque diablement',
  date: dayjs('2025-07-20T08:51'),
  fonction: 'partenaire à peu près actionnaire',
  loyer: 25477,
  numpiece: 'avare',
  observation: 'tantôt près de',
  telephone: '0632294338',
  typepiece: 'lâche appuyer orange',
};

export const sampleWithFullData: ILocataire = {
  id: 29993,
  idloc: 'gravir doucement étant donné que',
  arriere: 24762,
  date: dayjs('2025-07-20T10:43'),
  datedepart: dayjs('2025-07-20T19:14'),
  fonction: 'sous couleur de dring foule',
  loyer: 5153,
  motifdepart: 'grâce à gestionnaire',
  nationalite: 'loufoque signaler',
  nom: 'dedans de manière à ce que',
  numpiece: 'au-dessous de propre tchou tchouu',
  observation: 'entre',
  statut: 'foule',
  telephone: '0242635162',
  typepiece: 'manger dans la mesure où',
};

export const sampleWithNewData: NewLocataire = {
  idloc: 'gestionnaire coac coac pressentir',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
