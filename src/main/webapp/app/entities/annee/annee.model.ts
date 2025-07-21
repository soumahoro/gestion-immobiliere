export interface IAnnee {
  id: number;
  an?: number | null;
  libelle?: string | null;
}

export type NewAnnee = Omit<IAnnee, 'id'> & { id: null };
