import { ICharacteristic } from 'app/entities/characteristic/characteristic.model';
import { IOption } from 'app/entities/option/option.model';
import { IExtra } from 'app/entities/extra/extra.model';

export interface IDevice {
  id: number;
  supplierForeignKey?: number | null;
  supplier?: string | null;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  basePrice?: number | null;
  currency?: string | null;
  active?: boolean | null;
  characteristics?: ICharacteristic[] | null;
  options?: IOption[] | null;
  extras?: IExtra[] | null;
}

export type NewDevice = Omit<IDevice, 'id'> & { id: null };
