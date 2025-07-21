import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reglement.test-samples';

import { ReglementFormService } from './reglement-form.service';

describe('Reglement Form Service', () => {
  let service: ReglementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReglementFormService);
  });

  describe('Service methods', () => {
    describe('createReglementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReglementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idreg: expect.any(Object),
            annee: expect.any(Object),
            date: expect.any(Object),
            montant: expect.any(Object),
            montantlettres: expect.any(Object),
            motif: expect.any(Object),
            observ1: expect.any(Object),
            observ2: expect.any(Object),
            observ3: expect.any(Object),
            reste: expect.any(Object),
            locataire: expect.any(Object),
            moi: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });

      it('passing IReglement should create a new form with FormGroup', () => {
        const formGroup = service.createReglementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idreg: expect.any(Object),
            annee: expect.any(Object),
            date: expect.any(Object),
            montant: expect.any(Object),
            montantlettres: expect.any(Object),
            motif: expect.any(Object),
            observ1: expect.any(Object),
            observ2: expect.any(Object),
            observ3: expect.any(Object),
            reste: expect.any(Object),
            locataire: expect.any(Object),
            moi: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getReglement', () => {
      it('should return NewReglement for default Reglement initial value', () => {
        const formGroup = service.createReglementFormGroup(sampleWithNewData);

        const reglement = service.getReglement(formGroup) as any;

        expect(reglement).toMatchObject(sampleWithNewData);
      });

      it('should return NewReglement for empty Reglement initial value', () => {
        const formGroup = service.createReglementFormGroup();

        const reglement = service.getReglement(formGroup) as any;

        expect(reglement).toMatchObject({});
      });

      it('should return IReglement', () => {
        const formGroup = service.createReglementFormGroup(sampleWithRequiredData);

        const reglement = service.getReglement(formGroup) as any;

        expect(reglement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReglement should not enable id FormControl', () => {
        const formGroup = service.createReglementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReglement should disable id FormControl', () => {
        const formGroup = service.createReglementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
