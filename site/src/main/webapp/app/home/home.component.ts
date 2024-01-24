import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { SidebarService } from 'app/shared/services/sidebar.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  extendedUser: IExtendedUser | null = null;
  sidebarOpen$ = this.sidebarService.sidebarOpen$;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private sidebarService: SidebarService
    ) {
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.extendedUser = account));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

}
