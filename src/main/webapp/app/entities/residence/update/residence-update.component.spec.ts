import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProprietaire } from 'app/entities/proprietaire/proprietaire.model';
import { ProprietaireService } from 'app/entities/proprietaire/service/proprietaire.service';
import { ResidenceService } from '../service/residence.service';
import { IResidence } from '../residence.model';
import { ResidenceFormService } from './residence-form.service';

import { ResidenceUpdateComponent } from './residence-update.component';

describe('Residence Management Update Component', () => {
  let comp: ResidenceUpdateComponent;
  let fixture: ComponentFixture<ResidenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let residenceFormService: ResidenceFormService;
  let residenceService: ResidenceService;
  let proprietaireService: ProprietaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ResidenceUpdateComponent],
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
      .overrideTemplate(ResidenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResidenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    residenceFormService = TestBed.inject(ResidenceFormService);
    residenceService = TestBed.inject(ResidenceService);
    proprietaireService = TestBed.inject(ProprietaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Proprietaire query and add missing value', () => {
      const residence: IResidence = { id: 11519 };
      const proprietaire: IProprietaire = { id: 6967 };
      residence.proprietaire = proprietaire;

      const proprietaireCollection: IProprietaire[] = [{ id: 6967 }];
      jest.spyOn(proprietaireService, 'query').mockReturnValue(of(new HttpResponse({ body: proprietaireCollection })));
      const additionalProprietaires = [proprietaire];
      const expectedCollection: IProprietaire[] = [...additionalProprietaires, ...proprietaireCollection];
      jest.spyOn(proprietaireService, 'addProprietaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ residence });
      comp.ngOnInit();

      expect(proprietaireService.query).toHaveBeenCalled();
      expect(proprietaireService.addProprietaireToCollectionIfMissing).toHaveBeenCalledWith(
        proprietaireCollection,
        ...additionalProprietaires.map(expect.objectContaining),
      );
      expect(comp.proprietairesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const residence: IResidence = { id: 11519 };
      const proprietaire: IProprietaire = { id: 6967 };
      residence.proprietaire = proprietaire;

      activatedRoute.data = of({ residence });
      comp.ngOnInit();

      expect(comp.proprietairesSharedCollection).toContainEqual(proprietaire);
      expect(comp.residence).toEqual(residence);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResidence>>();
      const residence = { id: 8404 };
      jest.spyOn(residenceFormService, 'getResidence').mockReturnValue(residence);
      jest.spyOn(residenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ residence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: residence }));
      saveSubject.complete();

      // THEN
      expect(residenceFormService.getResidence).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(residenceService.update).toHaveBeenCalledWith(expect.objectContaining(residence));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResidence>>();
      const residence = { id: 8404 };
      jest.spyOn(residenceFormService, 'getResidence').mockReturnValue({ id: null });
      jest.spyOn(residenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ residence: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: residence }));
      saveSubject.complete();

      // THEN
      expect(residenceFormService.getResidence).toHaveBeenCalled();
      expect(residenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResidence>>();
      const residence = { id: 8404 };
      jest.spyOn(residenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ residence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(residenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProprietaire', () => {
      it('should forward to proprietaireService', () => {
        const entity = { id: 6967 };
        const entity2 = { id: 10022 };
        jest.spyOn(proprietaireService, 'compareProprietaire');
        comp.compareProprietaire(entity, entity2);
        expect(proprietaireService.compareProprietaire).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
