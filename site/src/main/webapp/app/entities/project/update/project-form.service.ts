import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProject, NewProject } from '../project.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProject for edit and NewProjectFormGroupInput for create.
 */
type ProjectFormGroupInput = IProject | PartialWithRequiredKeyOf<NewProject>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProject | NewProject> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

type ProjectFormRawValue = FormValueOf<IProject>;

type NewProjectFormRawValue = FormValueOf<NewProject>;

type ProjectFormDefaults = Pick<NewProject, 'id' | 'creationDate' | 'isPrivate' | 'active'>;

type ProjectFormGroupContent = {
  id: FormControl<ProjectFormRawValue['id'] | NewProject['id']>;
  name: FormControl<ProjectFormRawValue['name']>;
  description: FormControl<ProjectFormRawValue['description']>;
  link: FormControl<ProjectFormRawValue['link']>;
  image: FormControl<ProjectFormRawValue['image']>;
  imageContentType: FormControl<ProjectFormRawValue['imageContentType']>;
  order: FormControl<ProjectFormRawValue['order']>;
  creationDate: FormControl<ProjectFormRawValue['creationDate']>;
  isPrivate: FormControl<ProjectFormRawValue['isPrivate']>;
  active: FormControl<ProjectFormRawValue['active']>;
  extendedUser: FormControl<ProjectFormRawValue['extendedUser']>;
};

export type ProjectFormGroup = FormGroup<ProjectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjectFormService {
  createProjectFormGroup(project: ProjectFormGroupInput = { id: null }): ProjectFormGroup {
    const projectRawValue = this.convertProjectToProjectRawValue({
      ...this.getFormDefaults(),
      ...project,
    });
    return new FormGroup<ProjectFormGroupContent>({
      id: new FormControl(
        { value: projectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(projectRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(projectRawValue.description, {
        validators: [Validators.required, Validators.maxLength(3500)],
      }),
      link: new FormControl(projectRawValue.link, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      image: new FormControl(projectRawValue.image),
      imageContentType: new FormControl(projectRawValue.imageContentType),
      order: new FormControl(projectRawValue.order, {
        validators: [Validators.required],
      }),
      creationDate: new FormControl(projectRawValue.creationDate, {
        validators: [Validators.required],
      }),
      isPrivate: new FormControl(projectRawValue.isPrivate, {
        validators: [Validators.required],
      }),
      active: new FormControl(projectRawValue.active, {
        validators: [Validators.required],
      }),
      extendedUser: new FormControl(projectRawValue.extendedUser, {
        validators: [Validators.required],
      }),
    });
  }

  getProject(form: ProjectFormGroup): IProject | NewProject {
    return this.convertProjectRawValueToProject(form.getRawValue() as ProjectFormRawValue | NewProjectFormRawValue);
  }

  resetForm(form: ProjectFormGroup, project: ProjectFormGroupInput): void {
    const projectRawValue = this.convertProjectToProjectRawValue({ ...this.getFormDefaults(), ...project });
    form.reset(
      {
        ...projectRawValue,
        id: { value: projectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProjectFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      isPrivate: false,
      active: false,
    };
  }

  private convertProjectRawValueToProject(rawProject: ProjectFormRawValue | NewProjectFormRawValue): IProject | NewProject {
    return {
      ...rawProject,
      creationDate: dayjs(rawProject.creationDate, DATE_TIME_FORMAT),
    };
  }

  private convertProjectToProjectRawValue(
    project: IProject | (Partial<NewProject> & ProjectFormDefaults)
  ): ProjectFormRawValue | PartialWithRequiredKeyOf<NewProjectFormRawValue> {
    return {
      ...project,
      creationDate: project.creationDate ? project.creationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
