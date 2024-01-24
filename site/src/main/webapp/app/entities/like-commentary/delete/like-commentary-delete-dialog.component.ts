import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILikeCommentary } from '../like-commentary.model';
import { LikeCommentaryService } from '../service/like-commentary.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './like-commentary-delete-dialog.component.html',
})
export class LikeCommentaryDeleteDialogComponent {
  likeCommentary?: ILikeCommentary;

  constructor(protected likeCommentaryService: LikeCommentaryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.likeCommentaryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
