import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILikeImage } from '../like-image.model';
import { LikeImageService } from '../service/like-image.service';

@Injectable({ providedIn: 'root' })
export class LikeImageRoutingResolveService implements Resolve<ILikeImage | null> {
  constructor(protected service: LikeImageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILikeImage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((likeImage: HttpResponse<ILikeImage>) => {
          if (likeImage.body) {
            return of(likeImage.body);
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
