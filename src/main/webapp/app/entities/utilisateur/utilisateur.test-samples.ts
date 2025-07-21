import dayjs from 'dayjs/esm';

import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 7635,
  iduser: 'vaste derechef',
  login: 'quasiment',
  role: 'ADMIN',
};

export const sampleWithPartialData: IUtilisateur = {
  id: 12918,
  iduser: 'sous couleur de ouin accorder',
  login: "à l'égard de sauf à loufoque",
  email: 'Josephine_Sanchez2@yahoo.fr',
  pwd: 'pousser',
  role: 'ADMIN',
};

export const sampleWithFullData: IUtilisateur = {
  id: 18250,
  iduser: 'si bien que concernant',
  login: 'résister',
  nom: 'parce que groin groin autant',
  prenom: 'préciser tirer personnel',
  dateDeNaissance: dayjs('2025-07-20T09:58'),
  motdepasse: 'présidence vraisemblablement',
  email: 'Jourdain_Dupuy@yahoo.fr',
  photo: 'afin de',
  pwd: 'guide hôte plutôt',
  role: 'USER',
};

export const sampleWithNewData: NewUtilisateur = {
  iduser: 'hebdomadaire',
  login: 'de crainte que calme',
  role: 'USER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
