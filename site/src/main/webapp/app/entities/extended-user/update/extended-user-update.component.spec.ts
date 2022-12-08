import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExtendedUserFormService } from './extended-user-form.service';
import { ExtendedUserService } from '../service/extended-user.service';
import { IExtendedUser } from '../extended-user.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ExtendedUserUpdateComponent } from './extended-user-update.component';

describe('ExtendedUser Management Update Component', () => {
  let comp: ExtendedUserUpdateComponent;
  let fixture: ComponentFixture<ExtendedUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let extendedUserFormService: ExtendedUserFormService;
  let extendedUserService: ExtendedUserService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExtendedUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ExtendedUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExtendedUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    extendedUserFormService = TestBed.inject(ExtendedUserFormService);
    extendedUserService = TestBed.inject(ExtendedUserService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const extendedUser: IExtendedUser = { id: 456 };
      const user: IUser = { id: 71507 };
      extendedUser.user = user;

      const userCollection: IUser[] = [{ id: 26054 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ extendedUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const extendedUser: IExtendedUser = { id: 456 };
      const user: IUser = { id: 21416 };
      extendedUser.user = user;

      activatedRoute.data = of({ extendedUser });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.extendedUser).toEqual(extendedUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtendedUser>>();
      const extendedUser = { id: 123 };
      jest.spyOn(extendedUserFormService, 'getExtendedUser').mockReturnValue(extendedUser);
      jest.spyOn(extendedUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extendedUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extendedUser }));
      saveSubject.complete();

      // THEN
      expect(extendedUserFormService.getExtendedUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(extendedUserService.update).toHaveBeenCalledWith(expect.objectContaining(extendedUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtendedUser>>();
      const extendedUser = { id: 123 };
      jest.spyOn(extendedUserFormService, 'getExtendedUser').mockReturnValue({ id: null });
      jest.spyOn(extendedUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extendedUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extendedUser }));
      saveSubject.complete();

      // THEN
      expect(extendedUserFormService.getExtendedUser).toHaveBeenCalled();
      expect(extendedUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtendedUser>>();
      const extendedUser = { id: 123 };
      jest.spyOn(extendedUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extendedUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(extendedUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
