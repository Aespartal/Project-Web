import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImage } from '../image.model';

@Component({
  selector: 'jhi-image-detail',
  templateUrl: './image-detail.component.html',
})
export class ImageDetailComponent implements OnInit {
  image: IImage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ image }) => {
      this.image = image;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
