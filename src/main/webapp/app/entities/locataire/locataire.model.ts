import dayjs from 'dayjs/esm';
import { IAppartement } from 'app/entities/appartement/appartement.model';

export interface ILocataire {
  id: number;
  idloc?: string | null;
  arriere?: number | null;
  date?: dayjs.Dayjs | null;
  datedepart?: dayjs.Dayjs | null;
  fonction?: string | null;
  loyer?: number | null;
  motifdepart?: string | null;
  nationalite?: string | null;
  nom?: string | null;
  numpiece?: string | null;
  observation?: string | null;
  statut?: string | null;
  telephone?: string | null;
  typepiece?: string | null;
  appartement?: IAppartement | null;
}

export type NewLocataire = Omit<ILocataire, 'id'> & { id: null };
