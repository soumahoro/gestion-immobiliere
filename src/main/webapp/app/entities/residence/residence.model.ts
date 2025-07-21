import { IProprietaire } from 'app/entities/proprietaire/proprietaire.model';

export interface IResidence {
  id: number;
  idres?: string | null;
  ilot?: string | null;
  localisation?: string | null;
  observation?: string | null;
  quartier?: string | null;
  ville?: string | null;
  proprietaire?: IProprietaire | null;
}

export type NewResidence = Omit<IResidence, 'id'> & { id: null };
