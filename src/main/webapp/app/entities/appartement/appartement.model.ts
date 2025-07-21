import { IResidence } from 'app/entities/residence/residence.model';

export interface IAppartement {
  id: number;
  idapp?: string | null;
  libelle?: string | null;
  loyer?: number | null;
  nbrepieces?: number | null;
  taux?: number | null;
  residence?: IResidence | null;
}

export type NewAppartement = Omit<IAppartement, 'id'> & { id: null };
