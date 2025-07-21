import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMoi } from '../moi.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../moi.test-samples';

import { MoiService } from './moi.service';

const requireRestSample: IMoi = {
  ...sampleWithRequiredData,
};

describe('Moi Service', () => {
  let service: MoiService;
  let httpMock: HttpTestingController;
  let expectedResult: IMoi | IMoi[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MoiService);
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

    it('should create a Moi', () => {
      const moi = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(moi).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Moi', () => {
      const moi = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(moi).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Moi', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Moi', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Moi', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMoiToCollectionIfMissing', () => {
      it('should add a Moi to an empty array', () => {
        const moi: IMoi = sampleWithRequiredData;
        expectedResult = service.addMoiToCollectionIfMissing([], moi);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moi);
      });

      it('should not add a Moi to an array that contains it', () => {
        const moi: IMoi = sampleWithRequiredData;
        const moiCollection: IMoi[] = [
          {
            ...moi,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMoiToCollectionIfMissing(moiCollection, moi);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Moi to an array that doesn't contain it", () => {
        const moi: IMoi = sampleWithRequiredData;
        const moiCollection: IMoi[] = [sampleWithPartialData];
        expectedResult = service.addMoiToCollectionIfMissing(moiCollection, moi);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moi);
      });

      it('should add only unique Moi to an array', () => {
        const moiArray: IMoi[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const moiCollection: IMoi[] = [sampleWithRequiredData];
        expectedResult = service.addMoiToCollectionIfMissing(moiCollection, ...moiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moi: IMoi = sampleWithRequiredData;
        const moi2: IMoi = sampleWithPartialData;
        expectedResult = service.addMoiToCollectionIfMissing([], moi, moi2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moi);
        expect(expectedResult).toContain(moi2);
      });

      it('should accept null and undefined values', () => {
        const moi: IMoi = sampleWithRequiredData;
        expectedResult = service.addMoiToCollectionIfMissing([], null, moi, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moi);
      });

      it('should return initial array if no Moi is added', () => {
        const moiCollection: IMoi[] = [sampleWithRequiredData];
        expectedResult = service.addMoiToCollectionIfMissing(moiCollection, undefined, null);
        expect(expectedResult).toEqual(moiCollection);
      });
    });

    describe('compareMoi', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMoi(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25038 };
        const entity2 = null;

        const compareResult1 = service.compareMoi(entity1, entity2);
        const compareResult2 = service.compareMoi(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25038 };
        const entity2 = { id: 31934 };

        const compareResult1 = service.compareMoi(entity1, entity2);
        const compareResult2 = service.compareMoi(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25038 };
        const entity2 = { id: 25038 };

        const compareResult1 = service.compareMoi(entity1, entity2);
        const compareResult2 = service.compareMoi(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
