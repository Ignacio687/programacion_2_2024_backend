import { IDevice, NewDevice } from './device.model';

export const sampleWithRequiredData: IDevice = {
  id: 23054,
  supplierForeignKey: 30699,
  supplier: 'that',
  code: 'indeed for',
  basePrice: 21738.25,
};

export const sampleWithPartialData: IDevice = {
  id: 10944,
  supplierForeignKey: 22344,
  supplier: 'apse fork',
  code: 'uh-huh geez',
  basePrice: 15433.94,
  currency: 'ruddy cringe',
  active: true,
};

export const sampleWithFullData: IDevice = {
  id: 1624,
  supplierForeignKey: 11957,
  supplier: 'aw rudely ex-husband',
  code: 'gah',
  name: 'well behind elegantly',
  description: 'ptarmigan',
  basePrice: 17777.84,
  currency: 'noted',
  active: false,
};

export const sampleWithNewData: NewDevice = {
  supplierForeignKey: 3697,
  supplier: 'on researches since',
  code: 'book apud',
  basePrice: 30062.72,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
