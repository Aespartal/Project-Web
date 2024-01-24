import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { Login } from './login.model';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(private accountService: AccountService, private authServerProvider: AuthServerProvider, private router: Router) {}

  login(credentials: Login): Observable<IExtendedUser | null> {
    return this.authServerProvider.login(credentials).pipe(mergeMap(() => this.accountService.identity(true)));
  }

  logout(navigate = true): void {
    this.authServerProvider.logout().subscribe({ complete: () => {
      this.accountService.authenticate(null);
      if (navigate) {
        this.router.navigate(['']);
      }
    } });
  }
}
