import { IOption } from 'app/entities/option/option.model';
import { IDevice } from 'app/entities/device/device.model';

export interface ICustomization {
  id: number;
  supplierForeignId?: number | null;
  name?: string | null;
  description?: string | null;
  options?: IOption[] | null;
  devices?: IDevice[] | null;
}

export type NewCustomization = Omit<ICustomization, 'id'> & { id: null };
