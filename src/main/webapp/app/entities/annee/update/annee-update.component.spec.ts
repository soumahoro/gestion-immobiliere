import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AnneeService } from '../service/annee.service';
import { IAnnee } from '../annee.model';
import { AnneeFormService } from './annee-form.service';

import { AnneeUpdateComponent } from './annee-update.component';

describe('Annee Management Update Component', () => {
  let comp: AnneeUpdateComponent;
  let fixture: ComponentFixture<AnneeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let anneeFormService: AnneeFormService;
  let anneeService: AnneeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AnneeUpdateComponent],
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
      .overrideTemplate(AnneeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnneeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    anneeFormService = TestBed.inject(AnneeFormService);
    anneeService = TestBed.inject(AnneeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const annee: IAnnee = { id: 20876 };

      activatedRoute.data = of({ annee });
      comp.ngOnInit();

      expect(comp.annee).toEqual(annee);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnee>>();
      const annee = { id: 21078 };
      jest.spyOn(anneeFormService, 'getAnnee').mockReturnValue(annee);
      jest.spyOn(anneeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annee }));
      saveSubject.complete();

      // THEN
      expect(anneeFormService.getAnnee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(anneeService.update).toHaveBeenCalledWith(expect.objectContaining(annee));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnee>>();
      const annee = { id: 21078 };
      jest.spyOn(anneeFormService, 'getAnnee').mockReturnValue({ id: null });
      jest.spyOn(anneeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annee }));
      saveSubject.complete();

      // THEN
      expect(anneeFormService.getAnnee).toHaveBeenCalled();
      expect(anneeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnee>>();
      const annee = { id: 21078 };
      jest.spyOn(anneeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(anneeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
