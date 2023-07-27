import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FollowFormService, FollowFormGroup } from './follow-form.service';
import { IFollow } from '../follow.model';
import { FollowService } from '../service/follow.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';
import { FollowState } from 'app/entities/enumerations/follow-state.model';

@Component({
  selector: 'jhi-follow-update',
  templateUrl: './follow-update.component.html',
})
export class FollowUpdateComponent implements OnInit {
  isSaving = false;
  follow: IFollow | null = null;
  followStateValues = Object.keys(FollowState);

  extendedUsersSharedCollection: IExtendedUser[] = [];

  editForm: FollowFormGroup = this.followFormService.createFollowFormGroup();

  constructor(
    protected followService: FollowService,
    protected followFormService: FollowFormService,
    protected extendedUserService: ExtendedUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareExtendedUser = (o1: IExtendedUser | null, o2: IExtendedUser | null): boolean =>
    this.extendedUserService.compareExtendedUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ follow }) => {
      this.follow = follow;
      if (follow) {
        this.updateForm(follow);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const follow = this.followFormService.getFollow(this.editForm);
    if (follow.id !== null) {
      this.subscribeToSaveResponse(this.followService.update(follow));
    } else {
      this.subscribeToSaveResponse(this.followService.create(follow));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFollow>>): void {
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

  protected updateForm(follow: IFollow): void {
    this.follow = follow;
    this.followFormService.resetForm(this.editForm, follow);

    this.extendedUsersSharedCollection = this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(
      this.extendedUsersSharedCollection,
      follow.follower,
      follow.following
    );
  }

  protected loadRelationshipsOptions(): void {
    this.extendedUserService
      .query()
      .pipe(map((res: HttpResponse<IExtendedUser[]>) => res.body ?? []))
      .pipe(
        map((extendedUsers: IExtendedUser[]) =>
          this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(
            extendedUsers,
            this.follow?.follower,
            this.follow?.following
          )
        )
      )
      .subscribe((extendedUsers: IExtendedUser[]) => (this.extendedUsersSharedCollection = extendedUsers));
  }
}
