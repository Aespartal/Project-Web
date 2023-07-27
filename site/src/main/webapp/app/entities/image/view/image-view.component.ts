import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImage } from '../image.model';
import { CommentaryService } from 'app/entities/commentary/service/commentary.service';
import { ICommentary } from 'app/entities/commentary/commentary.model';
import { ImageService } from '../service/image.service';

@Component({
  selector: 'jhi-image-view',
  templateUrl: './image-view.component.html',
  styleUrls: ['./image-view.component.scss'],
})
export class ImageViewComponent implements OnInit {
  image: IImage | null = null;
  commentaries: ICommentary[] = [];

  url = this.imageService.resourceUrl.concat('/base64/');

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected commentaryService: CommentaryService,
    protected imageService: ImageService
    ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ image }) => {
      this.image = image;
      this.loadCommentariesByImageId();
    });
  }

  previousState(): void {
    window.history.back();
  }

  protected loadCommentariesByImageId(): void {
    this.commentaryService.query({ 'imageId.equals': this.image?.id })
    .subscribe((res) => {
      this.commentaries = res.body!;
    });
  }
}
