import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 1380,
  login: 'geFO',
};

export const sampleWithPartialData: IUser = {
  id: 25713,
  login: '0mL6@qVh\\\\Sfgjkz\\;T9iZiA',
};

export const sampleWithFullData: IUser = {
  id: 11510,
  login: 'w@6N',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
