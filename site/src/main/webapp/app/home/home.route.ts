import { Routes } from '@angular/router';

import { HomeComponent } from './home.component';
import { GalleryContentComponent } from './gallery/gallery-content.component';
import { NotificationContentComponent } from './notifications/notification-content.component';
import { ImageUploadComponent } from './image-upload/image-upload.component';

export const HOME_ROUTE: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      pageTitle: 'home.title',
    },
    children: [
      {
        path: '',
        component: GalleryContentComponent,
        data: {
          pageTitle: 'home.title',
        },
      },
      {
        path: 'notifications',
        component: NotificationContentComponent,
        data: {
          pageTitle: 'home.title',
        },
      },
      {
        path: 'image-upload',
        component: ImageUploadComponent,
        data: {
          pageTitle: 'home.title',
        },
      },
    ]
  }
];
