import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommentary } from '../commentary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../commentary.test-samples';

import { CommentaryService, RestCommentary } from './commentary.service';

const requireRestSample: RestCommentary = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
};

describe('Commentary Service', () => {
  let service: CommentaryService;
  let httpMock: HttpTestingController;
  let expectedResult: ICommentary | ICommentary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommentaryService);
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

    it('should create a Commentary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const commentary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(commentary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Commentary', () => {
      const commentary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(commentary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Commentary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Commentary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Commentary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCommentaryToCollectionIfMissing', () => {
      it('should add a Commentary to an empty array', () => {
        const commentary: ICommentary = sampleWithRequiredData;
        expectedResult = service.addCommentaryToCollectionIfMissing([], commentary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commentary);
      });

      it('should not add a Commentary to an array that contains it', () => {
        const commentary: ICommentary = sampleWithRequiredData;
        const commentaryCollection: ICommentary[] = [
          {
            ...commentary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCommentaryToCollectionIfMissing(commentaryCollection, commentary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Commentary to an array that doesn't contain it", () => {
        const commentary: ICommentary = sampleWithRequiredData;
        const commentaryCollection: ICommentary[] = [sampleWithPartialData];
        expectedResult = service.addCommentaryToCollectionIfMissing(commentaryCollection, commentary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commentary);
      });

      it('should add only unique Commentary to an array', () => {
        const commentaryArray: ICommentary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const commentaryCollection: ICommentary[] = [sampleWithRequiredData];
        expectedResult = service.addCommentaryToCollectionIfMissing(commentaryCollection, ...commentaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commentary: ICommentary = sampleWithRequiredData;
        const commentary2: ICommentary = sampleWithPartialData;
        expectedResult = service.addCommentaryToCollectionIfMissing([], commentary, commentary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commentary);
        expect(expectedResult).toContain(commentary2);
      });

      it('should accept null and undefined values', () => {
        const commentary: ICommentary = sampleWithRequiredData;
        expectedResult = service.addCommentaryToCollectionIfMissing([], null, commentary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commentary);
      });

      it('should return initial array if no Commentary is added', () => {
        const commentaryCollection: ICommentary[] = [sampleWithRequiredData];
        expectedResult = service.addCommentaryToCollectionIfMissing(commentaryCollection, undefined, null);
        expect(expectedResult).toEqual(commentaryCollection);
      });
    });

    describe('compareCommentary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCommentary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCommentary(entity1, entity2);
        const compareResult2 = service.compareCommentary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCommentary(entity1, entity2);
        const compareResult2 = service.compareCommentary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCommentary(entity1, entity2);
        const compareResult2 = service.compareCommentary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
