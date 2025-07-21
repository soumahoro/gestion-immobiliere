import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IResidence } from '../residence.model';

@Component({
  selector: 'jhi-residence-detail',
  templateUrl: './residence-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ResidenceDetailComponent {
  residence = input<IResidence | null>(null);

  previousState(): void {
    window.history.back();
  }
}
