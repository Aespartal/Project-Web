import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../like-image.test-samples';

import { LikeImageFormService } from './like-image-form.service';

describe('LikeImage Form Service', () => {
  let service: LikeImageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LikeImageFormService);
  });

  describe('Service methods', () => {
    describe('createLikeImageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLikeImageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            image: expect.any(Object),
            extendedUser: expect.any(Object),
          })
        );
      });

      it('passing ILikeImage should create a new form with FormGroup', () => {
        const formGroup = service.createLikeImageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            image: expect.any(Object),
            extendedUser: expect.any(Object),
          })
        );
      });
    });

    describe('getLikeImage', () => {
      it('should return NewLikeImage for default LikeImage initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLikeImageFormGroup(sampleWithNewData);

        const likeImage = service.getLikeImage(formGroup) as any;

        expect(likeImage).toMatchObject(sampleWithNewData);
      });

      it('should return NewLikeImage for empty LikeImage initial value', () => {
        const formGroup = service.createLikeImageFormGroup();

        const likeImage = service.getLikeImage(formGroup) as any;

        expect(likeImage).toMatchObject({});
      });

      it('should return ILikeImage', () => {
        const formGroup = service.createLikeImageFormGroup(sampleWithRequiredData);

        const likeImage = service.getLikeImage(formGroup) as any;

        expect(likeImage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILikeImage should not enable id FormControl', () => {
        const formGroup = service.createLikeImageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLikeImage should disable id FormControl', () => {
        const formGroup = service.createLikeImageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
