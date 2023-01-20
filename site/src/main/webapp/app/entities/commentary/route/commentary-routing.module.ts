import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommentaryComponent } from '../list/commentary.component';
import { CommentaryDetailComponent } from '../detail/commentary-detail.component';
import { CommentaryUpdateComponent } from '../update/commentary-update.component';
import { CommentaryRoutingResolveService } from './commentary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const commentaryRoute: Routes = [
  {
    path: '',
    component: CommentaryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommentaryDetailComponent,
    resolve: {
      commentary: CommentaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommentaryUpdateComponent,
    resolve: {
      commentary: CommentaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommentaryUpdateComponent,
    resolve: {
      commentary: CommentaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commentaryRoute)],
  exports: [RouterModule],
})
export class CommentaryRoutingModule {}
