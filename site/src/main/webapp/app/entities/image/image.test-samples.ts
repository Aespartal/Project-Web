import dayjs from 'dayjs/esm';

import { IImage, NewImage } from './image.model';

export const sampleWithRequiredData: IImage = {
  id: 41829,
  name: 'Pescado',
  description: 'Mobilidad acceso Pantalones',
  image: 'Ruanda Ergonómico',
  imageType: 'one-to-one TCP invoice',
  creationDate: dayjs('2023-01-13T17:19'),
  isPrivate: true,
};

export const sampleWithPartialData: IImage = {
  id: 17817,
  name: 'Bedfordshire',
  description: 'Salida',
  image: 'Humano Mascotas',
  imageType: 'vía Informática',
  creationDate: dayjs('2023-01-13T18:08'),
  isPrivate: false,
};

export const sampleWithFullData: IImage = {
  id: 47023,
  name: 'Baht Hormigon Pequeño',
  description: 'deliver',
  image: 'estrategia',
  imageType: 'Cataluña',
  creationDate: dayjs('2023-01-13T21:21'),
  modificationDate: dayjs('2023-01-14T05:12'),
  isPrivate: false,
};

export const sampleWithNewData: NewImage = {
  name: 'ROI Martinica state',
  description: 'Marroquinería quantifying XML',
  image: 'Ferrocarril',
  imageType: 'transmit Cuesta Marca',
  creationDate: dayjs('2023-01-14T10:56'),
  isPrivate: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
