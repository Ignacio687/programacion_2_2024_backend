import dayjs from 'dayjs/esm';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 18976,
  devicePrice: 13175.97,
  finalPrice: 19464.72,
  saleDate: dayjs('2024-09-26T00:57'),
};

export const sampleWithPartialData: ISale = {
  id: 20057,
  devicePrice: 7434.69,
  finalPrice: 13348.77,
  saleDate: dayjs('2024-09-26T02:35'),
  finalized: true,
};

export const sampleWithFullData: ISale = {
  id: 20855,
  devicePrice: 29004.18,
  finalPrice: 11880.48,
  saleDate: dayjs('2024-09-26T22:08'),
  currency: 'righteously',
  finalized: true,
};

export const sampleWithNewData: NewSale = {
  devicePrice: 402.53,
  finalPrice: 6953.06,
  saleDate: dayjs('2024-09-26T05:04'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
