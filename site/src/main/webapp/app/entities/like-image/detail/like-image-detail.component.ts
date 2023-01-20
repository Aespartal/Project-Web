import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILikeImage } from '../like-image.model';

@Component({
  selector: 'jhi-like-image-detail',
  templateUrl: './like-image-detail.component.html',
})
export class LikeImageDetailComponent implements OnInit {
  likeImage: ILikeImage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ likeImage }) => {
      this.likeImage = likeImage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
