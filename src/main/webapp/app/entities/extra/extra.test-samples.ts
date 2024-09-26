import { IExtra, NewExtra } from './extra.model';

export const sampleWithRequiredData: IExtra = {
  id: 9242,
  name: 'oof towards',
  price: 24954.86,
  freePrice: 13329.99,
};

export const sampleWithPartialData: IExtra = {
  id: 28434,
  name: 'how donkey',
  description: 'pro chairman',
  price: 4124.19,
  freePrice: 348.57,
};

export const sampleWithFullData: IExtra = {
  id: 27869,
  name: 'huzzah',
  description: 'grumble',
  price: 31021.34,
  freePrice: 15593.89,
};

export const sampleWithNewData: NewExtra = {
  name: 'sustenance',
  price: 21403.29,
  freePrice: 24224.66,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
