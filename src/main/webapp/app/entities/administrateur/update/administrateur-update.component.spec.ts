import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AdministrateurService } from '../service/administrateur.service';
import { IAdministrateur } from '../administrateur.model';
import { AdministrateurFormService } from './administrateur-form.service';

import { AdministrateurUpdateComponent } from './administrateur-update.component';

describe('Administrateur Management Update Component', () => {
  let comp: AdministrateurUpdateComponent;
  let fixture: ComponentFixture<AdministrateurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let administrateurFormService: AdministrateurFormService;
  let administrateurService: AdministrateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdministrateurUpdateComponent],
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
      .overrideTemplate(AdministrateurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdministrateurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    administrateurFormService = TestBed.inject(AdministrateurFormService);
    administrateurService = TestBed.inject(AdministrateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const administrateur: IAdministrateur = { id: 26568 };

      activatedRoute.data = of({ administrateur });
      comp.ngOnInit();

      expect(comp.administrateur).toEqual(administrateur);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrateur>>();
      const administrateur = { id: 7232 };
      jest.spyOn(administrateurFormService, 'getAdministrateur').mockReturnValue(administrateur);
      jest.spyOn(administrateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: administrateur }));
      saveSubject.complete();

      // THEN
      expect(administrateurFormService.getAdministrateur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(administrateurService.update).toHaveBeenCalledWith(expect.objectContaining(administrateur));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrateur>>();
      const administrateur = { id: 7232 };
      jest.spyOn(administrateurFormService, 'getAdministrateur').mockReturnValue({ id: null });
      jest.spyOn(administrateurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrateur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: administrateur }));
      saveSubject.complete();

      // THEN
      expect(administrateurFormService.getAdministrateur).toHaveBeenCalled();
      expect(administrateurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrateur>>();
      const administrateur = { id: 7232 };
      jest.spyOn(administrateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(administrateurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
