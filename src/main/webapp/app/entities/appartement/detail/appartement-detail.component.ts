import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAppartement } from '../appartement.model';

@Component({
  selector: 'jhi-appartement-detail',
  templateUrl: './appartement-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AppartementDetailComponent {
  appartement = input<IAppartement | null>(null);

  previousState(): void {
    window.history.back();
  }
}
