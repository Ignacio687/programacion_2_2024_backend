import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExtra } from '../extra.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../extra.test-samples';

import { ExtraService } from './extra.service';

const requireRestSample: IExtra = {
  ...sampleWithRequiredData,
};

describe('Extra Service', () => {
  let service: ExtraService;
  let httpMock: HttpTestingController;
  let expectedResult: IExtra | IExtra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExtraService);
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

    it('should create a Extra', () => {
      const extra = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(extra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Extra', () => {
      const extra = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(extra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Extra', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Extra', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Extra', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExtraToCollectionIfMissing', () => {
      it('should add a Extra to an empty array', () => {
        const extra: IExtra = sampleWithRequiredData;
        expectedResult = service.addExtraToCollectionIfMissing([], extra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extra);
      });

      it('should not add a Extra to an array that contains it', () => {
        const extra: IExtra = sampleWithRequiredData;
        const extraCollection: IExtra[] = [
          {
            ...extra,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExtraToCollectionIfMissing(extraCollection, extra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Extra to an array that doesn't contain it", () => {
        const extra: IExtra = sampleWithRequiredData;
        const extraCollection: IExtra[] = [sampleWithPartialData];
        expectedResult = service.addExtraToCollectionIfMissing(extraCollection, extra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extra);
      });

      it('should add only unique Extra to an array', () => {
        const extraArray: IExtra[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const extraCollection: IExtra[] = [sampleWithRequiredData];
        expectedResult = service.addExtraToCollectionIfMissing(extraCollection, ...extraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const extra: IExtra = sampleWithRequiredData;
        const extra2: IExtra = sampleWithPartialData;
        expectedResult = service.addExtraToCollectionIfMissing([], extra, extra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extra);
        expect(expectedResult).toContain(extra2);
      });

      it('should accept null and undefined values', () => {
        const extra: IExtra = sampleWithRequiredData;
        expectedResult = service.addExtraToCollectionIfMissing([], null, extra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extra);
      });

      it('should return initial array if no Extra is added', () => {
        const extraCollection: IExtra[] = [sampleWithRequiredData];
        expectedResult = service.addExtraToCollectionIfMissing(extraCollection, undefined, null);
        expect(expectedResult).toEqual(extraCollection);
      });
    });

    describe('compareExtra', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExtra(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExtra(entity1, entity2);
        const compareResult2 = service.compareExtra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExtra(entity1, entity2);
        const compareResult2 = service.compareExtra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExtra(entity1, entity2);
        const compareResult2 = service.compareExtra(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
