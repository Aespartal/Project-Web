import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LikeCommentaryFormService, LikeCommentaryFormGroup } from './like-commentary-form.service';
import { ILikeCommentary } from '../like-commentary.model';
import { LikeCommentaryService } from '../service/like-commentary.service';
import { ICommentary } from 'app/entities/commentary/commentary.model';
import { CommentaryService } from 'app/entities/commentary/service/commentary.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';

@Component({
  selector: 'jhi-like-commentary-update',
  templateUrl: './like-commentary-update.component.html',
})
export class LikeCommentaryUpdateComponent implements OnInit {
  isSaving = false;
  likeCommentary: ILikeCommentary | null = null;

  commentariesSharedCollection: ICommentary[] = [];
  extendedUsersSharedCollection: IExtendedUser[] = [];

  editForm: LikeCommentaryFormGroup = this.likeCommentaryFormService.createLikeCommentaryFormGroup();

  constructor(
    protected likeCommentaryService: LikeCommentaryService,
    protected likeCommentaryFormService: LikeCommentaryFormService,
    protected commentaryService: CommentaryService,
    protected extendedUserService: ExtendedUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCommentary = (o1: ICommentary | null, o2: ICommentary | null): boolean => this.commentaryService.compareCommentary(o1, o2);

  compareExtendedUser = (o1: IExtendedUser | null, o2: IExtendedUser | null): boolean =>
    this.extendedUserService.compareExtendedUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ likeCommentary }) => {
      this.likeCommentary = likeCommentary;
      if (likeCommentary) {
        this.updateForm(likeCommentary);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const likeCommentary = this.likeCommentaryFormService.getLikeCommentary(this.editForm);
    if (likeCommentary.id !== null) {
      this.subscribeToSaveResponse(this.likeCommentaryService.update(likeCommentary));
    } else {
      this.subscribeToSaveResponse(this.likeCommentaryService.create(likeCommentary));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILikeCommentary>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(likeCommentary: ILikeCommentary): void {
    this.likeCommentary = likeCommentary;
    this.likeCommentaryFormService.resetForm(this.editForm, likeCommentary);

    this.commentariesSharedCollection = this.commentaryService.addCommentaryToCollectionIfMissing<ICommentary>(
      this.commentariesSharedCollection,
      likeCommentary.commentary
    );
    this.extendedUsersSharedCollection = this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(
      this.extendedUsersSharedCollection,
      likeCommentary.extendedUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.commentaryService
      .query()
      .pipe(map((res: HttpResponse<ICommentary[]>) => res.body ?? []))
      .pipe(
        map((commentaries: ICommentary[]) =>
          this.commentaryService.addCommentaryToCollectionIfMissing<ICommentary>(commentaries, this.likeCommentary?.commentary)
        )
      )
      .subscribe((commentaries: ICommentary[]) => (this.commentariesSharedCollection = commentaries));

    this.extendedUserService
      .query()
      .pipe(map((res: HttpResponse<IExtendedUser[]>) => res.body ?? []))
      .pipe(
        map((extendedUsers: IExtendedUser[]) =>
          this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(extendedUsers, this.likeCommentary?.extendedUser)
        )
      )
      .subscribe((extendedUsers: IExtendedUser[]) => (this.extendedUsersSharedCollection = extendedUsers));
  }
}
