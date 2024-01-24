import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { IExtendedUser } from "app/entities/extended-user/extended-user.model";
import { NotificationFilter } from "../notifications/notification.filter";
import { RxStomp } from "@stomp/rx-stomp";
import { SidebarService } from "app/shared/services/sidebar.service";
import { LoginService } from "app/login/login.service";
import { Router } from "@angular/router";
import { NotificationService } from "app/entities/notification/service/notification.service";

@Component({
  selector: 'jhi-account-content',
  templateUrl: './account-content.component.html',
  styleUrls: ['./account-content.component.scss'],
})
export class AccountContentComponent implements OnInit, OnDestroy {

  @Input() extendedUser!: IExtendedUser | null;

  notifications = 0;

  predicate!: string;
  ascending!: boolean;

  filters: NotificationFilter = new NotificationFilter();

  sidebarOpen$ = this.sidebarService.sidebarOpen$;

  constructor(
    private rxStompService: RxStomp,
    private sidebarService: SidebarService,
    private loginService: LoginService,
    private router: Router,
    private notificationService: NotificationService
    ) {
  }

  ngOnInit(): void {
    if (!this.extendedUser) {
      return;
    }
    this.filters.notifyingId = this.extendedUser.id!;
    this.getNotificationsCount();
    this.connect();
  }

  ngOnDestroy(): void {
    this.disconnect();
  }

  disconnect(): void {
    this.rxStompService.deactivate();
  }

  connect(): void {
    this.rxStompService.watch('/topic/notifications').subscribe({
      next: () => {
        this.getNotificationsCount();
      }
    });
  }

  getNotificationsCount(): void {
    this.notificationService.count({
      filter: this.filters.toMap()
    }).subscribe({
      next: (res) => {
        this.notifications = res.body ?? 0;
      }
    })
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

}
