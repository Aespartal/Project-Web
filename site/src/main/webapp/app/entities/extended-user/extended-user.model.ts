import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IExtendedUser {
  id?: number | null;
  description?: string | null;
  location?: string | null;
  height?: number | null;
  weight?: number | null;
  birthDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  userLogin? : string | null;
  totalFollowers?: number | null;
  totalFollowing?: number | null;
  totalImages?: number | null;
  totalNotifications?: number | null;
}

export type NewExtendedUser = Omit<IExtendedUser, 'id'> & { id: null };
