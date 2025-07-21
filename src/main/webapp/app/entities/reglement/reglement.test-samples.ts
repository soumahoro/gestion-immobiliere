import dayjs from 'dayjs/esm';

import { IReglement, NewReglement } from './reglement.model';

export const sampleWithRequiredData: IReglement = {
  id: 17187,
};

export const sampleWithPartialData: IReglement = {
  id: 28292,
  idreg: 11136,
  date: dayjs('2025-07-20T13:29'),
  montant: 28449,
  montantlettres: '../fake-data/blob/hipster.txt',
  motif: 'glouglou puis',
  observ3: 'franco autrement',
  reste: 17145,
};

export const sampleWithFullData: IReglement = {
  id: 21658,
  idreg: 8757,
  annee: 31237,
  date: dayjs('2025-07-20T21:22'),
  montant: 3178,
  montantlettres: '../fake-data/blob/hipster.txt',
  motif: 'contre avant de',
  observ1: 'malgré vlan',
  observ2: 'avouer vaste',
  observ3: 'plic pauvre',
  reste: 16562,
};

export const sampleWithNewData: NewReglement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
