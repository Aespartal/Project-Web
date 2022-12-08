import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 55962,
  name: 'COM dedicada Agente',
  description: 'Genérico users',
  link: 'Comunicaciones Innovador dot-com',
  order: 50253,
  creationDate: dayjs('2022-12-08T15:05'),
  isPrivate: false,
  active: false,
};

export const sampleWithPartialData: IProject = {
  id: 93107,
  name: 'state',
  description: 'Loan Avon',
  link: 'escalable users invoice',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  order: 81146,
  creationDate: dayjs('2022-12-08T05:54'),
  isPrivate: false,
  active: true,
};

export const sampleWithFullData: IProject = {
  id: 20212,
  name: 'Bicicleta Tanzania',
  description: 'Moda deposit productize',
  link: 'state USB medición',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  order: 34684,
  creationDate: dayjs('2022-12-08T04:37'),
  isPrivate: false,
  active: false,
};

export const sampleWithNewData: NewProject = {
  name: 'Berkshire global',
  description: 'synthesize',
  link: 'EXE solutions',
  order: 46732,
  creationDate: dayjs('2022-12-07T16:31'),
  isPrivate: true,
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
