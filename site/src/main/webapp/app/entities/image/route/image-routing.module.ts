import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImageComponent } from '../list/image.component';
import { ImageDetailComponent } from '../detail/image-detail.component';
import { ImageUpdateComponent } from '../update/image-update.component';
import { ImageRoutingResolveService } from './image-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { ImageViewComponent } from '../view/image-view.component';

const imageRoute: Routes = [
  {
    path: '',
    component: ImageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImageDetailComponent,
    resolve: {
      image: ImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImageUpdateComponent,
    resolve: {
      image: ImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImageUpdateComponent,
    resolve: {
      image: ImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/photo-view',
    component: ImageViewComponent,
    resolve: {
      image: ImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imageRoute)],
  exports: [RouterModule],
})
export class ImageRoutingModule {}
