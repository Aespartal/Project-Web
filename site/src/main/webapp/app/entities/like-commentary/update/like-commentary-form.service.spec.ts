import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../like-commentary.test-samples';

import { LikeCommentaryFormService } from './like-commentary-form.service';

describe('LikeCommentary Form Service', () => {
  let service: LikeCommentaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LikeCommentaryFormService);
  });

  describe('Service methods', () => {
    describe('createLikeCommentaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLikeCommentaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            commentary: expect.any(Object),
            extendedUser: expect.any(Object),
          })
        );
      });

      it('passing ILikeCommentary should create a new form with FormGroup', () => {
        const formGroup = service.createLikeCommentaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            commentary: expect.any(Object),
            extendedUser: expect.any(Object),
          })
        );
      });
    });

    describe('getLikeCommentary', () => {
      it('should return NewLikeCommentary for default LikeCommentary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLikeCommentaryFormGroup(sampleWithNewData);

        const likeCommentary = service.getLikeCommentary(formGroup) as any;

        expect(likeCommentary).toMatchObject(sampleWithNewData);
      });

      it('should return NewLikeCommentary for empty LikeCommentary initial value', () => {
        const formGroup = service.createLikeCommentaryFormGroup();

        const likeCommentary = service.getLikeCommentary(formGroup) as any;

        expect(likeCommentary).toMatchObject({});
      });

      it('should return ILikeCommentary', () => {
        const formGroup = service.createLikeCommentaryFormGroup(sampleWithRequiredData);

        const likeCommentary = service.getLikeCommentary(formGroup) as any;

        expect(likeCommentary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILikeCommentary should not enable id FormControl', () => {
        const formGroup = service.createLikeCommentaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLikeCommentary should disable id FormControl', () => {
        const formGroup = service.createLikeCommentaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
