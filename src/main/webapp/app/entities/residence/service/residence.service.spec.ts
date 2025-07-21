import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IResidence } from '../residence.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../residence.test-samples';

import { ResidenceService } from './residence.service';

const requireRestSample: IResidence = {
  ...sampleWithRequiredData,
};

describe('Residence Service', () => {
  let service: ResidenceService;
  let httpMock: HttpTestingController;
  let expectedResult: IResidence | IResidence[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ResidenceService);
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

    it('should create a Residence', () => {
      const residence = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(residence).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Residence', () => {
      const residence = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(residence).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Residence', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Residence', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Residence', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addResidenceToCollectionIfMissing', () => {
      it('should add a Residence to an empty array', () => {
        const residence: IResidence = sampleWithRequiredData;
        expectedResult = service.addResidenceToCollectionIfMissing([], residence);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(residence);
      });

      it('should not add a Residence to an array that contains it', () => {
        const residence: IResidence = sampleWithRequiredData;
        const residenceCollection: IResidence[] = [
          {
            ...residence,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addResidenceToCollectionIfMissing(residenceCollection, residence);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Residence to an array that doesn't contain it", () => {
        const residence: IResidence = sampleWithRequiredData;
        const residenceCollection: IResidence[] = [sampleWithPartialData];
        expectedResult = service.addResidenceToCollectionIfMissing(residenceCollection, residence);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(residence);
      });

      it('should add only unique Residence to an array', () => {
        const residenceArray: IResidence[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const residenceCollection: IResidence[] = [sampleWithRequiredData];
        expectedResult = service.addResidenceToCollectionIfMissing(residenceCollection, ...residenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const residence: IResidence = sampleWithRequiredData;
        const residence2: IResidence = sampleWithPartialData;
        expectedResult = service.addResidenceToCollectionIfMissing([], residence, residence2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(residence);
        expect(expectedResult).toContain(residence2);
      });

      it('should accept null and undefined values', () => {
        const residence: IResidence = sampleWithRequiredData;
        expectedResult = service.addResidenceToCollectionIfMissing([], null, residence, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(residence);
      });

      it('should return initial array if no Residence is added', () => {
        const residenceCollection: IResidence[] = [sampleWithRequiredData];
        expectedResult = service.addResidenceToCollectionIfMissing(residenceCollection, undefined, null);
        expect(expectedResult).toEqual(residenceCollection);
      });
    });

    describe('compareResidence', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareResidence(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8404 };
        const entity2 = null;

        const compareResult1 = service.compareResidence(entity1, entity2);
        const compareResult2 = service.compareResidence(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8404 };
        const entity2 = { id: 11519 };

        const compareResult1 = service.compareResidence(entity1, entity2);
        const compareResult2 = service.compareResidence(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 8404 };
        const entity2 = { id: 8404 };

        const compareResult1 = service.compareResidence(entity1, entity2);
        const compareResult2 = service.compareResidence(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
