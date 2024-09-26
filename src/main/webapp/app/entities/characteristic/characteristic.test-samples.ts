import { ICharacteristic, NewCharacteristic } from './characteristic.model';

export const sampleWithRequiredData: ICharacteristic = {
  id: 28038,
  name: 'and likewise aha',
};

export const sampleWithPartialData: ICharacteristic = {
  id: 23579,
  name: 'teleport',
  description: 'aside hasty hmph',
};

export const sampleWithFullData: ICharacteristic = {
  id: 27541,
  name: 'an',
  description: 'intensely',
};

export const sampleWithNewData: NewCharacteristic = {
  name: 'deficit of',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
