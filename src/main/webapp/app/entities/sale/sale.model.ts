import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IDevice } from 'app/entities/device/device.model';

export interface ISale {
  id: number;
  supplierForeignId?: number | null;
  devicePrice?: number | null;
  finalPrice?: number | null;
  saleDate?: dayjs.Dayjs | null;
  currency?: string | null;
  finalized?: boolean | null;
  user?: Pick<IUser, 'id'> | null;
  device?: IDevice | null;
}

export type NewSale = Omit<ISale, 'id'> & { id: null };
