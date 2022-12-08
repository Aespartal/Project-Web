import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ExtendedUserFormService, ExtendedUserFormGroup } from './extended-user-form.service';
import { IExtendedUser } from '../extended-user.model';
import { ExtendedUserService } from '../service/extended-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-extended-user-update',
  templateUrl: './extended-user-update.component.html',
})
export class ExtendedUserUpdateComponent implements OnInit {
  isSaving = false;
  extendedUser: IExtendedUser | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: ExtendedUserFormGroup = this.extendedUserFormService.createExtendedUserFormGroup();

  constructor(
    protected extendedUserService: ExtendedUserService,
    protected extendedUserFormService: ExtendedUserFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extendedUser }) => {
      this.extendedUser = extendedUser;
      if (extendedUser) {
        this.updateForm(extendedUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const extendedUser = this.extendedUserFormService.getExtendedUser(this.editForm);
    if (extendedUser.id !== null) {
      this.subscribeToSaveResponse(this.extendedUserService.update(extendedUser));
    } else {
      this.subscribeToSaveResponse(this.extendedUserService.create(extendedUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtendedUser>>): void {
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

  protected updateForm(extendedUser: IExtendedUser): void {
    this.extendedUser = extendedUser;
    this.extendedUserFormService.resetForm(this.editForm, extendedUser);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, extendedUser.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.extendedUser?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
