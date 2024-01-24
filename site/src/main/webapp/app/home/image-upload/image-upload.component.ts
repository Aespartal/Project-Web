import { Component } from '@angular/core';
import { ImageCroppedEvent } from 'ngx-image-cropper';
import { DomSanitizer } from '@angular/platform-browser';
import { ImageService } from 'app/entities/image/service/image.service';
import { IImage, NewImage } from 'app/entities/image/image.model';
import { Router } from '@angular/router';

export interface Dimensions {
  width: number;
  height: number;
}

@Component({
  selector: 'jhi-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.scss']
})
export class ImageUploadComponent {

  steps: any[];
  activeIndex = 0;

  imageChangedEvent: any = '';
  croppedImage: any = '';
  showCropper = false;
  loading = false;
  hidden = false;
  isUploadingImage = false;

  editForm: any = {
    title: '',
    description: '',
    imageFile: null,
    isPrivate: false
  };

  constructor(
    private sanitizer: DomSanitizer,
    private imageService: ImageService,
    private router: Router
    ) {
    this.steps = [
      { label: 'Seleccionar Imagen' },
      { label: 'Título y Descripción' }
    ];
  }

  fileChangeEvent(event: any): void {
    this.editForm.imageFile = event;
    this.loading = true;
    this.imageChangedEvent = event;
    // eslint-disable-next-line no-console
    console.log(event);
  }

  imageCropped(event: ImageCroppedEvent): void  {
    this.croppedImage = this.sanitizer.bypassSecurityTrustUrl(event.objectUrl ?? event.base64 ?? '');
    this.editForm.imageFile = event.blob;
  }

  imageLoaded(): void {
    this.showCropper = true;
}
  cropperReady(sourceImageDimensions: Dimensions): void {
    // eslint-disable-next-line no-console
    console.log('Cropper ready', sourceImageDimensions);
    this.loading = false;
  }

  loadImageFailed(): void {
    // eslint-disable-next-line no-console
    console.error('Load image failed');
  }

  nextStep(): void {
    this.activeIndex++;
  }

  prevStep(): void {
    this.activeIndex--;
  }

  submitForm(): void {
    this.isUploadingImage = true;
    const newImage = this.createImageFromForm();

    const formData = new FormData();
    formData.append('file', this.editForm.imageFile);

    this.imageService.create(newImage, formData).subscribe({
      next: (res) => {
        const image = res.body;
        this.isUploadingImage = false;
        this.router.navigate(['/', 'image', image!.id, 'photo-view']);
      },
      error: () => {
        this.isUploadingImage = false;
      },
    })
  }

  createImageFromForm(): NewImage {
    return {
      id: null,
      title: this.editForm.title,
      description: this.editForm.description,
      isPrivate: this.editForm.isPrivate
    }
  }
}
