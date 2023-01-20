import dayjs from 'dayjs/esm';

import { ICommentary, NewCommentary } from './commentary.model';

export const sampleWithRequiredData: ICommentary = {
  id: 19061,
  description: 'transmitter',
  creationDate: dayjs('2023-01-14T02:15'),
};

export const sampleWithPartialData: ICommentary = {
  id: 47495,
  description: 'León y Analista',
  creationDate: dayjs('2023-01-14T14:26'),
};

export const sampleWithFullData: ICommentary = {
  id: 84318,
  description: 'payment initiatives',
  creationDate: dayjs('2023-01-14T08:44'),
};

export const sampleWithNewData: NewCommentary = {
  description: 'up Avon',
  creationDate: dayjs('2023-01-14T09:27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
