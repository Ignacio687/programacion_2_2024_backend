import { ICustomization, NewCustomization } from './customization.model';

export const sampleWithRequiredData: ICustomization = {
  id: 12583,
  supplierForeignId: 24486,
  name: 'gee cosset',
};

export const sampleWithPartialData: ICustomization = {
  id: 24905,
  supplierForeignId: 14003,
  name: 'coolly when',
  description: 'searchingly',
};

export const sampleWithFullData: ICustomization = {
  id: 4578,
  supplierForeignId: 16070,
  name: 'rudely miserable yellow',
  description: 'woot if concerning',
};

export const sampleWithNewData: NewCustomization = {
  supplierForeignId: 13630,
  name: 'lively geez',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
