import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LikeImageFormService } from './like-image-form.service';
import { LikeImageService } from '../service/like-image.service';
import { ILikeImage } from '../like-image.model';
import { IImage } from 'app/entities/image/image.model';
import { ImageService } from 'app/entities/image/service/image.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ExtendedUserService } from 'app/entities/extended-user/service/extended-user.service';

import { LikeImageUpdateComponent } from './like-image-update.component';

describe('LikeImage Management Update Component', () => {
  let comp: LikeImageUpdateComponent;
  let fixture: ComponentFixture<LikeImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let likeImageFormService: LikeImageFormService;
  let likeImageService: LikeImageService;
  let imageService: ImageService;
  let extendedUserService: ExtendedUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LikeImageUpdateComponent],
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
      .overrideTemplate(LikeImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LikeImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    likeImageFormService = TestBed.inject(LikeImageFormService);
    likeImageService = TestBed.inject(LikeImageService);
    imageService = TestBed.inject(ImageService);
    extendedUserService = TestBed.inject(ExtendedUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Image query and add missing value', () => {
      const likeImage: ILikeImage = { id: 456 };
      const image: IImage = { id: 8678 };
      likeImage.image = image;

      const imageCollection: IImage[] = [{ id: 20019 }];
      jest.spyOn(imageService, 'query').mockReturnValue(of(new HttpResponse({ body: imageCollection })));
      const additionalImages = [image];
      const expectedCollection: IImage[] = [...additionalImages, ...imageCollection];
      jest.spyOn(imageService, 'addImageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likeImage });
      comp.ngOnInit();

      expect(imageService.query).toHaveBeenCalled();
      expect(imageService.addImageToCollectionIfMissing).toHaveBeenCalledWith(
        imageCollection,
        ...additionalImages.map(expect.objectContaining)
      );
      expect(comp.imagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ExtendedUser query and add missing value', () => {
      const likeImage: ILikeImage = { id: 456 };
      const extendedUser: IExtendedUser = { id: 76422 };
      likeImage.extendedUser = extendedUser;

      const extendedUserCollection: IExtendedUser[] = [{ id: 86447 }];
      jest.spyOn(extendedUserService, 'query').mockReturnValue(of(new HttpResponse({ body: extendedUserCollection })));
      const additionalExtendedUsers = [extendedUser];
      const expectedCollection: IExtendedUser[] = [...additionalExtendedUsers, ...extendedUserCollection];
      jest.spyOn(extendedUserService, 'addExtendedUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likeImage });
      comp.ngOnInit();

      expect(extendedUserService.query).toHaveBeenCalled();
      expect(extendedUserService.addExtendedUserToCollectionIfMissing).toHaveBeenCalledWith(
        extendedUserCollection,
        ...additionalExtendedUsers.map(expect.objectContaining)
      );
      expect(comp.extendedUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const likeImage: ILikeImage = { id: 456 };
      const image: IImage = { id: 78300 };
      likeImage.image = image;
      const extendedUser: IExtendedUser = { id: 8921 };
      likeImage.extendedUser = extendedUser;

      activatedRoute.data = of({ likeImage });
      comp.ngOnInit();

      expect(comp.imagesSharedCollection).toContain(image);
      expect(comp.extendedUsersSharedCollection).toContain(extendedUser);
      expect(comp.likeImage).toEqual(likeImage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeImage>>();
      const likeImage = { id: 123 };
      jest.spyOn(likeImageFormService, 'getLikeImage').mockReturnValue(likeImage);
      jest.spyOn(likeImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likeImage }));
      saveSubject.complete();

      // THEN
      expect(likeImageFormService.getLikeImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(likeImageService.update).toHaveBeenCalledWith(expect.objectContaining(likeImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeImage>>();
      const likeImage = { id: 123 };
      jest.spyOn(likeImageFormService, 'getLikeImage').mockReturnValue({ id: null });
      jest.spyOn(likeImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likeImage }));
      saveSubject.complete();

      // THEN
      expect(likeImageFormService.getLikeImage).toHaveBeenCalled();
      expect(likeImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikeImage>>();
      const likeImage = { id: 123 };
      jest.spyOn(likeImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likeImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(likeImageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareImage', () => {
      it('Should forward to imageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(imageService, 'compareImage');
        comp.compareImage(entity, entity2);
        expect(imageService.compareImage).toHaveBeenCalledWith(entity, entity2);
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
