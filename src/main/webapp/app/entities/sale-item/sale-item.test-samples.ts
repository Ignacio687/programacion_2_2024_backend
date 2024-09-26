import { ISaleItem, NewSaleItem } from './sale-item.model';

export const sampleWithRequiredData: ISaleItem = {
  id: 9223,
  price: 19456.73,
};

export const sampleWithPartialData: ISaleItem = {
  id: 6383,
  price: 13047.07,
};

export const sampleWithFullData: ISaleItem = {
  id: 23899,
  price: 26438.25,
};

export const sampleWithNewData: NewSaleItem = {
  price: 22288.23,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
