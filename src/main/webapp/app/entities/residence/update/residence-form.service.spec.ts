import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../residence.test-samples';

import { ResidenceFormService } from './residence-form.service';

describe('Residence Form Service', () => {
  let service: ResidenceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResidenceFormService);
  });

  describe('Service methods', () => {
    describe('createResidenceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResidenceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idres: expect.any(Object),
            ilot: expect.any(Object),
            localisation: expect.any(Object),
            observation: expect.any(Object),
            quartier: expect.any(Object),
            ville: expect.any(Object),
            proprietaire: expect.any(Object),
          }),
        );
      });

      it('passing IResidence should create a new form with FormGroup', () => {
        const formGroup = service.createResidenceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idres: expect.any(Object),
            ilot: expect.any(Object),
            localisation: expect.any(Object),
            observation: expect.any(Object),
            quartier: expect.any(Object),
            ville: expect.any(Object),
            proprietaire: expect.any(Object),
          }),
        );
      });
    });

    describe('getResidence', () => {
      it('should return NewResidence for default Residence initial value', () => {
        const formGroup = service.createResidenceFormGroup(sampleWithNewData);

        const residence = service.getResidence(formGroup) as any;

        expect(residence).toMatchObject(sampleWithNewData);
      });

      it('should return NewResidence for empty Residence initial value', () => {
        const formGroup = service.createResidenceFormGroup();

        const residence = service.getResidence(formGroup) as any;

        expect(residence).toMatchObject({});
      });

      it('should return IResidence', () => {
        const formGroup = service.createResidenceFormGroup(sampleWithRequiredData);

        const residence = service.getResidence(formGroup) as any;

        expect(residence).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResidence should not enable id FormControl', () => {
        const formGroup = service.createResidenceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResidence should disable id FormControl', () => {
        const formGroup = service.createResidenceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
