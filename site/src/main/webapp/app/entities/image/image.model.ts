import dayjs from 'dayjs/esm';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';

export interface IImage {
  id: number;
  name?: string | null;
  description?: string | null;
  image?: string | null;
  imageType?: string | null;
  creationDate?: dayjs.Dayjs | null;
  modificationDate?: dayjs.Dayjs | null;
  isPrivate?: boolean | null;
  extendedUser?: Pick<IExtendedUser, 'id'> | null;
}

export type NewImage = Omit<IImage, 'id'> & { id: null };
