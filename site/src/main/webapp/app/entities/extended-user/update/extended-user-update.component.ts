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

import { LANGUAGES } from 'app/config/language.constants';
import { FormGroup } from '@angular/forms';
import { UserManagementService } from 'app/admin/user-management/service/user-management.service';

@Component({
  selector: 'jhi-extended-user-update',
  templateUrl: './extended-user-update.component.html',
})
export class ExtendedUserUpdateComponent implements OnInit {
  languages = LANGUAGES;
  authorities: string[] = [];
  isSaving = false;
  extendedUser: IExtendedUser | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: ExtendedUserFormGroup = this.extendedUserFormService.createExtendedUserFormGroup();

  // obtener de editForm el formGroup de user
  get userFormGroup(): FormGroup {
    return this.editForm.get('user') as FormGroup;
  }

  constructor(
    protected extendedUserService: ExtendedUserService,
    protected extendedUserFormService: ExtendedUserFormService,
    protected userService: UserService,
    protected userManagementService: UserManagementService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extendedUser }) => {
      this.extendedUser = extendedUser;
      if (extendedUser) {
        this.updateForm(extendedUser);
      }
    });
    this.userManagementService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const extendedUser = this.extendedUserFormService.getExtendedUser(this.editForm);
    console.debug(extendedUser);
    // if (extendedUser.id !== null) {
    //   this.subscribeToSaveResponse(this.extendedUserService.update(extendedUser));
    // } else {
    //   this.subscribeToSaveResponse(this.extendedUserService.create(extendedUser));
    // }
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
  }
}
