export interface IMoi {
  id: number;
  idmois?: number | null;
  mois?: string | null;
}

export type NewMoi = Omit<IMoi, 'id'> & { id: null };
