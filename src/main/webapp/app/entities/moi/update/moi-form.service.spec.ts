import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../moi.test-samples';

import { MoiFormService } from './moi-form.service';

describe('Moi Form Service', () => {
  let service: MoiFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MoiFormService);
  });

  describe('Service methods', () => {
    describe('createMoiFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMoiFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idmois: expect.any(Object),
            mois: expect.any(Object),
          }),
        );
      });

      it('passing IMoi should create a new form with FormGroup', () => {
        const formGroup = service.createMoiFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idmois: expect.any(Object),
            mois: expect.any(Object),
          }),
        );
      });
    });

    describe('getMoi', () => {
      it('should return NewMoi for default Moi initial value', () => {
        const formGroup = service.createMoiFormGroup(sampleWithNewData);

        const moi = service.getMoi(formGroup) as any;

        expect(moi).toMatchObject(sampleWithNewData);
      });

      it('should return NewMoi for empty Moi initial value', () => {
        const formGroup = service.createMoiFormGroup();

        const moi = service.getMoi(formGroup) as any;

        expect(moi).toMatchObject({});
      });

      it('should return IMoi', () => {
        const formGroup = service.createMoiFormGroup(sampleWithRequiredData);

        const moi = service.getMoi(formGroup) as any;

        expect(moi).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMoi should not enable id FormControl', () => {
        const formGroup = service.createMoiFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMoi should disable id FormControl', () => {
        const formGroup = service.createMoiFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
