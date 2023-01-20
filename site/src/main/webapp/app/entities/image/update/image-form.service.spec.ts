import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../image.test-samples';

import { ImageFormService } from './image-form.service';

describe('Image Form Service', () => {
  let service: ImageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageFormService);
  });

  describe('Service methods', () => {
    describe('createImageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            image: expect.any(Object),
            imageType: expect.any(Object),
            creationDate: expect.any(Object),
            modificationDate: expect.any(Object),
            isPrivate: expect.any(Object),
            extendedUser: expect.any(Object),
          })
        );
      });

      it('passing IImage should create a new form with FormGroup', () => {
        const formGroup = service.createImageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            image: expect.any(Object),
            imageType: expect.any(Object),
            creationDate: expect.any(Object),
            modificationDate: expect.any(Object),
            isPrivate: expect.any(Object),
            extendedUser: expect.any(Object),
          })
        );
      });
    });

    describe('getImage', () => {
      it('should return NewImage for default Image initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createImageFormGroup(sampleWithNewData);

        const image = service.getImage(formGroup) as any;

        expect(image).toMatchObject(sampleWithNewData);
      });

      it('should return NewImage for empty Image initial value', () => {
        const formGroup = service.createImageFormGroup();

        const image = service.getImage(formGroup) as any;

        expect(image).toMatchObject({});
      });

      it('should return IImage', () => {
        const formGroup = service.createImageFormGroup(sampleWithRequiredData);

        const image = service.getImage(formGroup) as any;

        expect(image).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImage should not enable id FormControl', () => {
        const formGroup = service.createImageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImage should disable id FormControl', () => {
        const formGroup = service.createImageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
