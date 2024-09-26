import dayjs from 'dayjs/esm';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 31701,
  devicePrice: 18975.54,
  finalPrice: 13175.97,
  saleDate: dayjs('2024-09-26T08:54'),
};

export const sampleWithPartialData: ISale = {
  id: 23899,
  devicePrice: 12825.66,
  finalPrice: 20056.81,
  saleDate: dayjs('2024-09-26T17:43'),
};

export const sampleWithFullData: ISale = {
  id: 13349,
  devicePrice: 28089.45,
  finalPrice: 15563.07,
  saleDate: dayjs('2024-09-26T07:53'),
  currency: 'psst room crystallize',
};

export const sampleWithNewData: NewSale = {
  devicePrice: 13124.46,
  finalPrice: 5572.53,
  saleDate: dayjs('2024-09-26T13:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
