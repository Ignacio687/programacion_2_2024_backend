import { IExtra, NewExtra } from './extra.model';

export const sampleWithRequiredData: IExtra = {
  id: 17165,
  supplierForeignId: 2084,
  name: 'past aha',
  price: 9208.35,
  freePrice: 28433.73,
};

export const sampleWithPartialData: IExtra = {
  id: 28860,
  supplierForeignId: 26328,
  name: 'mmm indeed',
  price: 1563.4,
  freePrice: 1403.9,
};

export const sampleWithFullData: IExtra = {
  id: 14634,
  supplierForeignId: 1047,
  name: 'chairman kindheartedly furthermore',
  description: 'boohoo sustenance',
  price: 21403.29,
  freePrice: 24224.66,
};

export const sampleWithNewData: NewExtra = {
  supplierForeignId: 10294,
  name: 'boastfully phooey',
  price: 20969.71,
  freePrice: 5188.01,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
