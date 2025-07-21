import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IResidence } from 'app/entities/residence/residence.model';
import { ResidenceService } from 'app/entities/residence/service/residence.service';
import { AppartementService } from '../service/appartement.service';
import { IAppartement } from '../appartement.model';
import { AppartementFormService } from './appartement-form.service';

import { AppartementUpdateComponent } from './appartement-update.component';

describe('Appartement Management Update Component', () => {
  let comp: AppartementUpdateComponent;
  let fixture: ComponentFixture<AppartementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appartementFormService: AppartementFormService;
  let appartementService: AppartementService;
  let residenceService: ResidenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AppartementUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AppartementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppartementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appartementFormService = TestBed.inject(AppartementFormService);
    appartementService = TestBed.inject(AppartementService);
    residenceService = TestBed.inject(ResidenceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Residence query and add missing value', () => {
      const appartement: IAppartement = { id: 32698 };
      const residence: IResidence = { id: 8404 };
      appartement.residence = residence;

      const residenceCollection: IResidence[] = [{ id: 8404 }];
      jest.spyOn(residenceService, 'query').mockReturnValue(of(new HttpResponse({ body: residenceCollection })));
      const additionalResidences = [residence];
      const expectedCollection: IResidence[] = [...additionalResidences, ...residenceCollection];
      jest.spyOn(residenceService, 'addResidenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appartement });
      comp.ngOnInit();

      expect(residenceService.query).toHaveBeenCalled();
      expect(residenceService.addResidenceToCollectionIfMissing).toHaveBeenCalledWith(
        residenceCollection,
        ...additionalResidences.map(expect.objectContaining),
      );
      expect(comp.residencesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const appartement: IAppartement = { id: 32698 };
      const residence: IResidence = { id: 8404 };
      appartement.residence = residence;

      activatedRoute.data = of({ appartement });
      comp.ngOnInit();

      expect(comp.residencesSharedCollection).toContainEqual(residence);
      expect(comp.appartement).toEqual(appartement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppartement>>();
      const appartement = { id: 30619 };
      jest.spyOn(appartementFormService, 'getAppartement').mockReturnValue(appartement);
      jest.spyOn(appartementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appartement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appartement }));
      saveSubject.complete();

      // THEN
      expect(appartementFormService.getAppartement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appartementService.update).toHaveBeenCalledWith(expect.objectContaining(appartement));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppartement>>();
      const appartement = { id: 30619 };
      jest.spyOn(appartementFormService, 'getAppartement').mockReturnValue({ id: null });
      jest.spyOn(appartementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appartement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appartement }));
      saveSubject.complete();

      // THEN
      expect(appartementFormService.getAppartement).toHaveBeenCalled();
      expect(appartementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppartement>>();
      const appartement = { id: 30619 };
      jest.spyOn(appartementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appartement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appartementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareResidence', () => {
      it('should forward to residenceService', () => {
        const entity = { id: 8404 };
        const entity2 = { id: 11519 };
        jest.spyOn(residenceService, 'compareResidence');
        comp.compareResidence(entity, entity2);
        expect(residenceService.compareResidence).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
