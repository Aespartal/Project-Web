import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Router } from "@angular/router";
import { IImage } from "app/entities/image/image.model";
import { ImageService } from "app/entities/image/service/image.service";


@Component({
  selector: 'jhi-card',
  templateUrl: './image-card.component.html',
  styleUrls: ['./image-card.component.scss'],
})
export class ImageCardComponent {
  @Input() image!: IImage;
  @Output() likeClicked = new EventEmitter<IImage>();
  @Output() commentClicked = new EventEmitter<IImage>();


  url = this.imageService.resourceUrl.concat('/base64/');

  constructor(
    private imageService: ImageService,
    private router: Router
  )
  {}

  onLikeClick(): void {
    this.likeClicked.emit(this.image);
  }

  onCommentClick(): void {
    this.commentClicked.emit(this.image);
  }

  navigateToView(): void {
    this.router.navigate(['/', 'image', this.image.id, 'photo-view']);
  }

}
