import dayjs from 'dayjs/esm';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';

export interface IProject {
  id: number;
  name?: string | null;
  description?: string | null;
  link?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  order?: number | null;
  creationDate?: dayjs.Dayjs | null;
  isPrivate?: boolean | null;
  active?: boolean | null;
  extendedUser?: Pick<IExtendedUser, 'id'> | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
