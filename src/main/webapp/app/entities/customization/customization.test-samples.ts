import { ICustomization, NewCustomization } from './customization.model';

export const sampleWithRequiredData: ICustomization = {
  id: 27046,
  supplierForeignId: 16356,
  name: 'smug',
};

export const sampleWithPartialData: ICustomization = {
  id: 25621,
  supplierForeignId: 28785,
  name: 'alienated',
  description: 'once',
};

export const sampleWithFullData: ICustomization = {
  id: 11643,
  supplierForeignId: 10241,
  name: 'beautiful afore drink',
  description: 'jovial why',
};

export const sampleWithNewData: NewCustomization = {
  supplierForeignId: 3637,
  name: 'frank parrot motor',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
