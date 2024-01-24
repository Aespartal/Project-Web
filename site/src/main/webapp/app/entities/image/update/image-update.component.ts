/* eslint-disable @typescript-eslint/restrict-plus-operands */
import { Component, ElementRef, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ImageFormService, ImageFormGroup } from './image-form.service';
import { IImage } from '../image.model';
import { ImageService } from '../service/image.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { EventManager } from 'app/core/util/event-manager.service';

@Component({
  selector: 'jhi-image-update',
  templateUrl: './image-update.component.html',
})
export class ImageUpdateComponent implements OnInit {
  isSaving = false;
  image: IImage | null = null;

  extendedUsersSharedCollection: IExtendedUser[] = [];

  formData = new FormData();

  imagenBase64!: string;

  editForm: ImageFormGroup = this.imageFormService.createImageFormGroup();

  constructor(
    protected imageService: ImageService,
    protected imageFormService: ImageFormService,
    protected extendedUserService: ExtendedUserService,
    protected activatedRoute: ActivatedRoute,
    protected elementRef: ElementRef,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
  ) {}

  compareExtendedUser = (o1: IExtendedUser | null, o2: IExtendedUser | null): boolean =>
    this.extendedUserService.compareExtendedUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ image }) => {
      this.image = image;
      if (image) {
        this.updateForm(image);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const image = this.imageFormService.getImage(this.editForm);
    if (image.id !== null) {
      this.subscribeToSaveResponse(this.imageService.update(image, this.formData));
    } else {
      this.subscribeToSaveResponse(this.imageService.create(image, this.formData));
    }
  }

  onFileChanged(event: any, field: string): void {
    const file = event.target.files[0];
    const reader = new FileReader();
    if(file.type.match('image.*')){
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imagenBase64 = reader.result as string;
      };
      this.formData = new FormData();
      this.formData.append(field, file);
    } else {
      console.error('El archivo no es una imagen');
  }
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  clearInputImage(idInput: string): void {
    this.formData = new FormData();
    this.imagenBase64 = '';
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImage>>): void {
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

  protected updateForm(image: IImage): void {
    this.image = image;
    this.imageFormService.resetForm(this.editForm, image);

    this.extendedUsersSharedCollection = this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(
      this.extendedUsersSharedCollection,
      image.extendedUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.extendedUserService
      .query()
      .pipe(map((res: HttpResponse<IExtendedUser[]>) => res.body ?? []))
      .pipe(
        map((extendedUsers: IExtendedUser[]) =>
          this.extendedUserService.addExtendedUserToCollectionIfMissing<IExtendedUser>(extendedUsers, this.image?.extendedUser)
        )
      )
      .subscribe((extendedUsers: IExtendedUser[]) => (this.extendedUsersSharedCollection = extendedUsers));
  }
}
