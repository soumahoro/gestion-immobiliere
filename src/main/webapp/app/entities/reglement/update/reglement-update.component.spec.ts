import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ILocataire } from 'app/entities/locataire/locataire.model';
import { LocataireService } from 'app/entities/locataire/service/locataire.service';
import { IMoi } from 'app/entities/moi/moi.model';
import { MoiService } from 'app/entities/moi/service/moi.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IReglement } from '../reglement.model';
import { ReglementService } from '../service/reglement.service';
import { ReglementFormService } from './reglement-form.service';

import { ReglementUpdateComponent } from './reglement-update.component';

describe('Reglement Management Update Component', () => {
  let comp: ReglementUpdateComponent;
  let fixture: ComponentFixture<ReglementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reglementFormService: ReglementFormService;
  let reglementService: ReglementService;
  let locataireService: LocataireService;
  let moiService: MoiService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReglementUpdateComponent],
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
      .overrideTemplate(ReglementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReglementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reglementFormService = TestBed.inject(ReglementFormService);
    reglementService = TestBed.inject(ReglementService);
    locataireService = TestBed.inject(LocataireService);
    moiService = TestBed.inject(MoiService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Locataire query and add missing value', () => {
      const reglement: IReglement = { id: 14463 };
      const locataire: ILocataire = { id: 3768 };
      reglement.locataire = locataire;

      const locataireCollection: ILocataire[] = [{ id: 3768 }];
      jest.spyOn(locataireService, 'query').mockReturnValue(of(new HttpResponse({ body: locataireCollection })));
      const additionalLocataires = [locataire];
      const expectedCollection: ILocataire[] = [...additionalLocataires, ...locataireCollection];
      jest.spyOn(locataireService, 'addLocataireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reglement });
      comp.ngOnInit();

      expect(locataireService.query).toHaveBeenCalled();
      expect(locataireService.addLocataireToCollectionIfMissing).toHaveBeenCalledWith(
        locataireCollection,
        ...additionalLocataires.map(expect.objectContaining),
      );
      expect(comp.locatairesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Moi query and add missing value', () => {
      const reglement: IReglement = { id: 14463 };
      const moi: IMoi = { id: 25038 };
      reglement.moi = moi;

      const moiCollection: IMoi[] = [{ id: 25038 }];
      jest.spyOn(moiService, 'query').mockReturnValue(of(new HttpResponse({ body: moiCollection })));
      const additionalMois = [moi];
      const expectedCollection: IMoi[] = [...additionalMois, ...moiCollection];
      jest.spyOn(moiService, 'addMoiToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reglement });
      comp.ngOnInit();

      expect(moiService.query).toHaveBeenCalled();
      expect(moiService.addMoiToCollectionIfMissing).toHaveBeenCalledWith(moiCollection, ...additionalMois.map(expect.objectContaining));
      expect(comp.moisSharedCollection).toEqual(expectedCollection);
    });

    it('should call Utilisateur query and add missing value', () => {
      const reglement: IReglement = { id: 14463 };
      const utilisateur: IUtilisateur = { id: 2179 };
      reglement.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reglement });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const reglement: IReglement = { id: 14463 };
      const locataire: ILocataire = { id: 3768 };
      reglement.locataire = locataire;
      const moi: IMoi = { id: 25038 };
      reglement.moi = moi;
      const utilisateur: IUtilisateur = { id: 2179 };
      reglement.utilisateur = utilisateur;

      activatedRoute.data = of({ reglement });
      comp.ngOnInit();

      expect(comp.locatairesSharedCollection).toContainEqual(locataire);
      expect(comp.moisSharedCollection).toContainEqual(moi);
      expect(comp.utilisateursSharedCollection).toContainEqual(utilisateur);
      expect(comp.reglement).toEqual(reglement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReglement>>();
      const reglement = { id: 13334 };
      jest.spyOn(reglementFormService, 'getReglement').mockReturnValue(reglement);
      jest.spyOn(reglementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reglement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reglement }));
      saveSubject.complete();

      // THEN
      expect(reglementFormService.getReglement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reglementService.update).toHaveBeenCalledWith(expect.objectContaining(reglement));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReglement>>();
      const reglement = { id: 13334 };
      jest.spyOn(reglementFormService, 'getReglement').mockReturnValue({ id: null });
      jest.spyOn(reglementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reglement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reglement }));
      saveSubject.complete();

      // THEN
      expect(reglementFormService.getReglement).toHaveBeenCalled();
      expect(reglementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReglement>>();
      const reglement = { id: 13334 };
      jest.spyOn(reglementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reglement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reglementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocataire', () => {
      it('should forward to locataireService', () => {
        const entity = { id: 3768 };
        const entity2 = { id: 24112 };
        jest.spyOn(locataireService, 'compareLocataire');
        comp.compareLocataire(entity, entity2);
        expect(locataireService.compareLocataire).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMoi', () => {
      it('should forward to moiService', () => {
        const entity = { id: 25038 };
        const entity2 = { id: 31934 };
        jest.spyOn(moiService, 'compareMoi');
        comp.compareMoi(entity, entity2);
        expect(moiService.compareMoi).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUtilisateur', () => {
      it('should forward to utilisateurService', () => {
        const entity = { id: 2179 };
        const entity2 = { id: 31928 };
        jest.spyOn(utilisateurService, 'compareUtilisateur');
        comp.compareUtilisateur(entity, entity2);
        expect(utilisateurService.compareUtilisateur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
