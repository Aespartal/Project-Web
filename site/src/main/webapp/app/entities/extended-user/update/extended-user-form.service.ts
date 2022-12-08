import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExtendedUser, NewExtendedUser } from '../extended-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExtendedUser for edit and NewExtendedUserFormGroupInput for create.
 */
type ExtendedUserFormGroupInput = IExtendedUser | PartialWithRequiredKeyOf<NewExtendedUser>;

type ExtendedUserFormGroupContent = {
  description: FormControl<IExtendedUser['description']>;
  web: FormControl<IExtendedUser['web']>;
  location: FormControl<IExtendedUser['location']>;
  profession: FormControl<IExtendedUser['profession']>;
  user: FormControl<IExtendedUser['user']>;
};

export type ExtendedUserFormGroup = FormGroup<ExtendedUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExtendedUserFormService {
  createExtendedUserFormGroup(extendedUser: ExtendedUserFormGroupInput = { id: null }): ExtendedUserFormGroup {
    const extendedUserRawValue = extendedUser;
    return new FormGroup<ExtendedUserFormGroupContent>({
      description: new FormControl(extendedUserRawValue.description, {
        validators: [Validators.maxLength(3500)],
      }),
      web: new FormControl(extendedUserRawValue.web, {
        validators: [Validators.maxLength(100)],
      }),
      location: new FormControl(extendedUserRawValue.location, {
        validators: [Validators.maxLength(50)],
      }),
      profession: new FormControl(extendedUserRawValue.profession, {
        validators: [Validators.maxLength(50)],
      }),
      user: new FormControl(extendedUserRawValue.user),
    });
  }

  getExtendedUser(form: ExtendedUserFormGroup): IExtendedUser | NewExtendedUser {
    return form.getRawValue() as IExtendedUser | NewExtendedUser;
  }

  resetForm(form: ExtendedUserFormGroup, extendedUser: ExtendedUserFormGroupInput): void {
    const extendedUserRawValue = extendedUser;
    form.reset(
      {
        ...extendedUserRawValue,
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }
}
