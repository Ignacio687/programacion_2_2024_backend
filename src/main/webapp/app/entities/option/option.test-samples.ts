import { IOption, NewOption } from './option.model';

export const sampleWithRequiredData: IOption = {
  id: 22856,
  supplierForeignId: 19215,
  code: 'phew yippee',
  additionalPrice: 26071.78,
};

export const sampleWithPartialData: IOption = {
  id: 20197,
  supplierForeignId: 5968,
  code: 'upon',
  description: 'than',
  additionalPrice: 14284.4,
};

export const sampleWithFullData: IOption = {
  id: 11156,
  supplierForeignId: 22693,
  code: 'oh',
  name: 'putrid nor',
  description: 'aboard however on',
  additionalPrice: 8790.86,
};

export const sampleWithNewData: NewOption = {
  supplierForeignId: 29790,
  code: 'forenenst',
  additionalPrice: 23844.36,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
