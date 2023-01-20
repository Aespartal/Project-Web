import dayjs from 'dayjs/esm';
import { IImage } from 'app/entities/image/image.model';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';

export interface ILikeImage {
  id: number;
  creationDate?: dayjs.Dayjs | null;
  image?: Pick<IImage, 'id'> | null;
  extendedUser?: Pick<IExtendedUser, 'id'> | null;
}

export type NewLikeImage = Omit<ILikeImage, 'id'> & { id: null };
