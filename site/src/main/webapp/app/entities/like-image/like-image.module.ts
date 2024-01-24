import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LikeImageComponent } from './list/like-image.component';
import { LikeImageDetailComponent } from './detail/like-image-detail.component';
import { LikeImageUpdateComponent } from './update/like-image-update.component';
import { LikeImageDeleteDialogComponent } from './delete/like-image-delete-dialog.component';
import { LikeImageRoutingModule } from './route/like-image-routing.module';

@NgModule({
  imports: [SharedModule, LikeImageRoutingModule],
  declarations: [LikeImageComponent, LikeImageDetailComponent, LikeImageUpdateComponent, LikeImageDeleteDialogComponent],
})
export class LikeImageModule {}
