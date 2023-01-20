import dayjs from 'dayjs/esm';

import { ILikeCommentary, NewLikeCommentary } from './like-commentary.model';

export const sampleWithRequiredData: ILikeCommentary = {
  id: 96376,
  creationDate: dayjs('2023-01-14T14:54'),
};

export const sampleWithPartialData: ILikeCommentary = {
  id: 7937,
  creationDate: dayjs('2023-01-13T21:50'),
};

export const sampleWithFullData: ILikeCommentary = {
  id: 27389,
  creationDate: dayjs('2023-01-14T01:42'),
};

export const sampleWithNewData: NewLikeCommentary = {
  creationDate: dayjs('2023-01-14T00:29'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
