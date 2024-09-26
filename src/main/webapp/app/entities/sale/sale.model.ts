import dayjs from 'dayjs/esm';
import { IDevice } from 'app/entities/device/device.model';

export interface ISale {
  id: number;
  devicePrice?: number | null;
  finalPrice?: number | null;
  saleDate?: dayjs.Dayjs | null;
  currency?: string | null;
  device?: IDevice | null;
}

export type NewSale = Omit<ISale, 'id'> & { id: null };
