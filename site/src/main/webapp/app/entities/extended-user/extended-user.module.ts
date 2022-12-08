import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExtendedUserComponent } from './list/extended-user.component';
import { ExtendedUserDetailComponent } from './detail/extended-user-detail.component';
import { ExtendedUserUpdateComponent } from './update/extended-user-update.component';
import { ExtendedUserDeleteDialogComponent } from './delete/extended-user-delete-dialog.component';
import { ExtendedUserRoutingModule } from './route/extended-user-routing.module';

@NgModule({
  imports: [SharedModule, ExtendedUserRoutingModule],
  declarations: [ExtendedUserComponent, ExtendedUserDetailComponent, ExtendedUserUpdateComponent, ExtendedUserDeleteDialogComponent],
})
export class ExtendedUserModule {}
