import { ICharacteristic } from 'app/entities/characteristic/characteristic.model';
import { IExtra } from 'app/entities/extra/extra.model';
import { ICustomization } from 'app/entities/customization/customization.model';

export interface IDevice {
  id: number;
  supplierForeignId?: number | null;
  supplier?: string | null;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  basePrice?: number | null;
  currency?: string | null;
  active?: boolean | null;
  characteristics?: ICharacteristic[] | null;
  extras?: IExtra[] | null;
  customizations?: ICustomization[] | null;
}

export type NewDevice = Omit<IDevice, 'id'> & { id: null };
