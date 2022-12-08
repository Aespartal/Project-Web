import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../extended-user.test-samples';

import { ExtendedUserFormService } from './extended-user-form.service';

describe('ExtendedUser Form Service', () => {
  let service: ExtendedUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExtendedUserFormService);
  });

  describe('Service methods', () => {
    describe('createExtendedUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExtendedUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            description: expect.any(Object),
            web: expect.any(Object),
            location: expect.any(Object),
            profession: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IExtendedUser should create a new form with FormGroup', () => {
        const formGroup = service.createExtendedUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            description: expect.any(Object),
            web: expect.any(Object),
            location: expect.any(Object),
            profession: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getExtendedUser', () => {
      it('should return NewExtendedUser for default ExtendedUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createExtendedUserFormGroup(sampleWithNewData);

        const extendedUser = service.getExtendedUser(formGroup) as any;

        expect(extendedUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewExtendedUser for empty ExtendedUser initial value', () => {
        const formGroup = service.createExtendedUserFormGroup();

        const extendedUser = service.getExtendedUser(formGroup) as any;

        expect(extendedUser).toMatchObject({});
      });

      it('should return IExtendedUser', () => {
        const formGroup = service.createExtendedUserFormGroup(sampleWithRequiredData);

        const extendedUser = service.getExtendedUser(formGroup) as any;

        expect(extendedUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExtendedUser should not enable id FormControl', () => {
        const formGroup = service.createExtendedUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExtendedUser should disable id FormControl', () => {
        const formGroup = service.createExtendedUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
