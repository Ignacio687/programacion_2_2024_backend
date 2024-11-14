export interface ICustomization {
  id: number;
  supplierForeignId?: number | null;
  name?: string | null;
  description?: string | null;
}

export type NewCustomization = Omit<ICustomization, 'id'> & { id: null };
