import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImage, NewImage } from '../image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImage for edit and NewImageFormGroupInput for create.
 */
type ImageFormGroupInput = IImage | PartialWithRequiredKeyOf<NewImage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImage | NewImage> = Omit<T, 'creationDate' | 'modificationDate'> & {
  creationDate?: string | null;
  modificationDate?: string | null;
};

type ImageFormRawValue = FormValueOf<IImage>;

type NewImageFormRawValue = FormValueOf<NewImage>;

type ImageFormDefaults = Pick<NewImage, 'id' | 'creationDate' | 'modificationDate' | 'isPrivate'>;

type ImageFormGroupContent = {
  id: FormControl<ImageFormRawValue['id'] | NewImage['id']>;
  name: FormControl<ImageFormRawValue['name']>;
  description: FormControl<ImageFormRawValue['description']>;
  image: FormControl<ImageFormRawValue['image']>;
  imageType: FormControl<ImageFormRawValue['imageType']>;
  creationDate: FormControl<ImageFormRawValue['creationDate']>;
  modificationDate: FormControl<ImageFormRawValue['modificationDate']>;
  isPrivate: FormControl<ImageFormRawValue['isPrivate']>;
  extendedUser: FormControl<ImageFormRawValue['extendedUser']>;
};

export type ImageFormGroup = FormGroup<ImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageFormService {
  createImageFormGroup(image: ImageFormGroupInput = { id: null }): ImageFormGroup {
    const imageRawValue = this.convertImageToImageRawValue({
      ...this.getFormDefaults(),
      ...image,
    });
    return new FormGroup<ImageFormGroupContent>({
      id: new FormControl(
        { value: imageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(imageRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(imageRawValue.description, {
        validators: [Validators.required, Validators.maxLength(3500)],
      }),
      image: new FormControl(imageRawValue.image, {
        validators: [Validators.required, Validators.maxLength(3500)],
      }),
      imageType: new FormControl(imageRawValue.imageType, {
        validators: [Validators.required],
      }),
      creationDate: new FormControl(imageRawValue.creationDate, {
        validators: [Validators.required],
      }),
      modificationDate: new FormControl(imageRawValue.modificationDate),
      isPrivate: new FormControl(imageRawValue.isPrivate, {
        validators: [Validators.required],
      }),
      extendedUser: new FormControl(imageRawValue.extendedUser, {
        validators: [Validators.required],
      }),
    });
  }

  getImage(form: ImageFormGroup): IImage | NewImage {
    return this.convertImageRawValueToImage(form.getRawValue() as ImageFormRawValue | NewImageFormRawValue);
  }

  resetForm(form: ImageFormGroup, image: ImageFormGroupInput): void {
    const imageRawValue = this.convertImageToImageRawValue({ ...this.getFormDefaults(), ...image });
    form.reset(
      {
        ...imageRawValue,
        id: { value: imageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ImageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      modificationDate: currentTime,
      isPrivate: false,
    };
  }

  private convertImageRawValueToImage(rawImage: ImageFormRawValue | NewImageFormRawValue): IImage | NewImage {
    return {
      ...rawImage,
      creationDate: dayjs(rawImage.creationDate, DATE_TIME_FORMAT),
      modificationDate: dayjs(rawImage.modificationDate, DATE_TIME_FORMAT),
    };
  }

  private convertImageToImageRawValue(
    image: IImage | (Partial<NewImage> & ImageFormDefaults)
  ): ImageFormRawValue | PartialWithRequiredKeyOf<NewImageFormRawValue> {
    return {
      ...image,
      creationDate: image.creationDate ? image.creationDate.format(DATE_TIME_FORMAT) : undefined,
      modificationDate: image.modificationDate ? image.modificationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
