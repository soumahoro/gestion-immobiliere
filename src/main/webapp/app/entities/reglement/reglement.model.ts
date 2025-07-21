import dayjs from 'dayjs/esm';
import { ILocataire } from 'app/entities/locataire/locataire.model';
import { IMoi } from 'app/entities/moi/moi.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IReglement {
  id: number;
  idreg?: number | null;
  annee?: number | null;
  date?: dayjs.Dayjs | null;
  montant?: number | null;
  montantlettres?: string | null;
  motif?: string | null;
  observ1?: string | null;
  observ2?: string | null;
  observ3?: string | null;
  reste?: number | null;
  locataire?: ILocataire | null;
  moi?: IMoi | null;
  utilisateur?: IUtilisateur | null;
}

export type NewReglement = Omit<IReglement, 'id'> & { id: null };
