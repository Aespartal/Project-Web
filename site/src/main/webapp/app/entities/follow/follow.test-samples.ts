import dayjs from 'dayjs/esm';

import { FollowState } from 'app/entities/enumerations/follow-state.model';

import { IFollow, NewFollow } from './follow.model';

export const sampleWithRequiredData: IFollow = {
  id: 15849,
  state: FollowState['ACCEPTED'],
  creationDate: dayjs('2023-07-27T04:54'),
};

export const sampleWithPartialData: IFollow = {
  id: 20839,
  state: FollowState['PENDING'],
  creationDate: dayjs('2023-07-27T01:19'),
  acceptanceDate: dayjs('2023-07-26T17:05'),
};

export const sampleWithFullData: IFollow = {
  id: 41550,
  state: FollowState['REJECTED'],
  creationDate: dayjs('2023-07-26T12:55'),
  acceptanceDate: dayjs('2023-07-27T09:23'),
};

export const sampleWithNewData: NewFollow = {
  state: FollowState['REJECTED'],
  creationDate: dayjs('2023-07-26T16:54'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
