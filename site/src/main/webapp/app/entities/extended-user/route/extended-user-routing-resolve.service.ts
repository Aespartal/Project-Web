import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExtendedUser } from '../extended-user.model';
import { ExtendedUserService } from '../service/extended-user.service';

@Injectable({ providedIn: 'root' })
export class ExtendedUserRoutingResolveService implements Resolve<IExtendedUser | null> {
  constructor(protected service: ExtendedUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExtendedUser | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((extendedUser: HttpResponse<IExtendedUser>) => {
          if (extendedUser.body) {
            return of(extendedUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
