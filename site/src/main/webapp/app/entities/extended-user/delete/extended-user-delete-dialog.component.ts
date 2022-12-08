import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExtendedUser } from '../extended-user.model';
import { ExtendedUserService } from '../service/extended-user.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './extended-user-delete-dialog.component.html',
})
export class ExtendedUserDeleteDialogComponent {
  extendedUser?: IExtendedUser;

  constructor(protected extendedUserService: ExtendedUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.extendedUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
