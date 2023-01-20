import dayjs from 'dayjs/esm';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { IImage } from 'app/entities/image/image.model';

export interface ICommentary {
  id: number;
  description?: string | null;
  creationDate?: dayjs.Dayjs | null;
  extendedUser?: Pick<IExtendedUser, 'id'> | null;
  image?: Pick<IImage, 'id'> | null;
}

export type NewCommentary = Omit<ICommentary, 'id'> & { id: null };
