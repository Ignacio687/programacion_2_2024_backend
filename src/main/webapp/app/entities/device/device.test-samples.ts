import { IDevice, NewDevice } from './device.model';

export const sampleWithRequiredData: IDevice = {
  id: 15139,
  code: 'fooey',
  basePrice: 32655.99,
};

export const sampleWithPartialData: IDevice = {
  id: 18289,
  code: 'end',
  name: 'sniveling below',
  basePrice: 31708.62,
};

export const sampleWithFullData: IDevice = {
  id: 24117,
  code: 'doll huzzah',
  name: 'duh without',
  description: 'carefree',
  basePrice: 10042.41,
  currency: 'geez tide',
};

export const sampleWithNewData: NewDevice = {
  code: 'order kiddingly',
  basePrice: 25775.08,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
