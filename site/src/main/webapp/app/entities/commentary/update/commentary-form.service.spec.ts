import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../commentary.test-samples';

import { CommentaryFormService } from './commentary-form.service';

describe('Commentary Form Service', () => {
  let service: CommentaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommentaryFormService);
  });

  describe('Service methods', () => {
    describe('createCommentaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCommentaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            creationDate: expect.any(Object),
            extendedUser: expect.any(Object),
            image: expect.any(Object),
          })
        );
      });

      it('passing ICommentary should create a new form with FormGroup', () => {
        const formGroup = service.createCommentaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            creationDate: expect.any(Object),
            extendedUser: expect.any(Object),
            image: expect.any(Object),
          })
        );
      });
    });

    describe('getCommentary', () => {
      it('should return NewCommentary for default Commentary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCommentaryFormGroup(sampleWithNewData);

        const commentary = service.getCommentary(formGroup) as any;

        expect(commentary).toMatchObject(sampleWithNewData);
      });

      it('should return NewCommentary for empty Commentary initial value', () => {
        const formGroup = service.createCommentaryFormGroup();

        const commentary = service.getCommentary(formGroup) as any;

        expect(commentary).toMatchObject({});
      });

      it('should return ICommentary', () => {
        const formGroup = service.createCommentaryFormGroup(sampleWithRequiredData);

        const commentary = service.getCommentary(formGroup) as any;

        expect(commentary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICommentary should not enable id FormControl', () => {
        const formGroup = service.createCommentaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCommentary should disable id FormControl', () => {
        const formGroup = service.createCommentaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
