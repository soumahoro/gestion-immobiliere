import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AnneeDetailComponent } from './annee-detail.component';

describe('Annee Management Detail Component', () => {
  let comp: AnneeDetailComponent;
  let fixture: ComponentFixture<AnneeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnneeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./annee-detail.component').then(m => m.AnneeDetailComponent),
              resolve: { annee: () => of({ id: 21078 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AnneeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnneeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load annee on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AnneeDetailComponent);

      // THEN
      expect(instance.annee()).toEqual(expect.objectContaining({ id: 21078 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
