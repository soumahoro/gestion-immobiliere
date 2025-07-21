export interface IAdministrateur {
  id: number;
  code?: string | null;
}

export type NewAdministrateur = Omit<IAdministrateur, 'id'> & { id: null };
