import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExtendedUserComponent } from '../list/extended-user.component';
import { ExtendedUserDetailComponent } from '../detail/extended-user-detail.component';
import { ExtendedUserUpdateComponent } from '../update/extended-user-update.component';
import { ExtendedUserRoutingResolveService } from './extended-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const extendedUserRoute: Routes = [
  {
    path: '',
    component: ExtendedUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExtendedUserDetailComponent,
    resolve: {
      extendedUser: ExtendedUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExtendedUserUpdateComponent,
    resolve: {
      extendedUser: ExtendedUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExtendedUserUpdateComponent,
    resolve: {
      extendedUser: ExtendedUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(extendedUserRoute)],
  exports: [RouterModule],
})
export class ExtendedUserRoutingModule {}
