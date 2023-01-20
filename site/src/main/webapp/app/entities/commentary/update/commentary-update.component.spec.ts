import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommentaryFormService } from './commentary-form.service';
import { CommentaryService } from '../service/commentary.service';
import { ICommentary } from '../commentary.model';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';
import { IImage } from 'app/entities/image/image.model';
import { ImageService } from 'app/entities/image/service/image.service';

import { CommentaryUpdateComponent } from './commentary-update.component';

describe('Commentary Management Update Component', () => {
  let comp: CommentaryUpdateComponent;
  let fixture: ComponentFixture<CommentaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commentaryFormService: CommentaryFormService;
  let commentaryService: CommentaryService;
  let extendedUserService: ExtendedUserService;
  let imageService: ImageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommentaryUpdateComponent],
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
      .overrideTemplate(CommentaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommentaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commentaryFormService = TestBed.inject(CommentaryFormService);
    commentaryService = TestBed.inject(CommentaryService);
    extendedUserService = TestBed.inject(ExtendedUserService);
    imageService = TestBed.inject(ImageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ExtendedUser query and add missing value', () => {
      const commentary: ICommentary = { id: 456 };
      const extendedUser: IExtendedUser = { id: 63044 };
      commentary.extendedUser = extendedUser;

      const extendedUserCollection: IExtendedUser[] = [{ id: 81651 }];
      jest.spyOn(extendedUserService, 'query').mockReturnValue(of(new HttpResponse({ body: extendedUserCollection })));
      const additionalExtendedUsers = [extendedUser];
      const expectedCollection: IExtendedUser[] = [...additionalExtendedUsers, ...extendedUserCollection];
      jest.spyOn(extendedUserService, 'addExtendedUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commentary });
      comp.ngOnInit();

      expect(extendedUserService.query).toHaveBeenCalled();
      expect(extendedUserService.addExtendedUserToCollectionIfMissing).toHaveBeenCalledWith(
        extendedUserCollection,
        ...additionalExtendedUsers.map(expect.objectContaining)
      );
      expect(comp.extendedUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Image query and add missing value', () => {
      const commentary: ICommentary = { id: 456 };
      const image: IImage = { id: 44443 };
      commentary.image = image;

      const imageCollection: IImage[] = [{ id: 21047 }];
      jest.spyOn(imageService, 'query').mockReturnValue(of(new HttpResponse({ body: imageCollection })));
      const additionalImages = [image];
      const expectedCollection: IImage[] = [...additionalImages, ...imageCollection];
      jest.spyOn(imageService, 'addImageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commentary });
      comp.ngOnInit();

      expect(imageService.query).toHaveBeenCalled();
      expect(imageService.addImageToCollectionIfMissing).toHaveBeenCalledWith(
        imageCollection,
        ...additionalImages.map(expect.objectContaining)
      );
      expect(comp.imagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commentary: ICommentary = { id: 456 };
      const extendedUser: IExtendedUser = { id: 46372 };
      commentary.extendedUser = extendedUser;
      const image: IImage = { id: 21823 };
      commentary.image = image;

      activatedRoute.data = of({ commentary });
      comp.ngOnInit();

      expect(comp.extendedUsersSharedCollection).toContain(extendedUser);
      expect(comp.imagesSharedCollection).toContain(image);
      expect(comp.commentary).toEqual(commentary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentary>>();
      const commentary = { id: 123 };
      jest.spyOn(commentaryFormService, 'getCommentary').mockReturnValue(commentary);
      jest.spyOn(commentaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commentary }));
      saveSubject.complete();

      // THEN
      expect(commentaryFormService.getCommentary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commentaryService.update).toHaveBeenCalledWith(expect.objectContaining(commentary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentary>>();
      const commentary = { id: 123 };
      jest.spyOn(commentaryFormService, 'getCommentary').mockReturnValue({ id: null });
      jest.spyOn(commentaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commentary }));
      saveSubject.complete();

      // THEN
      expect(commentaryFormService.getCommentary).toHaveBeenCalled();
      expect(commentaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentary>>();
      const commentary = { id: 123 };
      jest.spyOn(commentaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commentaryService.update).toHaveBeenCalled();
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

    describe('compareImage', () => {
      it('Should forward to imageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(imageService, 'compareImage');
        comp.compareImage(entity, entity2);
        expect(imageService.compareImage).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
