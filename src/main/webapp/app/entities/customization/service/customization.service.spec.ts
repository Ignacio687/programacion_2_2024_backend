import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICustomization } from '../customization.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../customization.test-samples';

import { CustomizationService } from './customization.service';

const requireRestSample: ICustomization = {
  ...sampleWithRequiredData,
};

describe('Customization Service', () => {
  let service: CustomizationService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomization | ICustomization[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustomizationService);
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

    it('should create a Customization', () => {
      const customization = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customization).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Customization', () => {
      const customization = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customization).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Customization', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Customization', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Customization', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomizationToCollectionIfMissing', () => {
      it('should add a Customization to an empty array', () => {
        const customization: ICustomization = sampleWithRequiredData;
        expectedResult = service.addCustomizationToCollectionIfMissing([], customization);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customization);
      });

      it('should not add a Customization to an array that contains it', () => {
        const customization: ICustomization = sampleWithRequiredData;
        const customizationCollection: ICustomization[] = [
          {
            ...customization,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomizationToCollectionIfMissing(customizationCollection, customization);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Customization to an array that doesn't contain it", () => {
        const customization: ICustomization = sampleWithRequiredData;
        const customizationCollection: ICustomization[] = [sampleWithPartialData];
        expectedResult = service.addCustomizationToCollectionIfMissing(customizationCollection, customization);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customization);
      });

      it('should add only unique Customization to an array', () => {
        const customizationArray: ICustomization[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customizationCollection: ICustomization[] = [sampleWithRequiredData];
        expectedResult = service.addCustomizationToCollectionIfMissing(customizationCollection, ...customizationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customization: ICustomization = sampleWithRequiredData;
        const customization2: ICustomization = sampleWithPartialData;
        expectedResult = service.addCustomizationToCollectionIfMissing([], customization, customization2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customization);
        expect(expectedResult).toContain(customization2);
      });

      it('should accept null and undefined values', () => {
        const customization: ICustomization = sampleWithRequiredData;
        expectedResult = service.addCustomizationToCollectionIfMissing([], null, customization, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customization);
      });

      it('should return initial array if no Customization is added', () => {
        const customizationCollection: ICustomization[] = [sampleWithRequiredData];
        expectedResult = service.addCustomizationToCollectionIfMissing(customizationCollection, undefined, null);
        expect(expectedResult).toEqual(customizationCollection);
      });
    });

    describe('compareCustomization', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomization(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCustomization(entity1, entity2);
        const compareResult2 = service.compareCustomization(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCustomization(entity1, entity2);
        const compareResult2 = service.compareCustomization(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCustomization(entity1, entity2);
        const compareResult2 = service.compareCustomization(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
