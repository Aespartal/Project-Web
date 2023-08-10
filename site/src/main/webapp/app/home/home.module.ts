import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { AccountContentComponent } from './account/account-content.component';
import { GalleryContentComponent } from './gallery/gallery-content.component';
import { NotificationContentComponent } from './notifications/notification-content.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(HOME_ROUTE)],
  declarations: [HomeComponent, AccountContentComponent, GalleryContentComponent, NotificationContentComponent],
})
export class HomeModule {}
