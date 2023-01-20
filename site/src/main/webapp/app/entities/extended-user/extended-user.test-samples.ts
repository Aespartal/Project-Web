import dayjs from 'dayjs/esm';

import { IExtendedUser, NewExtendedUser } from './extended-user.model';

export const sampleWithRequiredData: IExtendedUser = {
  height: 43390,
  weight: 6105,
  birthDate: dayjs('2022-12-07T20:52'),
};

export const sampleWithPartialData: IExtendedUser = {
  description: 'productividad Avon',
  location: 'Planificador PCI Operaciones',
  height: 83762,
  weight: 96759,
  birthDate: dayjs('2022-12-07T22:29'),
};

export const sampleWithFullData: IExtendedUser = {
  description: 'Inversor Futuro',
  location: 'Ergonómico calculating Checa',
  height: 81856,
  weight: 18113,
  birthDate: dayjs('2022-12-07T17:49'),
};

export const sampleWithNewData: NewExtendedUser = {
  height: 81072,
  weight: 41874,
  birthDate: dayjs('2022-12-08T11:54'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
