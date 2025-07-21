import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAppartement } from '../appartement.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../appartement.test-samples';

import { AppartementService } from './appartement.service';

const requireRestSample: IAppartement = {
  ...sampleWithRequiredData,
};

describe('Appartement Service', () => {
  let service: AppartementService;
  let httpMock: HttpTestingController;
  let expectedResult: IAppartement | IAppartement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AppartementService);
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

    it('should create a Appartement', () => {
      const appartement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(appartement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Appartement', () => {
      const appartement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(appartement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Appartement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Appartement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Appartement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAppartementToCollectionIfMissing', () => {
      it('should add a Appartement to an empty array', () => {
        const appartement: IAppartement = sampleWithRequiredData;
        expectedResult = service.addAppartementToCollectionIfMissing([], appartement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appartement);
      });

      it('should not add a Appartement to an array that contains it', () => {
        const appartement: IAppartement = sampleWithRequiredData;
        const appartementCollection: IAppartement[] = [
          {
            ...appartement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAppartementToCollectionIfMissing(appartementCollection, appartement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Appartement to an array that doesn't contain it", () => {
        const appartement: IAppartement = sampleWithRequiredData;
        const appartementCollection: IAppartement[] = [sampleWithPartialData];
        expectedResult = service.addAppartementToCollectionIfMissing(appartementCollection, appartement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appartement);
      });

      it('should add only unique Appartement to an array', () => {
        const appartementArray: IAppartement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const appartementCollection: IAppartement[] = [sampleWithRequiredData];
        expectedResult = service.addAppartementToCollectionIfMissing(appartementCollection, ...appartementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appartement: IAppartement = sampleWithRequiredData;
        const appartement2: IAppartement = sampleWithPartialData;
        expectedResult = service.addAppartementToCollectionIfMissing([], appartement, appartement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appartement);
        expect(expectedResult).toContain(appartement2);
      });

      it('should accept null and undefined values', () => {
        const appartement: IAppartement = sampleWithRequiredData;
        expectedResult = service.addAppartementToCollectionIfMissing([], null, appartement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appartement);
      });

      it('should return initial array if no Appartement is added', () => {
        const appartementCollection: IAppartement[] = [sampleWithRequiredData];
        expectedResult = service.addAppartementToCollectionIfMissing(appartementCollection, undefined, null);
        expect(expectedResult).toEqual(appartementCollection);
      });
    });

    describe('compareAppartement', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAppartement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30619 };
        const entity2 = null;

        const compareResult1 = service.compareAppartement(entity1, entity2);
        const compareResult2 = service.compareAppartement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30619 };
        const entity2 = { id: 32698 };

        const compareResult1 = service.compareAppartement(entity1, entity2);
        const compareResult2 = service.compareAppartement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 30619 };
        const entity2 = { id: 30619 };

        const compareResult1 = service.compareAppartement(entity1, entity2);
        const compareResult2 = service.compareAppartement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
