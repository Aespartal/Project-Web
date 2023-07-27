import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFollow, NewFollow } from '../follow.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFollow for edit and NewFollowFormGroupInput for create.
 */
type FollowFormGroupInput = IFollow | PartialWithRequiredKeyOf<NewFollow>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFollow | NewFollow> = Omit<T, 'creationDate' | 'acceptanceDate'> & {
  creationDate?: string | null;
  acceptanceDate?: string | null;
};

type FollowFormRawValue = FormValueOf<IFollow>;

type NewFollowFormRawValue = FormValueOf<NewFollow>;

type FollowFormDefaults = Pick<NewFollow, 'id' | 'creationDate' | 'acceptanceDate'>;

type FollowFormGroupContent = {
  id: FormControl<FollowFormRawValue['id'] | NewFollow['id']>;
  state: FormControl<FollowFormRawValue['state']>;
  creationDate: FormControl<FollowFormRawValue['creationDate']>;
  acceptanceDate: FormControl<FollowFormRawValue['acceptanceDate']>;
  follower: FormControl<FollowFormRawValue['follower']>;
  following: FormControl<FollowFormRawValue['following']>;
};

export type FollowFormGroup = FormGroup<FollowFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FollowFormService {
  createFollowFormGroup(follow: FollowFormGroupInput = { id: null }): FollowFormGroup {
    const followRawValue = this.convertFollowToFollowRawValue({
      ...this.getFormDefaults(),
      ...follow,
    });
    return new FormGroup<FollowFormGroupContent>({
      id: new FormControl(
        { value: followRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      state: new FormControl(followRawValue.state, {
        validators: [Validators.required],
      }),
      creationDate: new FormControl(followRawValue.creationDate, {
        validators: [Validators.required],
      }),
      acceptanceDate: new FormControl(followRawValue.acceptanceDate),
      follower: new FormControl(followRawValue.follower, {
        validators: [Validators.required],
      }),
      following: new FormControl(followRawValue.following, {
        validators: [Validators.required],
      }),
    });
  }

  getFollow(form: FollowFormGroup): IFollow | NewFollow {
    return this.convertFollowRawValueToFollow(form.getRawValue() as FollowFormRawValue | NewFollowFormRawValue);
  }

  resetForm(form: FollowFormGroup, follow: FollowFormGroupInput): void {
    const followRawValue = this.convertFollowToFollowRawValue({ ...this.getFormDefaults(), ...follow });
    form.reset(
      {
        ...followRawValue,
        id: { value: followRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FollowFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      acceptanceDate: currentTime,
    };
  }

  private convertFollowRawValueToFollow(rawFollow: FollowFormRawValue | NewFollowFormRawValue): IFollow | NewFollow {
    return {
      ...rawFollow,
      creationDate: dayjs(rawFollow.creationDate, DATE_TIME_FORMAT),
      acceptanceDate: dayjs(rawFollow.acceptanceDate, DATE_TIME_FORMAT),
    };
  }

  private convertFollowToFollowRawValue(
    follow: IFollow | (Partial<NewFollow> & FollowFormDefaults)
  ): FollowFormRawValue | PartialWithRequiredKeyOf<NewFollowFormRawValue> {
    return {
      ...follow,
      creationDate: follow.creationDate ? follow.creationDate.format(DATE_TIME_FORMAT) : undefined,
      acceptanceDate: follow.acceptanceDate ? follow.acceptanceDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
