import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MoiDetailComponent } from './moi-detail.component';

describe('Moi Management Detail Component', () => {
  let comp: MoiDetailComponent;
  let fixture: ComponentFixture<MoiDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoiDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./moi-detail.component').then(m => m.MoiDetailComponent),
              resolve: { moi: () => of({ id: 25038 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MoiDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MoiDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load moi on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MoiDetailComponent);

      // THEN
      expect(instance.moi()).toEqual(expect.objectContaining({ id: 25038 }));
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
