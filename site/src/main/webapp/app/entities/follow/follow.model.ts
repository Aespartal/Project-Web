import dayjs from 'dayjs/esm';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { FollowState } from 'app/entities/enumerations/follow-state.model';

export interface IFollow {
  id: number;
  state?: FollowState | null;
  creationDate?: dayjs.Dayjs | null;
  acceptanceDate?: dayjs.Dayjs | null;
  follower?: Pick<IExtendedUser, 'id'> | null;
  following?: Pick<IExtendedUser, 'id'> | null;
}

export type NewFollow = Omit<IFollow, 'id'> & { id: null };
