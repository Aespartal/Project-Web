import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LikeCommentaryComponent } from './list/like-commentary.component';
import { LikeCommentaryDetailComponent } from './detail/like-commentary-detail.component';
import { LikeCommentaryUpdateComponent } from './update/like-commentary-update.component';
import { LikeCommentaryDeleteDialogComponent } from './delete/like-commentary-delete-dialog.component';
import { LikeCommentaryRoutingModule } from './route/like-commentary-routing.module';

@NgModule({
  imports: [SharedModule, LikeCommentaryRoutingModule],
  declarations: [
    LikeCommentaryComponent,
    LikeCommentaryDetailComponent,
    LikeCommentaryUpdateComponent,
    LikeCommentaryDeleteDialogComponent,
  ],
})
export class LikeCommentaryModule {}
