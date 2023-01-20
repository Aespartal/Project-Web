import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LikeImageFormService, LikeImageFormGroup } from './like-image-form.service';
import { ILikeImage } from '../like-image.model';
import { LikeImageService } from '../service/like-image.service';
import { IImage } from 'app/entities/image/image.model';
import { ImageService } from 'app/entities/image/service/image.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';

@Component({
  selector: 'jhi-like-image-update',
  templateUrl: './like-image-update.component.html',
})
export class LikeImageUpdateComponent implements OnInit {
  isSaving = false;
  likeImage: ILikeImage | null = null;

  imagesSharedCollection: IImage[] = [];
  extendedUsersSharedCollection: IExtendedUser[] = [];

  editForm: LikeImageFormGroup = this.likeImageFormService.createLikeImageFormGroup();

  constructor(
    protected likeImageService: LikeImageService,
    protected likeImageFormService: LikeImageFormService,
    protected imageService: ImageService,
    protected extendedUserService: ExtendedUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareImage = (o1: IImage | null, o2: IImage | null): boolean => this.imageService.compareImage(o1, o2);

  compareExtendedUser = (o1: IExtendedUser | null, o2: IExtendedUser | null): boolean =>
    this.extendedUserService.compareExtendedUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ likeImage }) => {
      this.likeImage = likeImage;
      if (likeImage) {
        this.updateForm(likeImage);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const likeImage = this.likeImageFormService.getLikeImage(this.editForm);
    if (likeImage.id !== null) {
      this.subscribeToSaveResponse(this.likeImageService.update(likeImage));
    } else {
      this.subscribeToSaveResponse(this.likeImageService.create(likeImage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILikeImage>>): void {
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

  protected updateForm(likeImage: ILikeImage): void {
    this.likeImage = likeImage;
    this.likeImageFormService.resetForm(this.editForm, likeImage);

    this.imagesSharedCollection = this.imageService.addImageToCollectionIfMissing<IImage>(this.imagesSharedCollection, likeImage.image);
    this.extendedUsersSharedCollection = this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(
      this.extendedUsersSharedCollection,
      likeImage.extendedUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.imageService
      .query()
      .pipe(map((res: HttpResponse<IImage[]>) => res.body ?? []))
      .pipe(map((images: IImage[]) => this.imageService.addImageToCollectionIfMissing<IImage>(images, this.likeImage?.image)))
      .subscribe((images: IImage[]) => (this.imagesSharedCollection = images));

    this.extendedUserService
      .query()
      .pipe(map((res: HttpResponse<IExtendedUser[]>) => res.body ?? []))
      .pipe(
        map((extendedUsers: IExtendedUser[]) =>
          this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(extendedUsers, this.likeImage?.extendedUser)
        )
      )
      .subscribe((extendedUsers: IExtendedUser[]) => (this.extendedUsersSharedCollection = extendedUsers));
  }
}
