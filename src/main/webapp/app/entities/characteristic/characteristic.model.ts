import { IDevice } from 'app/entities/device/device.model';

export interface ICharacteristic {
  id: number;
  name?: string | null;
  description?: string | null;
  devices?: IDevice[] | null;
}

export type NewCharacteristic = Omit<ICharacteristic, 'id'> & { id: null };
