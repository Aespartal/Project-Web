import { Component, Input, OnInit } from "@angular/core";
import { IExtendedUser } from "app/entities/extended-user/extended-user.model";
import { NotificationFilter } from "../notifications/notification.filter";
import { INotification } from "app/entities/notification/notification.model";
import { IMessage, RxStomp } from "@stomp/rx-stomp";

@Component({
  selector: 'jhi-account-content',
  templateUrl: './account-content.component.html',
  styleUrls: ['./account-content.component.scss'],
})
export class AccountContentComponent implements OnInit {

  @Input() extendedUser!: IExtendedUser;

  notifications = 0;

  predicate!: string;
  ascending!: boolean;

  filters: NotificationFilter = new NotificationFilter();

  constructor(
    private rxStompService: RxStomp,
    ) {
  }
  ngOnInit(): void {
    this.filters.notifyingId = this.extendedUser.id!;
    this.notifications = this.extendedUser.totalNotifications ?? 0;
    this.connect();
  }

  disconnect(): void {
    this.rxStompService.deactivate();
  }

  connect(): void {
    this.rxStompService.watch('/topic/notifications').subscribe({
      next: (message: IMessage) => {
        const notification = JSON.parse(message.body) as INotification | null;
        if (notification && notification.notifying!.id === this.extendedUser.id) {
          this.notifications++;
        }
      }
    });
  }

}
