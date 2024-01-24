import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILikeImage } from '../like-image.model';
import { LikeImageService } from '../service/like-image.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './like-image-delete-dialog.component.html',
})
export class LikeImageDeleteDialogComponent {
  likeImage?: ILikeImage;

  constructor(protected likeImageService: LikeImageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.likeImageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
