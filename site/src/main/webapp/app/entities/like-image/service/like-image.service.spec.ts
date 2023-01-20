import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILikeImage } from '../like-image.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../like-image.test-samples';

import { LikeImageService, RestLikeImage } from './like-image.service';

const requireRestSample: RestLikeImage = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
};

describe('LikeImage Service', () => {
  let service: LikeImageService;
  let httpMock: HttpTestingController;
  let expectedResult: ILikeImage | ILikeImage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LikeImageService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a LikeImage', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const likeImage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(likeImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LikeImage', () => {
      const likeImage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(likeImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LikeImage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LikeImage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LikeImage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLikeImageToCollectionIfMissing', () => {
      it('should add a LikeImage to an empty array', () => {
        const likeImage: ILikeImage = sampleWithRequiredData;
        expectedResult = service.addLikeImageToCollectionIfMissing([], likeImage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(likeImage);
      });

      it('should not add a LikeImage to an array that contains it', () => {
        const likeImage: ILikeImage = sampleWithRequiredData;
        const likeImageCollection: ILikeImage[] = [
          {
            ...likeImage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLikeImageToCollectionIfMissing(likeImageCollection, likeImage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LikeImage to an array that doesn't contain it", () => {
        const likeImage: ILikeImage = sampleWithRequiredData;
        const likeImageCollection: ILikeImage[] = [sampleWithPartialData];
        expectedResult = service.addLikeImageToCollectionIfMissing(likeImageCollection, likeImage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(likeImage);
      });

      it('should add only unique LikeImage to an array', () => {
        const likeImageArray: ILikeImage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const likeImageCollection: ILikeImage[] = [sampleWithRequiredData];
        expectedResult = service.addLikeImageToCollectionIfMissing(likeImageCollection, ...likeImageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const likeImage: ILikeImage = sampleWithRequiredData;
        const likeImage2: ILikeImage = sampleWithPartialData;
        expectedResult = service.addLikeImageToCollectionIfMissing([], likeImage, likeImage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(likeImage);
        expect(expectedResult).toContain(likeImage2);
      });

      it('should accept null and undefined values', () => {
        const likeImage: ILikeImage = sampleWithRequiredData;
        expectedResult = service.addLikeImageToCollectionIfMissing([], null, likeImage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(likeImage);
      });

      it('should return initial array if no LikeImage is added', () => {
        const likeImageCollection: ILikeImage[] = [sampleWithRequiredData];
        expectedResult = service.addLikeImageToCollectionIfMissing(likeImageCollection, undefined, null);
        expect(expectedResult).toEqual(likeImageCollection);
      });
    });

    describe('compareLikeImage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLikeImage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLikeImage(entity1, entity2);
        const compareResult2 = service.compareLikeImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLikeImage(entity1, entity2);
        const compareResult2 = service.compareLikeImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLikeImage(entity1, entity2);
        const compareResult2 = service.compareLikeImage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
