import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAdministrateur } from '../administrateur.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../administrateur.test-samples';

import { AdministrateurService } from './administrateur.service';

const requireRestSample: IAdministrateur = {
  ...sampleWithRequiredData,
};

describe('Administrateur Service', () => {
  let service: AdministrateurService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdministrateur | IAdministrateur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AdministrateurService);
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

    it('should create a Administrateur', () => {
      const administrateur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(administrateur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Administrateur', () => {
      const administrateur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(administrateur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Administrateur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Administrateur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Administrateur', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdministrateurToCollectionIfMissing', () => {
      it('should add a Administrateur to an empty array', () => {
        const administrateur: IAdministrateur = sampleWithRequiredData;
        expectedResult = service.addAdministrateurToCollectionIfMissing([], administrateur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(administrateur);
      });

      it('should not add a Administrateur to an array that contains it', () => {
        const administrateur: IAdministrateur = sampleWithRequiredData;
        const administrateurCollection: IAdministrateur[] = [
          {
            ...administrateur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdministrateurToCollectionIfMissing(administrateurCollection, administrateur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Administrateur to an array that doesn't contain it", () => {
        const administrateur: IAdministrateur = sampleWithRequiredData;
        const administrateurCollection: IAdministrateur[] = [sampleWithPartialData];
        expectedResult = service.addAdministrateurToCollectionIfMissing(administrateurCollection, administrateur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(administrateur);
      });

      it('should add only unique Administrateur to an array', () => {
        const administrateurArray: IAdministrateur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const administrateurCollection: IAdministrateur[] = [sampleWithRequiredData];
        expectedResult = service.addAdministrateurToCollectionIfMissing(administrateurCollection, ...administrateurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const administrateur: IAdministrateur = sampleWithRequiredData;
        const administrateur2: IAdministrateur = sampleWithPartialData;
        expectedResult = service.addAdministrateurToCollectionIfMissing([], administrateur, administrateur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(administrateur);
        expect(expectedResult).toContain(administrateur2);
      });

      it('should accept null and undefined values', () => {
        const administrateur: IAdministrateur = sampleWithRequiredData;
        expectedResult = service.addAdministrateurToCollectionIfMissing([], null, administrateur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(administrateur);
      });

      it('should return initial array if no Administrateur is added', () => {
        const administrateurCollection: IAdministrateur[] = [sampleWithRequiredData];
        expectedResult = service.addAdministrateurToCollectionIfMissing(administrateurCollection, undefined, null);
        expect(expectedResult).toEqual(administrateurCollection);
      });
    });

    describe('compareAdministrateur', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdministrateur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7232 };
        const entity2 = null;

        const compareResult1 = service.compareAdministrateur(entity1, entity2);
        const compareResult2 = service.compareAdministrateur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7232 };
        const entity2 = { id: 26568 };

        const compareResult1 = service.compareAdministrateur(entity1, entity2);
        const compareResult2 = service.compareAdministrateur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7232 };
        const entity2 = { id: 7232 };

        const compareResult1 = service.compareAdministrateur(entity1, entity2);
        const compareResult2 = service.compareAdministrateur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
