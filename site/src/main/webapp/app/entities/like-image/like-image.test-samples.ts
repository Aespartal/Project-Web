import dayjs from 'dayjs/esm';

import { ILikeImage, NewLikeImage } from './like-image.model';

export const sampleWithRequiredData: ILikeImage = {
  id: 41747,
  creationDate: dayjs('2023-01-14T00:39'),
};

export const sampleWithPartialData: ILikeImage = {
  id: 50694,
  creationDate: dayjs('2023-01-14T16:10'),
};

export const sampleWithFullData: ILikeImage = {
  id: 73247,
  creationDate: dayjs('2023-01-14T13:12'),
};

export const sampleWithNewData: NewLikeImage = {
  creationDate: dayjs('2023-01-14T08:04'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
