import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILikeCommentary } from '../like-commentary.model';

@Component({
  selector: 'jhi-like-commentary-detail',
  templateUrl: './like-commentary-detail.component.html',
})
export class LikeCommentaryDetailComponent implements OnInit {
  likeCommentary: ILikeCommentary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ likeCommentary }) => {
      this.likeCommentary = likeCommentary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
