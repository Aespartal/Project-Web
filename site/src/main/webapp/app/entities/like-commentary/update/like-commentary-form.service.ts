import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILikeCommentary, NewLikeCommentary } from '../like-commentary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILikeCommentary for edit and NewLikeCommentaryFormGroupInput for create.
 */
type LikeCommentaryFormGroupInput = ILikeCommentary | PartialWithRequiredKeyOf<NewLikeCommentary>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILikeCommentary | NewLikeCommentary> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

type LikeCommentaryFormRawValue = FormValueOf<ILikeCommentary>;

type NewLikeCommentaryFormRawValue = FormValueOf<NewLikeCommentary>;

type LikeCommentaryFormDefaults = Pick<NewLikeCommentary, 'id' | 'creationDate'>;

type LikeCommentaryFormGroupContent = {
  id: FormControl<LikeCommentaryFormRawValue['id'] | NewLikeCommentary['id']>;
  creationDate: FormControl<LikeCommentaryFormRawValue['creationDate']>;
  commentary: FormControl<LikeCommentaryFormRawValue['commentary']>;
  extendedUser: FormControl<LikeCommentaryFormRawValue['extendedUser']>;
};

export type LikeCommentaryFormGroup = FormGroup<LikeCommentaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LikeCommentaryFormService {
  createLikeCommentaryFormGroup(likeCommentary: LikeCommentaryFormGroupInput = { id: null }): LikeCommentaryFormGroup {
    const likeCommentaryRawValue = this.convertLikeCommentaryToLikeCommentaryRawValue({
      ...this.getFormDefaults(),
      ...likeCommentary,
    });
    return new FormGroup<LikeCommentaryFormGroupContent>({
      id: new FormControl(
        { value: likeCommentaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      creationDate: new FormControl(likeCommentaryRawValue.creationDate, {
        validators: [Validators.required],
      }),
      commentary: new FormControl(likeCommentaryRawValue.commentary, {
        validators: [Validators.required],
      }),
      extendedUser: new FormControl(likeCommentaryRawValue.extendedUser, {
        validators: [Validators.required],
      }),
    });
  }

  getLikeCommentary(form: LikeCommentaryFormGroup): ILikeCommentary | NewLikeCommentary {
    return this.convertLikeCommentaryRawValueToLikeCommentary(
      form.getRawValue() as LikeCommentaryFormRawValue | NewLikeCommentaryFormRawValue
    );
  }

  resetForm(form: LikeCommentaryFormGroup, likeCommentary: LikeCommentaryFormGroupInput): void {
    const likeCommentaryRawValue = this.convertLikeCommentaryToLikeCommentaryRawValue({ ...this.getFormDefaults(), ...likeCommentary });
    form.reset(
      {
        ...likeCommentaryRawValue,
        id: { value: likeCommentaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LikeCommentaryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
    };
  }

  private convertLikeCommentaryRawValueToLikeCommentary(
    rawLikeCommentary: LikeCommentaryFormRawValue | NewLikeCommentaryFormRawValue
  ): ILikeCommentary | NewLikeCommentary {
    return {
      ...rawLikeCommentary,
      creationDate: dayjs(rawLikeCommentary.creationDate, DATE_TIME_FORMAT),
    };
  }

  private convertLikeCommentaryToLikeCommentaryRawValue(
    likeCommentary: ILikeCommentary | (Partial<NewLikeCommentary> & LikeCommentaryFormDefaults)
  ): LikeCommentaryFormRawValue | PartialWithRequiredKeyOf<NewLikeCommentaryFormRawValue> {
    return {
      ...likeCommentary,
      creationDate: likeCommentary.creationDate ? likeCommentary.creationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
