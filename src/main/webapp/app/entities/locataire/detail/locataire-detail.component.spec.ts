import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LocataireDetailComponent } from './locataire-detail.component';

describe('Locataire Management Detail Component', () => {
  let comp: LocataireDetailComponent;
  let fixture: ComponentFixture<LocataireDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocataireDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./locataire-detail.component').then(m => m.LocataireDetailComponent),
              resolve: { locataire: () => of({ id: 3768 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LocataireDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LocataireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load locataire on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LocataireDetailComponent);

      // THEN
      expect(instance.locataire()).toEqual(expect.objectContaining({ id: 3768 }));
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
