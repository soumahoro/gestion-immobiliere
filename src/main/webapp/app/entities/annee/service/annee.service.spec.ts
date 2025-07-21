import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAnnee } from '../annee.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../annee.test-samples';

import { AnneeService } from './annee.service';

const requireRestSample: IAnnee = {
  ...sampleWithRequiredData,
};

describe('Annee Service', () => {
  let service: AnneeService;
  let httpMock: HttpTestingController;
  let expectedResult: IAnnee | IAnnee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AnneeService);
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

    it('should create a Annee', () => {
      const annee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(annee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Annee', () => {
      const annee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(annee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Annee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Annee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Annee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAnneeToCollectionIfMissing', () => {
      it('should add a Annee to an empty array', () => {
        const annee: IAnnee = sampleWithRequiredData;
        expectedResult = service.addAnneeToCollectionIfMissing([], annee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(annee);
      });

      it('should not add a Annee to an array that contains it', () => {
        const annee: IAnnee = sampleWithRequiredData;
        const anneeCollection: IAnnee[] = [
          {
            ...annee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAnneeToCollectionIfMissing(anneeCollection, annee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Annee to an array that doesn't contain it", () => {
        const annee: IAnnee = sampleWithRequiredData;
        const anneeCollection: IAnnee[] = [sampleWithPartialData];
        expectedResult = service.addAnneeToCollectionIfMissing(anneeCollection, annee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(annee);
      });

      it('should add only unique Annee to an array', () => {
        const anneeArray: IAnnee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const anneeCollection: IAnnee[] = [sampleWithRequiredData];
        expectedResult = service.addAnneeToCollectionIfMissing(anneeCollection, ...anneeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const annee: IAnnee = sampleWithRequiredData;
        const annee2: IAnnee = sampleWithPartialData;
        expectedResult = service.addAnneeToCollectionIfMissing([], annee, annee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(annee);
        expect(expectedResult).toContain(annee2);
      });

      it('should accept null and undefined values', () => {
        const annee: IAnnee = sampleWithRequiredData;
        expectedResult = service.addAnneeToCollectionIfMissing([], null, annee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(annee);
      });

      it('should return initial array if no Annee is added', () => {
        const anneeCollection: IAnnee[] = [sampleWithRequiredData];
        expectedResult = service.addAnneeToCollectionIfMissing(anneeCollection, undefined, null);
        expect(expectedResult).toEqual(anneeCollection);
      });
    });

    describe('compareAnnee', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAnnee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21078 };
        const entity2 = null;

        const compareResult1 = service.compareAnnee(entity1, entity2);
        const compareResult2 = service.compareAnnee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21078 };
        const entity2 = { id: 20876 };

        const compareResult1 = service.compareAnnee(entity1, entity2);
        const compareResult2 = service.compareAnnee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 21078 };
        const entity2 = { id: 21078 };

        const compareResult1 = service.compareAnnee(entity1, entity2);
        const compareResult2 = service.compareAnnee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
