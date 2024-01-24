import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FollowFormService } from './follow-form.service';
import { FollowService } from '../service/follow.service';
import { IFollow } from '../follow.model';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';

import { FollowUpdateComponent } from './follow-update.component';

describe('Follow Management Update Component', () => {
  let comp: FollowUpdateComponent;
  let fixture: ComponentFixture<FollowUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let followFormService: FollowFormService;
  let followService: FollowService;
  let extendedUserService: ExtendedUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FollowUpdateComponent],
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
      .overrideTemplate(FollowUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FollowUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    followFormService = TestBed.inject(FollowFormService);
    followService = TestBed.inject(FollowService);
    extendedUserService = TestBed.inject(ExtendedUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ExtendedUser query and add missing value', () => {
      const follow: IFollow = { id: 456 };
      const follower: IExtendedUser = { id: 97991 };
      follow.follower = follower;
      const following: IExtendedUser = { id: 86247 };
      follow.following = following;

      const extendedUserCollection: IExtendedUser[] = [{ id: 42730 }];
      jest.spyOn(extendedUserService, 'query').mockReturnValue(of(new HttpResponse({ body: extendedUserCollection })));
      const additionalExtendedUsers = [follower, following];
      const expectedCollection: IExtendedUser[] = [...additionalExtendedUsers, ...extendedUserCollection];
      jest.spyOn(extendedUserService, 'addExtendedUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ follow });
      comp.ngOnInit();

      expect(extendedUserService.query).toHaveBeenCalled();
      expect(extendedUserService.addExtendedUserToCollectionIfMissing).toHaveBeenCalledWith(
        extendedUserCollection,
        ...additionalExtendedUsers.map(expect.objectContaining)
      );
      expect(comp.extendedUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const follow: IFollow = { id: 456 };
      const follower: IExtendedUser = { id: 75004 };
      follow.follower = follower;
      const following: IExtendedUser = { id: 3255 };
      follow.following = following;

      activatedRoute.data = of({ follow });
      comp.ngOnInit();

      expect(comp.extendedUsersSharedCollection).toContain(follower);
      expect(comp.extendedUsersSharedCollection).toContain(following);
      expect(comp.follow).toEqual(follow);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFollow>>();
      const follow = { id: 123 };
      jest.spyOn(followFormService, 'getFollow').mockReturnValue(follow);
      jest.spyOn(followService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ follow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: follow }));
      saveSubject.complete();

      // THEN
      expect(followFormService.getFollow).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(followService.update).toHaveBeenCalledWith(expect.objectContaining(follow));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFollow>>();
      const follow = { id: 123 };
      jest.spyOn(followFormService, 'getFollow').mockReturnValue({ id: null });
      jest.spyOn(followService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ follow: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: follow }));
      saveSubject.complete();

      // THEN
      expect(followFormService.getFollow).toHaveBeenCalled();
      expect(followService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFollow>>();
      const follow = { id: 123 };
      jest.spyOn(followService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ follow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(followService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExtendedUser', () => {
      it('Should forward to extendedUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(extendedUserService, 'compareExtendedUser');
        comp.compareExtendedUser(entity, entity2);
        expect(extendedUserService.compareExtendedUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
