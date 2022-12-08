import { IUser } from 'app/entities/user/user.model';

export interface IExtendedUser {
  description?: string | null;
  web?: string | null;
  location?: string | null;
  profession?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewExtendedUser = Omit<IExtendedUser, 'id'> & { id: null };
