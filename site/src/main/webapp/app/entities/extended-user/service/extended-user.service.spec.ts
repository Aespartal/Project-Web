import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExtendedUser } from '../extended-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../extended-user.test-samples';

import { ExtendedUserService } from './extended-user.service';

const requireRestSample: IExtendedUser = {
  ...sampleWithRequiredData,
};

describe('ExtendedUser Service', () => {
  let service: ExtendedUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IExtendedUser | IExtendedUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExtendedUserService);
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

    it('should create a ExtendedUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const extendedUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(extendedUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExtendedUser', () => {
      const extendedUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(extendedUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExtendedUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExtendedUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExtendedUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExtendedUserToCollectionIfMissing', () => {
      it('should add a ExtendedUser to an empty array', () => {
        const extendedUser: IExtendedUser = sampleWithRequiredData;
        expectedResult = service.addExtendedUserToCollectionIfMissing([], extendedUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extendedUser);
      });

      it('should not add a ExtendedUser to an array that contains it', () => {
        const extendedUser: IExtendedUser = sampleWithRequiredData;
        const extendedUserCollection: IExtendedUser[] = [
          {
            ...extendedUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExtendedUserToCollectionIfMissing(extendedUserCollection, extendedUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExtendedUser to an array that doesn't contain it", () => {
        const extendedUser: IExtendedUser = sampleWithRequiredData;
        const extendedUserCollection: IExtendedUser[] = [sampleWithPartialData];
        expectedResult = service.addExtendedUserToCollectionIfMissing(extendedUserCollection, extendedUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extendedUser);
      });

      it('should add only unique ExtendedUser to an array', () => {
        const extendedUserArray: IExtendedUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const extendedUserCollection: IExtendedUser[] = [sampleWithRequiredData];
        expectedResult = service.addExtendedUserToCollectionIfMissing(extendedUserCollection, ...extendedUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const extendedUser: IExtendedUser = sampleWithRequiredData;
        const extendedUser2: IExtendedUser = sampleWithPartialData;
        expectedResult = service.addExtendedUserToCollectionIfMissing([], extendedUser, extendedUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extendedUser);
        expect(expectedResult).toContain(extendedUser2);
      });

      it('should accept null and undefined values', () => {
        const extendedUser: IExtendedUser = sampleWithRequiredData;
        expectedResult = service.addExtendedUserToCollectionIfMissing([], null, extendedUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extendedUser);
      });

      it('should return initial array if no ExtendedUser is added', () => {
        const extendedUserCollection: IExtendedUser[] = [sampleWithRequiredData];
        expectedResult = service.addExtendedUserToCollectionIfMissing(extendedUserCollection, undefined, null);
        expect(expectedResult).toEqual(extendedUserCollection);
      });
    });

    describe('compareExtendedUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExtendedUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExtendedUser(entity1, entity2);
        const compareResult2 = service.compareExtendedUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExtendedUser(entity1, entity2);
        const compareResult2 = service.compareExtendedUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExtendedUser(entity1, entity2);
        const compareResult2 = service.compareExtendedUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
