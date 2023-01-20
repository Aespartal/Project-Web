import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommentary } from '../commentary.model';

@Component({
  selector: 'jhi-commentary-detail',
  templateUrl: './commentary-detail.component.html',
})
export class CommentaryDetailComponent implements OnInit {
  commentary: ICommentary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commentary }) => {
      this.commentary = commentary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
