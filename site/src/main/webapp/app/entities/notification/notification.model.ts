import dayjs from 'dayjs/esm';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';

export interface INotification {
  id: number;
  message?: string | null;
  type?: NotificationType | null;
  creationDate?: dayjs.Dayjs | null;
  image?: Pick<IExtendedUser, 'id'> | null;
  commentary?: Pick<IExtendedUser, 'id'> | null;
  notifier?: Pick<IExtendedUser, 'id'> | null;
  notifying?: Pick<IExtendedUser, 'id'> | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
