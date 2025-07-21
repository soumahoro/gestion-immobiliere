import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../locataire.test-samples';

import { LocataireFormService } from './locataire-form.service';

describe('Locataire Form Service', () => {
  let service: LocataireFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocataireFormService);
  });

  describe('Service methods', () => {
    describe('createLocataireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLocataireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idloc: expect.any(Object),
            arriere: expect.any(Object),
            date: expect.any(Object),
            datedepart: expect.any(Object),
            fonction: expect.any(Object),
            loyer: expect.any(Object),
            motifdepart: expect.any(Object),
            nationalite: expect.any(Object),
            nom: expect.any(Object),
            numpiece: expect.any(Object),
            observation: expect.any(Object),
            statut: expect.any(Object),
            telephone: expect.any(Object),
            typepiece: expect.any(Object),
            appartement: expect.any(Object),
          }),
        );
      });

      it('passing ILocataire should create a new form with FormGroup', () => {
        const formGroup = service.createLocataireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idloc: expect.any(Object),
            arriere: expect.any(Object),
            date: expect.any(Object),
            datedepart: expect.any(Object),
            fonction: expect.any(Object),
            loyer: expect.any(Object),
            motifdepart: expect.any(Object),
            nationalite: expect.any(Object),
            nom: expect.any(Object),
            numpiece: expect.any(Object),
            observation: expect.any(Object),
            statut: expect.any(Object),
            telephone: expect.any(Object),
            typepiece: expect.any(Object),
            appartement: expect.any(Object),
          }),
        );
      });
    });

    describe('getLocataire', () => {
      it('should return NewLocataire for default Locataire initial value', () => {
        const formGroup = service.createLocataireFormGroup(sampleWithNewData);

        const locataire = service.getLocataire(formGroup) as any;

        expect(locataire).toMatchObject(sampleWithNewData);
      });

      it('should return NewLocataire for empty Locataire initial value', () => {
        const formGroup = service.createLocataireFormGroup();

        const locataire = service.getLocataire(formGroup) as any;

        expect(locataire).toMatchObject({});
      });

      it('should return ILocataire', () => {
        const formGroup = service.createLocataireFormGroup(sampleWithRequiredData);

        const locataire = service.getLocataire(formGroup) as any;

        expect(locataire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILocataire should not enable id FormControl', () => {
        const formGroup = service.createLocataireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLocataire should disable id FormControl', () => {
        const formGroup = service.createLocataireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
