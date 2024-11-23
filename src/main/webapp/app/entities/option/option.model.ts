import { ICustomization } from 'app/entities/customization/customization.model';

export interface IOption {
  id: number;
  supplierForeignId?: number | null;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  additionalPrice?: number | null;
  customizations?: ICustomization[] | null;
}

export type NewOption = Omit<IOption, 'id'> & { id: null };
