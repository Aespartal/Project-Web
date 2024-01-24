import dayjs from 'dayjs/esm';

import { NotificationType } from 'app/entities/enumerations/notification-type.model';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 30621,
  message: 'migración',
  type: NotificationType['COMMENTARY'],
  creationDate: dayjs('2023-07-26T18:23'),
};

export const sampleWithPartialData: INotification = {
  id: 25492,
  message: 'Apartamento',
  type: NotificationType['FOLLOW'],
  creationDate: dayjs('2023-07-26T20:57'),
};

export const sampleWithFullData: INotification = {
  id: 23627,
  message: 'Berkshire',
  type: NotificationType['LIKE_IMAGE'],
  creationDate: dayjs('2023-07-27T01:17'),
};

export const sampleWithNewData: NewNotification = {
  message: 'Baleares Swedish Increible',
  type: NotificationType['LIKE_COMMENTARY'],
  creationDate: dayjs('2023-07-26T16:02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
