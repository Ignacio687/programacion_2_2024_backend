import { IDevice } from 'app/entities/device/device.model';

export interface IExtra {
  id: number;
  supplierForeignId?: number | null;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  freePrice?: number | null;
  devices?: IDevice[] | null;
}

export type NewExtra = Omit<IExtra, 'id'> & { id: null };
