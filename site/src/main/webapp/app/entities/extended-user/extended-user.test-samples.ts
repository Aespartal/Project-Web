import dayjs from 'dayjs/esm';

import { IExtendedUser, NewExtendedUser } from './extended-user.model';

export const sampleWithRequiredData: IExtendedUser = {
  height: 43390,
  weight: 6105,
  birthDate: dayjs('2022-12-07T20:52'),
  totalImages: 54378,
  totalNotifications: 55532,
};

export const sampleWithPartialData: IExtendedUser = {
  description: 'Decoración Planificador',
  height: 87767,
  weight: 35403,
  birthDate: dayjs('2022-12-07T22:12'),
  totalFollowers: 94108,
  totalFollowing: 52865,
  totalImages: 10807,
  totalNotifications: 83762,
};

export const sampleWithFullData: IExtendedUser = {
  description: 'alarm deposit discreta',
  location: 'Artesanal',
  height: 69624,
  weight: 85492,
  birthDate: dayjs('2022-12-08T02:24'),
  totalFollowers: 24997,
  totalFollowing: 76467,
  totalImages: 81856,
  totalNotifications: 18113,
};

export const sampleWithNewData: NewExtendedUser = {
  height: 90896,
  weight: 81072,
  birthDate: dayjs('2022-12-08T05:34'),
  totalImages: 15515,
  totalNotifications: 23666,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
