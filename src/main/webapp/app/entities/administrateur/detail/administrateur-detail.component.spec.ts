import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AdministrateurDetailComponent } from './administrateur-detail.component';

describe('Administrateur Management Detail Component', () => {
  let comp: AdministrateurDetailComponent;
  let fixture: ComponentFixture<AdministrateurDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdministrateurDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./administrateur-detail.component').then(m => m.AdministrateurDetailComponent),
              resolve: { administrateur: () => of({ id: 7232 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AdministrateurDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdministrateurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load administrateur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AdministrateurDetailComponent);

      // THEN
      expect(instance.administrateur()).toEqual(expect.objectContaining({ id: 7232 }));
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
