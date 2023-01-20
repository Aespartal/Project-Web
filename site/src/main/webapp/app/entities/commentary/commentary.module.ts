import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommentaryComponent } from './list/commentary.component';
import { CommentaryDetailComponent } from './detail/commentary-detail.component';
import { CommentaryUpdateComponent } from './update/commentary-update.component';
import { CommentaryDeleteDialogComponent } from './delete/commentary-delete-dialog.component';
import { CommentaryRoutingModule } from './route/commentary-routing.module';

@NgModule({
  imports: [SharedModule, CommentaryRoutingModule],
  declarations: [CommentaryComponent, CommentaryDetailComponent, CommentaryUpdateComponent, CommentaryDeleteDialogComponent],
})
export class CommentaryModule {}
