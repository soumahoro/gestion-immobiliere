import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../appartement.test-samples';

import { AppartementFormService } from './appartement-form.service';

describe('Appartement Form Service', () => {
  let service: AppartementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppartementFormService);
  });

  describe('Service methods', () => {
    describe('createAppartementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAppartementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idapp: expect.any(Object),
            libelle: expect.any(Object),
            loyer: expect.any(Object),
            nbrepieces: expect.any(Object),
            taux: expect.any(Object),
            residence: expect.any(Object),
          }),
        );
      });

      it('passing IAppartement should create a new form with FormGroup', () => {
        const formGroup = service.createAppartementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idapp: expect.any(Object),
            libelle: expect.any(Object),
            loyer: expect.any(Object),
            nbrepieces: expect.any(Object),
            taux: expect.any(Object),
            residence: expect.any(Object),
          }),
        );
      });
    });

    describe('getAppartement', () => {
      it('should return NewAppartement for default Appartement initial value', () => {
        const formGroup = service.createAppartementFormGroup(sampleWithNewData);

        const appartement = service.getAppartement(formGroup) as any;

        expect(appartement).toMatchObject(sampleWithNewData);
      });

      it('should return NewAppartement for empty Appartement initial value', () => {
        const formGroup = service.createAppartementFormGroup();

        const appartement = service.getAppartement(formGroup) as any;

        expect(appartement).toMatchObject({});
      });

      it('should return IAppartement', () => {
        const formGroup = service.createAppartementFormGroup(sampleWithRequiredData);

        const appartement = service.getAppartement(formGroup) as any;

        expect(appartement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAppartement should not enable id FormControl', () => {
        const formGroup = service.createAppartementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAppartement should disable id FormControl', () => {
        const formGroup = service.createAppartementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
