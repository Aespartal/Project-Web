import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILikeImage, NewLikeImage } from '../like-image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILikeImage for edit and NewLikeImageFormGroupInput for create.
 */
type LikeImageFormGroupInput = ILikeImage | PartialWithRequiredKeyOf<NewLikeImage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILikeImage | NewLikeImage> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

type LikeImageFormRawValue = FormValueOf<ILikeImage>;

type NewLikeImageFormRawValue = FormValueOf<NewLikeImage>;

type LikeImageFormDefaults = Pick<NewLikeImage, 'id' | 'creationDate'>;

type LikeImageFormGroupContent = {
  id: FormControl<LikeImageFormRawValue['id'] | NewLikeImage['id']>;
  creationDate: FormControl<LikeImageFormRawValue['creationDate']>;
  image: FormControl<LikeImageFormRawValue['image']>;
  extendedUser: FormControl<LikeImageFormRawValue['extendedUser']>;
};

export type LikeImageFormGroup = FormGroup<LikeImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LikeImageFormService {
  createLikeImageFormGroup(likeImage: LikeImageFormGroupInput = { id: null }): LikeImageFormGroup {
    const likeImageRawValue = this.convertLikeImageToLikeImageRawValue({
      ...this.getFormDefaults(),
      ...likeImage,
    });
    return new FormGroup<LikeImageFormGroupContent>({
      id: new FormControl(
        { value: likeImageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      creationDate: new FormControl(likeImageRawValue.creationDate, {
        validators: [Validators.required],
      }),
      image: new FormControl(likeImageRawValue.image, {
        validators: [Validators.required],
      }),
      extendedUser: new FormControl(likeImageRawValue.extendedUser, {
        validators: [Validators.required],
      }),
    });
  }

  getLikeImage(form: LikeImageFormGroup): ILikeImage | NewLikeImage {
    return this.convertLikeImageRawValueToLikeImage(form.getRawValue() as LikeImageFormRawValue | NewLikeImageFormRawValue);
  }

  resetForm(form: LikeImageFormGroup, likeImage: LikeImageFormGroupInput): void {
    const likeImageRawValue = this.convertLikeImageToLikeImageRawValue({ ...this.getFormDefaults(), ...likeImage });
    form.reset(
      {
        ...likeImageRawValue,
        id: { value: likeImageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LikeImageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
    };
  }

  private convertLikeImageRawValueToLikeImage(rawLikeImage: LikeImageFormRawValue | NewLikeImageFormRawValue): ILikeImage | NewLikeImage {
    return {
      ...rawLikeImage,
      creationDate: dayjs(rawLikeImage.creationDate, DATE_TIME_FORMAT),
    };
  }

  private convertLikeImageToLikeImageRawValue(
    likeImage: ILikeImage | (Partial<NewLikeImage> & LikeImageFormDefaults)
  ): LikeImageFormRawValue | PartialWithRequiredKeyOf<NewLikeImageFormRawValue> {
    return {
      ...likeImage,
      creationDate: likeImage.creationDate ? likeImage.creationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
