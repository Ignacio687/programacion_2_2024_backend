export interface ICustomization {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewCustomization = Omit<ICustomization, 'id'> & { id: null };
