import { IExtendedUser, NewExtendedUser } from './extended-user.model';

export const sampleWithRequiredData: IExtendedUser = {};

export const sampleWithPartialData: IExtendedUser = {
  location: 'Cantabria XML',
  profession: 'Estratega circuit capacitor',
};

export const sampleWithFullData: IExtendedUser = {
  description: 'parsing',
  web: 'Inversor Futuro',
  location: 'Ergonómico calculating Checa',
  profession: 'Ensalada platforms Honduras',
};

export const sampleWithNewData: NewExtendedUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
