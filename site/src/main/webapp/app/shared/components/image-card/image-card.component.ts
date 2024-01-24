import { Component, EventEmitter, Input, Output, ViewChild } from "@angular/core";
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
  @Output() imageClicked = new EventEmitter<IImage>();

  url = this.imageService.resourceUrl.concat('/base64/');

  constructor(
    private imageService: ImageService,
    private router: Router
  )
  {

  }

  onLikeClick(): void {
    this.onLikeClickAnimation();
    this.likeClicked.emit(this.image);
  }

  onImageClickedAnimation(): void {
    this.onLikeClickAnimation();
    this.likeClicked.emit(this.image);
  }

  onLikeClickAnimation(): void {
    const svgElement = document.getElementById(`Layer_${this.image.id}`);
    svgElement!.classList.add('like');
    setTimeout(() => {
      svgElement!.classList.remove('like');
    }, 1000);
  }

  onCommentClick(): void {
    this.commentClicked.emit(this.image);
  }

  onImageClicked(): void {
    this.imageClicked.emit(this.image);
  }

}
