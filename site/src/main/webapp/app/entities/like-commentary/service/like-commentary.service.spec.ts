import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILikeCommentary } from '../like-commentary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../like-commentary.test-samples';

import { LikeCommentaryService, RestLikeCommentary } from './like-commentary.service';

const requireRestSample: RestLikeCommentary = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
};

describe('LikeCommentary Service', () => {
  let service: LikeCommentaryService;
  let httpMock: HttpTestingController;
  let expectedResult: ILikeCommentary | ILikeCommentary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LikeCommentaryService);
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

    it('should create a LikeCommentary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const likeCommentary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(likeCommentary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LikeCommentary', () => {
      const likeCommentary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(likeCommentary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LikeCommentary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LikeCommentary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LikeCommentary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLikeCommentaryToCollectionIfMissing', () => {
      it('should add a LikeCommentary to an empty array', () => {
        const likeCommentary: ILikeCommentary = sampleWithRequiredData;
        expectedResult = service.addLikeCommentaryToCollectionIfMissing([], likeCommentary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(likeCommentary);
      });

      it('should not add a LikeCommentary to an array that contains it', () => {
        const likeCommentary: ILikeCommentary = sampleWithRequiredData;
        const likeCommentaryCollection: ILikeCommentary[] = [
          {
            ...likeCommentary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLikeCommentaryToCollectionIfMissing(likeCommentaryCollection, likeCommentary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LikeCommentary to an array that doesn't contain it", () => {
        const likeCommentary: ILikeCommentary = sampleWithRequiredData;
        const likeCommentaryCollection: ILikeCommentary[] = [sampleWithPartialData];
        expectedResult = service.addLikeCommentaryToCollectionIfMissing(likeCommentaryCollection, likeCommentary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(likeCommentary);
      });

      it('should add only unique LikeCommentary to an array', () => {
        const likeCommentaryArray: ILikeCommentary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const likeCommentaryCollection: ILikeCommentary[] = [sampleWithRequiredData];
        expectedResult = service.addLikeCommentaryToCollectionIfMissing(likeCommentaryCollection, ...likeCommentaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const likeCommentary: ILikeCommentary = sampleWithRequiredData;
        const likeCommentary2: ILikeCommentary = sampleWithPartialData;
        expectedResult = service.addLikeCommentaryToCollectionIfMissing([], likeCommentary, likeCommentary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(likeCommentary);
        expect(expectedResult).toContain(likeCommentary2);
      });

      it('should accept null and undefined values', () => {
        const likeCommentary: ILikeCommentary = sampleWithRequiredData;
        expectedResult = service.addLikeCommentaryToCollectionIfMissing([], null, likeCommentary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(likeCommentary);
      });

      it('should return initial array if no LikeCommentary is added', () => {
        const likeCommentaryCollection: ILikeCommentary[] = [sampleWithRequiredData];
        expectedResult = service.addLikeCommentaryToCollectionIfMissing(likeCommentaryCollection, undefined, null);
        expect(expectedResult).toEqual(likeCommentaryCollection);
      });
    });

    describe('compareLikeCommentary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLikeCommentary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLikeCommentary(entity1, entity2);
        const compareResult2 = service.compareLikeCommentary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLikeCommentary(entity1, entity2);
        const compareResult2 = service.compareLikeCommentary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLikeCommentary(entity1, entity2);
        const compareResult2 = service.compareLikeCommentary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
