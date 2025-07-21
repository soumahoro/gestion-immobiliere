import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReglement } from '../reglement.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../reglement.test-samples';

import { ReglementService, RestReglement } from './reglement.service';

const requireRestSample: RestReglement = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
};

describe('Reglement Service', () => {
  let service: ReglementService;
  let httpMock: HttpTestingController;
  let expectedResult: IReglement | IReglement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReglementService);
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

    it('should create a Reglement', () => {
      const reglement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reglement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Reglement', () => {
      const reglement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reglement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Reglement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Reglement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Reglement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReglementToCollectionIfMissing', () => {
      it('should add a Reglement to an empty array', () => {
        const reglement: IReglement = sampleWithRequiredData;
        expectedResult = service.addReglementToCollectionIfMissing([], reglement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reglement);
      });

      it('should not add a Reglement to an array that contains it', () => {
        const reglement: IReglement = sampleWithRequiredData;
        const reglementCollection: IReglement[] = [
          {
            ...reglement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReglementToCollectionIfMissing(reglementCollection, reglement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Reglement to an array that doesn't contain it", () => {
        const reglement: IReglement = sampleWithRequiredData;
        const reglementCollection: IReglement[] = [sampleWithPartialData];
        expectedResult = service.addReglementToCollectionIfMissing(reglementCollection, reglement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reglement);
      });

      it('should add only unique Reglement to an array', () => {
        const reglementArray: IReglement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reglementCollection: IReglement[] = [sampleWithRequiredData];
        expectedResult = service.addReglementToCollectionIfMissing(reglementCollection, ...reglementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reglement: IReglement = sampleWithRequiredData;
        const reglement2: IReglement = sampleWithPartialData;
        expectedResult = service.addReglementToCollectionIfMissing([], reglement, reglement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reglement);
        expect(expectedResult).toContain(reglement2);
      });

      it('should accept null and undefined values', () => {
        const reglement: IReglement = sampleWithRequiredData;
        expectedResult = service.addReglementToCollectionIfMissing([], null, reglement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reglement);
      });

      it('should return initial array if no Reglement is added', () => {
        const reglementCollection: IReglement[] = [sampleWithRequiredData];
        expectedResult = service.addReglementToCollectionIfMissing(reglementCollection, undefined, null);
        expect(expectedResult).toEqual(reglementCollection);
      });
    });

    describe('compareReglement', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReglement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13334 };
        const entity2 = null;

        const compareResult1 = service.compareReglement(entity1, entity2);
        const compareResult2 = service.compareReglement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13334 };
        const entity2 = { id: 14463 };

        const compareResult1 = service.compareReglement(entity1, entity2);
        const compareResult2 = service.compareReglement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13334 };
        const entity2 = { id: 13334 };

        const compareResult1 = service.compareReglement(entity1, entity2);
        const compareResult2 = service.compareReglement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
