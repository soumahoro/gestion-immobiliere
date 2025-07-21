import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ResidenceDetailComponent } from './residence-detail.component';

describe('Residence Management Detail Component', () => {
  let comp: ResidenceDetailComponent;
  let fixture: ComponentFixture<ResidenceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResidenceDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./residence-detail.component').then(m => m.ResidenceDetailComponent),
              resolve: { residence: () => of({ id: 8404 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ResidenceDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResidenceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load residence on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ResidenceDetailComponent);

      // THEN
      expect(instance.residence()).toEqual(expect.objectContaining({ id: 8404 }));
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
