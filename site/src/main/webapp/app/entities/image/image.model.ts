import dayjs from 'dayjs/esm';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';

export interface IImage {
  id: number;
  title?: string | null;
  description?: string | null;
  fileName?: string | null;
  path?: string | null;
  creationDate?: dayjs.Dayjs | null;
  modificationDate?: dayjs.Dayjs | null;
  isPrivate?: boolean | null;
  likes?: number;
  favourited?: boolean;
  extendedUser?: Pick<IExtendedUser, 'id'> | null;
}

export type NewImage = Omit<IImage, 'id'> & { id: null };
