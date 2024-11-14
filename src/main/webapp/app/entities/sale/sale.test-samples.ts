import dayjs from 'dayjs/esm';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 13176,
  devicePrice: 19464.72,
  finalPrice: 30311.23,
  saleDate: dayjs('2024-09-26T05:39'),
};

export const sampleWithPartialData: ISale = {
  id: 13349,
  supplierForeignId: 28090,
  devicePrice: 15563.07,
  finalPrice: 20855,
  saleDate: dayjs('2024-09-26T01:55'),
  finalized: true,
};

export const sampleWithFullData: ISale = {
  id: 1403,
  supplierForeignId: 10216,
  devicePrice: 24154.26,
  finalPrice: 1153.06,
  saleDate: dayjs('2024-09-26T10:24'),
  currency: 'room crystallize',
  finalized: true,
};

export const sampleWithNewData: NewSale = {
  devicePrice: 5572.53,
  finalPrice: 13539.72,
  saleDate: dayjs('2024-09-26T06:20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
