import { Component, EventEmitter, Input, Output } from "@angular/core";
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

  url = this.imageService.resourceUrl.concat('/base64/');

  constructor(private imageService: ImageService) {

  }

  onLikeClick(): void {
    this.likeClicked.emit(this.image);
  }

}
