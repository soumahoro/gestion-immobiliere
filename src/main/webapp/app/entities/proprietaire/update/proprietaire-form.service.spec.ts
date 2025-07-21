import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../proprietaire.test-samples';

import { ProprietaireFormService } from './proprietaire-form.service';

describe('Proprietaire Form Service', () => {
  let service: ProprietaireFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProprietaireFormService);
  });

  describe('Service methods', () => {
    describe('createProprietaireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProprietaireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idpro: expect.any(Object),
            date: expect.any(Object),
            nom: expect.any(Object),
            residence: expect.any(Object),
            tel: expect.any(Object),
          }),
        );
      });

      it('passing IProprietaire should create a new form with FormGroup', () => {
        const formGroup = service.createProprietaireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idpro: expect.any(Object),
            date: expect.any(Object),
            nom: expect.any(Object),
            residence: expect.any(Object),
            tel: expect.any(Object),
          }),
        );
      });
    });

    describe('getProprietaire', () => {
      it('should return NewProprietaire for default Proprietaire initial value', () => {
        const formGroup = service.createProprietaireFormGroup(sampleWithNewData);

        const proprietaire = service.getProprietaire(formGroup) as any;

        expect(proprietaire).toMatchObject(sampleWithNewData);
      });

      it('should return NewProprietaire for empty Proprietaire initial value', () => {
        const formGroup = service.createProprietaireFormGroup();

        const proprietaire = service.getProprietaire(formGroup) as any;

        expect(proprietaire).toMatchObject({});
      });

      it('should return IProprietaire', () => {
        const formGroup = service.createProprietaireFormGroup(sampleWithRequiredData);

        const proprietaire = service.getProprietaire(formGroup) as any;

        expect(proprietaire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProprietaire should not enable id FormControl', () => {
        const formGroup = service.createProprietaireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProprietaire should disable id FormControl', () => {
        const formGroup = service.createProprietaireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
