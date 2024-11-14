import { ICharacteristic, NewCharacteristic } from './characteristic.model';

export const sampleWithRequiredData: ICharacteristic = {
  id: 27966,
  supplierForeignId: 26657,
  name: 'geez hm mmm',
};

export const sampleWithPartialData: ICharacteristic = {
  id: 2214,
  supplierForeignId: 31332,
  name: 'celebrated or yum',
  description: 'about brightly intensely',
};

export const sampleWithFullData: ICharacteristic = {
  id: 19110,
  supplierForeignId: 26186,
  name: 'edit devise eek',
  description: 'mechanically',
};

export const sampleWithNewData: NewCharacteristic = {
  supplierForeignId: 28723,
  name: 'phooey buying',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
