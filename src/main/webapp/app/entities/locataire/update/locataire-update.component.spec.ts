import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAppartement } from 'app/entities/appartement/appartement.model';
import { AppartementService } from 'app/entities/appartement/service/appartement.service';
import { LocataireService } from '../service/locataire.service';
import { ILocataire } from '../locataire.model';
import { LocataireFormService } from './locataire-form.service';

import { LocataireUpdateComponent } from './locataire-update.component';

describe('Locataire Management Update Component', () => {
  let comp: LocataireUpdateComponent;
  let fixture: ComponentFixture<LocataireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let locataireFormService: LocataireFormService;
  let locataireService: LocataireService;
  let appartementService: AppartementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LocataireUpdateComponent],
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
      .overrideTemplate(LocataireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocataireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    locataireFormService = TestBed.inject(LocataireFormService);
    locataireService = TestBed.inject(LocataireService);
    appartementService = TestBed.inject(AppartementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Appartement query and add missing value', () => {
      const locataire: ILocataire = { id: 24112 };
      const appartement: IAppartement = { id: 30619 };
      locataire.appartement = appartement;

      const appartementCollection: IAppartement[] = [{ id: 30619 }];
      jest.spyOn(appartementService, 'query').mockReturnValue(of(new HttpResponse({ body: appartementCollection })));
      const additionalAppartements = [appartement];
      const expectedCollection: IAppartement[] = [...additionalAppartements, ...appartementCollection];
      jest.spyOn(appartementService, 'addAppartementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ locataire });
      comp.ngOnInit();

      expect(appartementService.query).toHaveBeenCalled();
      expect(appartementService.addAppartementToCollectionIfMissing).toHaveBeenCalledWith(
        appartementCollection,
        ...additionalAppartements.map(expect.objectContaining),
      );
      expect(comp.appartementsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const locataire: ILocataire = { id: 24112 };
      const appartement: IAppartement = { id: 30619 };
      locataire.appartement = appartement;

      activatedRoute.data = of({ locataire });
      comp.ngOnInit();

      expect(comp.appartementsSharedCollection).toContainEqual(appartement);
      expect(comp.locataire).toEqual(locataire);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocataire>>();
      const locataire = { id: 3768 };
      jest.spyOn(locataireFormService, 'getLocataire').mockReturnValue(locataire);
      jest.spyOn(locataireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locataire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locataire }));
      saveSubject.complete();

      // THEN
      expect(locataireFormService.getLocataire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(locataireService.update).toHaveBeenCalledWith(expect.objectContaining(locataire));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocataire>>();
      const locataire = { id: 3768 };
      jest.spyOn(locataireFormService, 'getLocataire').mockReturnValue({ id: null });
      jest.spyOn(locataireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locataire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locataire }));
      saveSubject.complete();

      // THEN
      expect(locataireFormService.getLocataire).toHaveBeenCalled();
      expect(locataireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocataire>>();
      const locataire = { id: 3768 };
      jest.spyOn(locataireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locataire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(locataireService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppartement', () => {
      it('should forward to appartementService', () => {
        const entity = { id: 30619 };
        const entity2 = { id: 32698 };
        jest.spyOn(appartementService, 'compareAppartement');
        comp.compareAppartement(entity, entity2);
        expect(appartementService.compareAppartement).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
