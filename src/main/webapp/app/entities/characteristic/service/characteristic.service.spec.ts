import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICharacteristic } from '../characteristic.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../characteristic.test-samples';

import { CharacteristicService } from './characteristic.service';

const requireRestSample: ICharacteristic = {
  ...sampleWithRequiredData,
};

describe('Characteristic Service', () => {
  let service: CharacteristicService;
  let httpMock: HttpTestingController;
  let expectedResult: ICharacteristic | ICharacteristic[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CharacteristicService);
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

    it('should create a Characteristic', () => {
      const characteristic = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(characteristic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Characteristic', () => {
      const characteristic = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(characteristic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Characteristic', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Characteristic', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Characteristic', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCharacteristicToCollectionIfMissing', () => {
      it('should add a Characteristic to an empty array', () => {
        const characteristic: ICharacteristic = sampleWithRequiredData;
        expectedResult = service.addCharacteristicToCollectionIfMissing([], characteristic);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characteristic);
      });

      it('should not add a Characteristic to an array that contains it', () => {
        const characteristic: ICharacteristic = sampleWithRequiredData;
        const characteristicCollection: ICharacteristic[] = [
          {
            ...characteristic,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCharacteristicToCollectionIfMissing(characteristicCollection, characteristic);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Characteristic to an array that doesn't contain it", () => {
        const characteristic: ICharacteristic = sampleWithRequiredData;
        const characteristicCollection: ICharacteristic[] = [sampleWithPartialData];
        expectedResult = service.addCharacteristicToCollectionIfMissing(characteristicCollection, characteristic);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characteristic);
      });

      it('should add only unique Characteristic to an array', () => {
        const characteristicArray: ICharacteristic[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const characteristicCollection: ICharacteristic[] = [sampleWithRequiredData];
        expectedResult = service.addCharacteristicToCollectionIfMissing(characteristicCollection, ...characteristicArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const characteristic: ICharacteristic = sampleWithRequiredData;
        const characteristic2: ICharacteristic = sampleWithPartialData;
        expectedResult = service.addCharacteristicToCollectionIfMissing([], characteristic, characteristic2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characteristic);
        expect(expectedResult).toContain(characteristic2);
      });

      it('should accept null and undefined values', () => {
        const characteristic: ICharacteristic = sampleWithRequiredData;
        expectedResult = service.addCharacteristicToCollectionIfMissing([], null, characteristic, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characteristic);
      });

      it('should return initial array if no Characteristic is added', () => {
        const characteristicCollection: ICharacteristic[] = [sampleWithRequiredData];
        expectedResult = service.addCharacteristicToCollectionIfMissing(characteristicCollection, undefined, null);
        expect(expectedResult).toEqual(characteristicCollection);
      });
    });

    describe('compareCharacteristic', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCharacteristic(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCharacteristic(entity1, entity2);
        const compareResult2 = service.compareCharacteristic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCharacteristic(entity1, entity2);
        const compareResult2 = service.compareCharacteristic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCharacteristic(entity1, entity2);
        const compareResult2 = service.compareCharacteristic(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
