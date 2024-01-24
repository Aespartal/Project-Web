import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'extended-user',
        data: { pageTitle: 'projectApp.extendedUser.home.title' },
        loadChildren: () => import('./extended-user/extended-user.module').then(m => m.ExtendedUserModule),
      },
      {
        path: 'project',
        data: { pageTitle: 'projectApp.project.home.title' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      {
        path: 'image',
        data: { pageTitle: 'projectApp.image.home.title' },
        loadChildren: () => import('./image/image.module').then(m => m.ImageModule),
      },
      {
        path: 'like-image',
        data: { pageTitle: 'projectApp.likeImage.home.title' },
        loadChildren: () => import('./like-image/like-image.module').then(m => m.LikeImageModule),
      },
      {
        path: 'commentary',
        data: { pageTitle: 'projectApp.commentary.home.title' },
        loadChildren: () => import('./commentary/commentary.module').then(m => m.CommentaryModule),
      },
      {
        path: 'like-commentary',
        data: { pageTitle: 'projectApp.likeCommentary.home.title' },
        loadChildren: () => import('./like-commentary/like-commentary.module').then(m => m.LikeCommentaryModule),
      },
      {
        path: 'follow',
        data: { pageTitle: 'projectApp.follow.home.title' },
        loadChildren: () => import('./follow/follow.module').then(m => m.FollowModule),
      },
      {
        path: 'notification',
        data: { pageTitle: 'projectApp.notification.home.title' },
        loadChildren: () => import('./notification/notification.module').then(m => m.NotificationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
