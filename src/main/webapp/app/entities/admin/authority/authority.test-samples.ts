import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'fc09b1e1-f89c-4661-bed1-8d4a1c5faed2',
};

export const sampleWithPartialData: IAuthority = {
  name: '4666758e-f268-4048-b387-09cee1d20dbc',
};

export const sampleWithFullData: IAuthority = {
  name: 'c938c239-bc89-484c-8167-676a1f90478b',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
