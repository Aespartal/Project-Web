import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LikeCommentaryComponent } from '../list/like-commentary.component';
import { LikeCommentaryDetailComponent } from '../detail/like-commentary-detail.component';
import { LikeCommentaryUpdateComponent } from '../update/like-commentary-update.component';
import { LikeCommentaryRoutingResolveService } from './like-commentary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const likeCommentaryRoute: Routes = [
  {
    path: '',
    component: LikeCommentaryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LikeCommentaryDetailComponent,
    resolve: {
      likeCommentary: LikeCommentaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LikeCommentaryUpdateComponent,
    resolve: {
      likeCommentary: LikeCommentaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LikeCommentaryUpdateComponent,
    resolve: {
      likeCommentary: LikeCommentaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(likeCommentaryRoute)],
  exports: [RouterModule],
})
export class LikeCommentaryRoutingModule {}
