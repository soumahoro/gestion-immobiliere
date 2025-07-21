import dayjs from 'dayjs/esm';
import { Role } from 'app/entities/enumerations/role.model';

export interface IUtilisateur {
  id: number;
  iduser?: string | null;
  login?: string | null;
  nom?: string | null;
  prenom?: string | null;
  dateDeNaissance?: dayjs.Dayjs | null;
  motdepasse?: string | null;
  email?: string | null;
  photo?: string | null;
  pwd?: string | null;
  role?: keyof typeof Role | null;
}

export type NewUtilisateur = Omit<IUtilisateur, 'id'> & { id: null };
