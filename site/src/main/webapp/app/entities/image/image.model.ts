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
  totalLikes?: number;
  totalCommentaries?: number;
  favourited?: boolean;
  extendedUser?: Pick<IExtendedUser, 'id' |  'userLogin' | 'user'> | null;
  order?: number | null;
}

export type NewImage = Omit<IImage, 'id'> & { id: null };
