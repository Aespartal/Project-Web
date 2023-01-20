import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LikeCommentaryFormService } from './like-commentary-form.service';
import { LikeCommentaryService } from '../service/like-commentary.service';
import { ILikeCommentary } from '../like-commentary.model';
import { ICommentary } from 'app/entities/commentary/commentary.model';
import { CommentaryService } from 'app/entities/commentary/service/commentary.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';

import { LikeCommentaryUpdateComponent } from './like-commentary-update.component';

describe('LikeCommentary Management Update Component', () => {
  let comp: LikeCommentaryUpdateComponent;
  let fixture: ComponentFixture<LikeCommentaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let likeCommentaryFormService: LikeCommentaryFormService;
  let likeCommentaryService: LikeCommentaryService;
  let commentaryService: CommentaryService;
  let extendedUserService: ExtendedUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LikeCommentaryUpdateComponent],
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
      .overrideTemplate(LikeCommentaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LikeCommentaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    likeCommentaryFormService = TestBed.inject(LikeCommentaryFormService);
    likeCommentaryService = TestBed.inject(LikeCommentaryService);
    commentaryService = TestBed.inject(CommentaryService);
    extendedUserService = TestBed.inject(ExtendedUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Commentary query and add missing value', () => {
      const likeCommentary: ILikeCommentary = { id: 456 };
      const commentary: ICommentary = { id: 85567 };
      likeCommentary.commentary = commentary;

      const commentaryCollection: ICommentary[] = [{ id: 29467 }];
      jest.spyOn(commentaryService, 'query').mockReturnValue(of(new HttpResponse({ body: commentaryCollection })));
      const additionalCommentaries = [commentary];
      const expectedCollection: ICommentary[] = [...additionalCommentaries, ...commentaryCollection];
      jest.spyOn(commentaryService, 'addCommentaryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likeCommentary });
      comp.ngOnInit();

      expect(commentaryService.query).toHaveBeenCalled();
      expect(commentaryService.addCommentaryToCollectionIfMissing).toHaveBeenCalledWith(
        commentaryCollection,
        ...additionalCommentaries.map(expect.objectContaining)
      );
      expect(comp.commentariesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ExtendedUser query and add missing value', () => {
      const likeCommentary: ILikeCommentary = { id: 456 };
      const extendedUser: IExtendedUser = { id: 66079 };
      likeCommentary.extendedUser = extendedUser;

      const extendedUserCollection: IExtendedUser[] = [{ id: 92749 }];
      jest.spyOn(extendedUserService, 'query').mockReturnValue(of(new HttpResponse({ body: extendedUserCollection })));
      const additionalExtendedUsers = [extendedUser];
      const expectedCollection: IExtendedUser[] = [...additionalExtendedUsers, ...extendedUserCollection];
      jest.spyOn(extendedUserService, 'addExtendedUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likeCommentary });
      comp.ngOnInit();

      expect(extendedUserService.query).toHaveBeenCalled();
      expect(extendedUserService.addExtendedUserToCollectionIfMissing).toHaveBeenCalledWith(
        extendedUserCollection,
        ...additionalExtendedUsers.map(expect.objectContaining)
      );
      expect(comp.extendedUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const likeCommentary: ILikeCommentary = { id: 456 };
      const commentary: ICommentary = { id: 49489 };
      likeCommentary.commentary = commentary;
      const extendedUser: IExtendedUser = { id: 97111 };
      likeCommentary.extendedUser = extendedUser;

      activatedRoute.data = of({ likeCommentary });
      comp.ngOnInit();

      expect(comp.commentariesSharedCollection).toContain(commentary);
      expect(comp.extendedUsersSharedCollection).toContain(extendedUser);
      expect(comp.likeCommentary).toEqual(likeCommentary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeCommentary>>();
      const likeCommentary = { id: 123 };
      jest.spyOn(likeCommentaryFormService, 'getLikeCommentary').mockReturnValue(likeCommentary);
      jest.spyOn(likeCommentaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeCommentary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likeCommentary }));
      saveSubject.complete();

      // THEN
      expect(likeCommentaryFormService.getLikeCommentary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(likeCommentaryService.update).toHaveBeenCalledWith(expect.objectContaining(likeCommentary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeCommentary>>();
      const likeCommentary = { id: 123 };
      jest.spyOn(likeCommentaryFormService, 'getLikeCommentary').mockReturnValue({ id: null });
      jest.spyOn(likeCommentaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeCommentary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likeCommentary }));
      saveSubject.complete();

      // THEN
      expect(likeCommentaryFormService.getLikeCommentary).toHaveBeenCalled();
      expect(likeCommentaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeCommentary>>();
      const likeCommentary = { id: 123 };
      jest.spyOn(likeCommentaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeCommentary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(likeCommentaryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCommentary', () => {
      it('Should forward to commentaryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(commentaryService, 'compareCommentary');
        comp.compareCommentary(entity, entity2);
        expect(commentaryService.compareCommentary).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
