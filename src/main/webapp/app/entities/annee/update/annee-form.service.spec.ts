import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../annee.test-samples';

import { AnneeFormService } from './annee-form.service';

describe('Annee Form Service', () => {
  let service: AnneeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnneeFormService);
  });

  describe('Service methods', () => {
    describe('createAnneeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAnneeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            an: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing IAnnee should create a new form with FormGroup', () => {
        const formGroup = service.createAnneeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            an: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getAnnee', () => {
      it('should return NewAnnee for default Annee initial value', () => {
        const formGroup = service.createAnneeFormGroup(sampleWithNewData);

        const annee = service.getAnnee(formGroup) as any;

        expect(annee).toMatchObject(sampleWithNewData);
      });

      it('should return NewAnnee for empty Annee initial value', () => {
        const formGroup = service.createAnneeFormGroup();

        const annee = service.getAnnee(formGroup) as any;

        expect(annee).toMatchObject({});
      });

      it('should return IAnnee', () => {
        const formGroup = service.createAnneeFormGroup(sampleWithRequiredData);

        const annee = service.getAnnee(formGroup) as any;

        expect(annee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAnnee should not enable id FormControl', () => {
        const formGroup = service.createAnneeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAnnee should disable id FormControl', () => {
        const formGroup = service.createAnneeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
