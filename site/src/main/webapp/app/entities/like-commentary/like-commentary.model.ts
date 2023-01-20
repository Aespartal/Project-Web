import dayjs from 'dayjs/esm';
import { ICommentary } from 'app/entities/commentary/commentary.model';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';

export interface ILikeCommentary {
  id: number;
  creationDate?: dayjs.Dayjs | null;
  commentary?: Pick<ICommentary, 'id'> | null;
  extendedUser?: Pick<IExtendedUser, 'id'> | null;
}

export type NewLikeCommentary = Omit<ILikeCommentary, 'id'> & { id: null };
