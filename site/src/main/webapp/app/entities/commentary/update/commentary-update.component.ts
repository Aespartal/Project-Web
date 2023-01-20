import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CommentaryFormService, CommentaryFormGroup } from './commentary-form.service';
import { ICommentary } from '../commentary.model';
import { CommentaryService } from '../service/commentary.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';
import { IImage } from 'app/entities/image/image.model';
import { ImageService } from 'app/entities/image/service/image.service';

@Component({
  selector: 'jhi-commentary-update',
  templateUrl: './commentary-update.component.html',
})
export class CommentaryUpdateComponent implements OnInit {
  isSaving = false;
  commentary: ICommentary | null = null;

  extendedUsersSharedCollection: IExtendedUser[] = [];
  imagesSharedCollection: IImage[] = [];

  editForm: CommentaryFormGroup = this.commentaryFormService.createCommentaryFormGroup();

  constructor(
    protected commentaryService: CommentaryService,
    protected commentaryFormService: CommentaryFormService,
    protected extendedUserService: ExtendedUserService,
    protected imageService: ImageService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareExtendedUser = (o1: IExtendedUser | null, o2: IExtendedUser | null): boolean =>
    this.extendedUserService.compareExtendedUser(o1, o2);

  compareImage = (o1: IImage | null, o2: IImage | null): boolean => this.imageService.compareImage(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commentary }) => {
      this.commentary = commentary;
      if (commentary) {
        this.updateForm(commentary);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commentary = this.commentaryFormService.getCommentary(this.editForm);
    if (commentary.id !== null) {
      this.subscribeToSaveResponse(this.commentaryService.update(commentary));
    } else {
      this.subscribeToSaveResponse(this.commentaryService.create(commentary));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommentary>>): void {
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

  protected updateForm(commentary: ICommentary): void {
    this.commentary = commentary;
    this.commentaryFormService.resetForm(this.editForm, commentary);

    this.extendedUsersSharedCollection = this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(
      this.extendedUsersSharedCollection,
      commentary.extendedUser
    );
    this.imagesSharedCollection = this.imageService.addImageToCollectionIfMissing<IImage>(this.imagesSharedCollection, commentary.image);
  }

  protected loadRelationshipsOptions(): void {
    this.extendedUserService
      .query()
      .pipe(map((res: HttpResponse<IExtendedUser[]>) => res.body ?? []))
      .pipe(
        map((extendedUsers: IExtendedUser[]) =>
          this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(extendedUsers, this.commentary?.extendedUser)
        )
      )
      .subscribe((extendedUsers: IExtendedUser[]) => (this.extendedUsersSharedCollection = extendedUsers));

    this.imageService
      .query()
      .pipe(map((res: HttpResponse<IImage[]>) => res.body ?? []))
      .pipe(map((images: IImage[]) => this.imageService.addImageToCollectionIfMissing<IImage>(images, this.commentary?.image)))
      .subscribe((images: IImage[]) => (this.imagesSharedCollection = images));
  }
}
