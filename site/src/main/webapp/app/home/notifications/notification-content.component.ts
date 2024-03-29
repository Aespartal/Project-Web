import { Component, OnInit } from "@angular/core";
import { AccountService } from "app/core/auth/account.service";
import { IExtendedUser } from "app/entities/extended-user/extended-user.model";
import { INotification } from "app/entities/notification/notification.model";
import { NotificationService } from "app/entities/notification/service/notification.service";
import { NotificationFilter } from "./notification.filter";
import { ImageService } from "app/entities/image/service/image.service";
import { SidebarService } from "app/shared/services/sidebar.service";
import { FollowService } from "app/entities/follow/service/follow.service";
import { NewFollow } from "app/entities/follow/follow.model";
import { FollowState } from "app/entities/enumerations/follow-state.model";
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-notification-content',
  templateUrl: './notification-content.component.html',
  styleUrls: ['./notification-content.component.scss'],
})
export class NotificationContentComponent implements OnInit {

  extendedUser: IExtendedUser | null = null;

  notifications: INotification[] = [];

  predicate!: string;
  ascending!: boolean;

  filters: NotificationFilter = new NotificationFilter();

  url = this.imageService.resourceUrl.concat('/base64/');

  constructor(
    private accountService: AccountService,
    private notificationService: NotificationService,
    private imageService: ImageService,
    private sidebarService: SidebarService,
    private followService: FollowService
    ) {
    this.predicate = 'creationDate';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .subscribe(account => {
        this.extendedUser = account!
        this.filters.notifyingId = this.extendedUser.id!;
        this.getNotifications();
      });
  }

  getNotifications(): void {
    const queryObject = {
      page: -1,
      size: 20,
      sort: this.sort(),
      filter: this.filters.toMap(),
    };
    this.notificationService.query(queryObject).subscribe((res) => {
      if (!res.body) {
        return;
      }
      this.notifications = res.body;
    })
  }

  onAcceptClick(notification: INotification): void {;
    const newFollow: NewFollow = {
      id: null,
      follower: notification.notifier,
      following: notification.notifying,
      state: FollowState.ACCEPTED,
      acceptanceDate: dayjs(),
      creationDate: dayjs(),
    };
    this.followService.create(newFollow).subscribe(() => {
      this.notificationService.delete(notification.id).subscribe(() => {
        this.getNotifications();
      });
    });
  }

  onRejectClick(notification: INotification): void {
    const newFollow: NewFollow = {
      id: null,
      follower: notification.notifier,
      following: notification.notifying,
      state: FollowState.REJECTED,
      creationDate: dayjs(),
    };

    this.followService.create(newFollow).subscribe(() => {
      this.notificationService.delete(notification.id).subscribe(() => {
        this.getNotifications();
      });
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'creationDate') {
      result.push('creationDate');
    }
    return result;
  }
}
