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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
