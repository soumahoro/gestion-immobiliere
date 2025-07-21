import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AppartementDetailComponent } from './appartement-detail.component';

describe('Appartement Management Detail Component', () => {
  let comp: AppartementDetailComponent;
  let fixture: ComponentFixture<AppartementDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppartementDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./appartement-detail.component').then(m => m.AppartementDetailComponent),
              resolve: { appartement: () => of({ id: 30619 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AppartementDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppartementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load appartement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AppartementDetailComponent);

      // THEN
      expect(instance.appartement()).toEqual(expect.objectContaining({ id: 30619 }));
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
