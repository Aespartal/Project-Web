import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LikeImageComponent } from '../list/like-image.component';
import { LikeImageDetailComponent } from '../detail/like-image-detail.component';
import { LikeImageUpdateComponent } from '../update/like-image-update.component';
import { LikeImageRoutingResolveService } from './like-image-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const likeImageRoute: Routes = [
  {
    path: '',
    component: LikeImageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LikeImageDetailComponent,
    resolve: {
      likeImage: LikeImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LikeImageUpdateComponent,
    resolve: {
      likeImage: LikeImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LikeImageUpdateComponent,
    resolve: {
      likeImage: LikeImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(likeImageRoute)],
  exports: [RouterModule],
})
export class LikeImageRoutingModule {}
