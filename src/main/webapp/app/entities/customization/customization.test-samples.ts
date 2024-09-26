import { ICustomization, NewCustomization } from './customization.model';

export const sampleWithRequiredData: ICustomization = {
  id: 8165,
  name: 'inasmuch suddenly',
};

export const sampleWithPartialData: ICustomization = {
  id: 6881,
  name: 'craw',
};

export const sampleWithFullData: ICustomization = {
  id: 23687,
  name: 'besides',
  description: 'beautiful afore drink',
};

export const sampleWithNewData: NewCustomization = {
  name: 'jovial why',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
