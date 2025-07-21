import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ProprietaireService } from '../service/proprietaire.service';
import { IProprietaire } from '../proprietaire.model';
import { ProprietaireFormService } from './proprietaire-form.service';

import { ProprietaireUpdateComponent } from './proprietaire-update.component';

describe('Proprietaire Management Update Component', () => {
  let comp: ProprietaireUpdateComponent;
  let fixture: ComponentFixture<ProprietaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let proprietaireFormService: ProprietaireFormService;
  let proprietaireService: ProprietaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProprietaireUpdateComponent],
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
      .overrideTemplate(ProprietaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProprietaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    proprietaireFormService = TestBed.inject(ProprietaireFormService);
    proprietaireService = TestBed.inject(ProprietaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const proprietaire: IProprietaire = { id: 10022 };

      activatedRoute.data = of({ proprietaire });
      comp.ngOnInit();

      expect(comp.proprietaire).toEqual(proprietaire);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProprietaire>>();
      const proprietaire = { id: 6967 };
      jest.spyOn(proprietaireFormService, 'getProprietaire').mockReturnValue(proprietaire);
      jest.spyOn(proprietaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proprietaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proprietaire }));
      saveSubject.complete();

      // THEN
      expect(proprietaireFormService.getProprietaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(proprietaireService.update).toHaveBeenCalledWith(expect.objectContaining(proprietaire));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProprietaire>>();
      const proprietaire = { id: 6967 };
      jest.spyOn(proprietaireFormService, 'getProprietaire').mockReturnValue({ id: null });
      jest.spyOn(proprietaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proprietaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proprietaire }));
      saveSubject.complete();

      // THEN
      expect(proprietaireFormService.getProprietaire).toHaveBeenCalled();
      expect(proprietaireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProprietaire>>();
      const proprietaire = { id: 6967 };
      jest.spyOn(proprietaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proprietaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(proprietaireService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
