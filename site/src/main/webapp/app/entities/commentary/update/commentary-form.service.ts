import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICommentary, NewCommentary } from '../commentary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommentary for edit and NewCommentaryFormGroupInput for create.
 */
type CommentaryFormGroupInput = ICommentary | PartialWithRequiredKeyOf<NewCommentary>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICommentary | NewCommentary> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

type CommentaryFormRawValue = FormValueOf<ICommentary>;

type NewCommentaryFormRawValue = FormValueOf<NewCommentary>;

type CommentaryFormDefaults = Pick<NewCommentary, 'id' | 'creationDate'>;

type CommentaryFormGroupContent = {
  id: FormControl<CommentaryFormRawValue['id'] | NewCommentary['id']>;
  description: FormControl<CommentaryFormRawValue['description']>;
  creationDate: FormControl<CommentaryFormRawValue['creationDate']>;
  extendedUser: FormControl<CommentaryFormRawValue['extendedUser']>;
  image: FormControl<CommentaryFormRawValue['image']>;
};

export type CommentaryFormGroup = FormGroup<CommentaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommentaryFormService {
  createCommentaryFormGroup(commentary: CommentaryFormGroupInput = { id: null }): CommentaryFormGroup {
    const commentaryRawValue = this.convertCommentaryToCommentaryRawValue({
      ...this.getFormDefaults(),
      ...commentary,
    });
    return new FormGroup<CommentaryFormGroupContent>({
      id: new FormControl(
        { value: commentaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(commentaryRawValue.description, {
        validators: [Validators.required, Validators.maxLength(3500)],
      }),
      creationDate: new FormControl(commentaryRawValue.creationDate, {
        validators: [Validators.required],
      }),
      extendedUser: new FormControl(commentaryRawValue.extendedUser, {
        validators: [Validators.required],
      }),
      image: new FormControl(commentaryRawValue.image),
    });
  }

  getCommentary(form: CommentaryFormGroup): ICommentary | NewCommentary {
    return this.convertCommentaryRawValueToCommentary(form.getRawValue() as CommentaryFormRawValue | NewCommentaryFormRawValue);
  }

  resetForm(form: CommentaryFormGroup, commentary: CommentaryFormGroupInput): void {
    const commentaryRawValue = this.convertCommentaryToCommentaryRawValue({ ...this.getFormDefaults(), ...commentary });
    form.reset(
      {
        ...commentaryRawValue,
        id: { value: commentaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CommentaryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
    };
  }

  private convertCommentaryRawValueToCommentary(
    rawCommentary: CommentaryFormRawValue | NewCommentaryFormRawValue
  ): ICommentary | NewCommentary {
    return {
      ...rawCommentary,
      creationDate: dayjs(rawCommentary.creationDate, DATE_TIME_FORMAT),
    };
  }

  private convertCommentaryToCommentaryRawValue(
    commentary: ICommentary | (Partial<NewCommentary> & CommentaryFormDefaults)
  ): CommentaryFormRawValue | PartialWithRequiredKeyOf<NewCommentaryFormRawValue> {
    return {
      ...commentary,
      creationDate: commentary.creationDate ? commentary.creationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
