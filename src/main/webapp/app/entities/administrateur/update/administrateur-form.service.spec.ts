import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../administrateur.test-samples';

import { AdministrateurFormService } from './administrateur-form.service';

describe('Administrateur Form Service', () => {
  let service: AdministrateurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdministrateurFormService);
  });

  describe('Service methods', () => {
    describe('createAdministrateurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdministrateurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
          }),
        );
      });

      it('passing IAdministrateur should create a new form with FormGroup', () => {
        const formGroup = service.createAdministrateurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
          }),
        );
      });
    });

    describe('getAdministrateur', () => {
      it('should return NewAdministrateur for default Administrateur initial value', () => {
        const formGroup = service.createAdministrateurFormGroup(sampleWithNewData);

        const administrateur = service.getAdministrateur(formGroup) as any;

        expect(administrateur).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdministrateur for empty Administrateur initial value', () => {
        const formGroup = service.createAdministrateurFormGroup();

        const administrateur = service.getAdministrateur(formGroup) as any;

        expect(administrateur).toMatchObject({});
      });

      it('should return IAdministrateur', () => {
        const formGroup = service.createAdministrateurFormGroup(sampleWithRequiredData);

        const administrateur = service.getAdministrateur(formGroup) as any;

        expect(administrateur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdministrateur should not enable id FormControl', () => {
        const formGroup = service.createAdministrateurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdministrateur should disable id FormControl', () => {
        const formGroup = service.createAdministrateurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
