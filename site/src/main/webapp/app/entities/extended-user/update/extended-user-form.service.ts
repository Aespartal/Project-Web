import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExtendedUser | NewExtendedUser> = Omit<T, 'birthDate'> & {
  birthDate?: string | null;
};

type ExtendedUserFormRawValue = FormValueOf<IExtendedUser>;

type NewExtendedUserFormRawValue = FormValueOf<NewExtendedUser>;

type ExtendedUserFormDefaults = Pick<NewExtendedUser, 'birthDate'>;

type ExtendedUserFormGroupContent = {
  id: FormControl<ExtendedUserFormRawValue['id']>;
  description: FormControl<ExtendedUserFormRawValue['description']>;
  location: FormControl<ExtendedUserFormRawValue['location']>;
  height: FormControl<ExtendedUserFormRawValue['height']>;
  weight: FormControl<ExtendedUserFormRawValue['weight']>;
  birthDate: FormControl<ExtendedUserFormRawValue['birthDate']>;
  user: FormControl<ExtendedUserFormRawValue['user']>;
};

export type ExtendedUserFormGroup = FormGroup<ExtendedUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExtendedUserFormService {
  createExtendedUserFormGroup(extendedUser: ExtendedUserFormGroupInput = { id: null }): ExtendedUserFormGroup {
    const extendedUserRawValue = this.convertExtendedUserToExtendedUserRawValue({
      ...this.getFormDefaults(),
      ...extendedUser,
    });
    return new FormGroup<ExtendedUserFormGroupContent>({
      id: new FormControl (extendedUserRawValue.id),
      description: new FormControl(extendedUserRawValue.description, {
        validators: [Validators.maxLength(3500)],
      }),
      location: new FormControl(extendedUserRawValue.location, {
        validators: [Validators.maxLength(50)],
      }),
      height: new FormControl(extendedUserRawValue.height, {
        validators: [Validators.required, Validators.min(0)],
      }),
      weight: new FormControl(extendedUserRawValue.weight, {
        validators: [Validators.required, Validators.min(0)],
      }),
      birthDate: new FormControl(extendedUserRawValue.birthDate, {
        validators: [Validators.required],
      }),
      user: new FormControl(extendedUserRawValue.user),
    });
  }

  getExtendedUser(form: ExtendedUserFormGroup): IExtendedUser | NewExtendedUser {
    return this.convertExtendedUserRawValueToExtendedUser(form.getRawValue() as ExtendedUserFormRawValue | NewExtendedUserFormRawValue);
  }

  resetForm(form: ExtendedUserFormGroup, extendedUser: ExtendedUserFormGroupInput): void {
    const extendedUserRawValue = this.convertExtendedUserToExtendedUserRawValue({ ...this.getFormDefaults(), ...extendedUser });
    form.reset(
      {
        ...extendedUserRawValue,
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExtendedUserFormDefaults {
    const currentTime = dayjs();

    return {
      birthDate: currentTime,
    };
  }

  private convertExtendedUserRawValueToExtendedUser(
    rawExtendedUser: ExtendedUserFormRawValue | NewExtendedUserFormRawValue
  ): IExtendedUser | NewExtendedUser {
    return {
      ...rawExtendedUser,
      birthDate: dayjs(rawExtendedUser.birthDate, DATE_TIME_FORMAT),
    };
  }

  private convertExtendedUserToExtendedUserRawValue(
    extendedUser: IExtendedUser | (Partial<NewExtendedUser> & ExtendedUserFormDefaults)
  ): ExtendedUserFormRawValue | PartialWithRequiredKeyOf<NewExtendedUserFormRawValue> {
    return {
      ...extendedUser,
      birthDate: extendedUser.birthDate ? extendedUser.birthDate.format(DATE_TIME_FORMAT) : null,
    };
  }
}
