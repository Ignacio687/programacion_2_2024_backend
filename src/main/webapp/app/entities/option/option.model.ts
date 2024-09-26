import { ICustomization } from 'app/entities/customization/customization.model';
import { IDevice } from 'app/entities/device/device.model';

export interface IOption {
  id: number;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  additionalPrice?: number | null;
  customization?: ICustomization | null;
  devices?: IDevice[] | null;
}

export type NewOption = Omit<IOption, 'id'> & { id: null };
