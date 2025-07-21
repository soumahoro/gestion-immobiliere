import dayjs from 'dayjs/esm';

export interface IProprietaire {
  id: number;
  idpro?: string | null;
  date?: dayjs.Dayjs | null;
  nom?: string | null;
  residence?: string | null;
  tel?: string | null;
}

export type NewProprietaire = Omit<IProprietaire, 'id'> & { id: null };
