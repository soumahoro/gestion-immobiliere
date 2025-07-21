import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MoiService } from '../service/moi.service';
import { IMoi } from '../moi.model';
import { MoiFormService } from './moi-form.service';

import { MoiUpdateComponent } from './moi-update.component';

describe('Moi Management Update Component', () => {
  let comp: MoiUpdateComponent;
  let fixture: ComponentFixture<MoiUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moiFormService: MoiFormService;
  let moiService: MoiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MoiUpdateComponent],
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
      .overrideTemplate(MoiUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoiUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moiFormService = TestBed.inject(MoiFormService);
    moiService = TestBed.inject(MoiService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const moi: IMoi = { id: 31934 };

      activatedRoute.data = of({ moi });
      comp.ngOnInit();

      expect(comp.moi).toEqual(moi);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoi>>();
      const moi = { id: 25038 };
      jest.spyOn(moiFormService, 'getMoi').mockReturnValue(moi);
      jest.spyOn(moiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moi }));
      saveSubject.complete();

      // THEN
      expect(moiFormService.getMoi).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moiService.update).toHaveBeenCalledWith(expect.objectContaining(moi));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoi>>();
      const moi = { id: 25038 };
      jest.spyOn(moiFormService, 'getMoi').mockReturnValue({ id: null });
      jest.spyOn(moiService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moi: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moi }));
      saveSubject.complete();

      // THEN
      expect(moiFormService.getMoi).toHaveBeenCalled();
      expect(moiService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoi>>();
      const moi = { id: 25038 };
      jest.spyOn(moiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moiService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
