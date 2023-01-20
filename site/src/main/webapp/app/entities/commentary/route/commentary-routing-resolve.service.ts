import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommentary } from '../commentary.model';
import { CommentaryService } from '../service/commentary.service';

@Injectable({ providedIn: 'root' })
export class CommentaryRoutingResolveService implements Resolve<ICommentary | null> {
  constructor(protected service: CommentaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommentary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((commentary: HttpResponse<ICommentary>) => {
          if (commentary.body) {
            return of(commentary.body);
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
