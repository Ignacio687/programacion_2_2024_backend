import { IOption, NewOption } from './option.model';

export const sampleWithRequiredData: IOption = {
  id: 13507,
  code: 'decorate miserably definitive',
  additionalPrice: 1721.55,
};

export const sampleWithPartialData: IOption = {
  id: 15046,
  code: 'promptly pace',
  name: 'oh',
  description: 'putrid nor',
  additionalPrice: 23679.11,
};

export const sampleWithFullData: IOption = {
  id: 8103,
  code: 'unlike save thwack',
  name: 'forenenst',
  description: 'education depersonalize centralize',
  additionalPrice: 6083.6,
};

export const sampleWithNewData: NewOption = {
  code: 'except yum regarding',
  additionalPrice: 23992.4,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
