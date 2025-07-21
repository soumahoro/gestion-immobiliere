import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProprietaireDetailComponent } from './proprietaire-detail.component';

describe('Proprietaire Management Detail Component', () => {
  let comp: ProprietaireDetailComponent;
  let fixture: ComponentFixture<ProprietaireDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProprietaireDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./proprietaire-detail.component').then(m => m.ProprietaireDetailComponent),
              resolve: { proprietaire: () => of({ id: 6967 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProprietaireDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProprietaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load proprietaire on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProprietaireDetailComponent);

      // THEN
      expect(instance.proprietaire()).toEqual(expect.objectContaining({ id: 6967 }));
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
