import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILikeCommentary } from '../like-commentary.model';
import { LikeCommentaryService } from '../service/like-commentary.service';

@Injectable({ providedIn: 'root' })
export class LikeCommentaryRoutingResolveService implements Resolve<ILikeCommentary | null> {
  constructor(protected service: LikeCommentaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILikeCommentary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((likeCommentary: HttpResponse<ILikeCommentary>) => {
          if (likeCommentary.body) {
            return of(likeCommentary.body);
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
