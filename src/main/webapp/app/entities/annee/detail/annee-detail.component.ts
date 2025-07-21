import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAnnee } from '../annee.model';

@Component({
  selector: 'jhi-annee-detail',
  templateUrl: './annee-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AnneeDetailComponent {
  annee = input<IAnnee | null>(null);

  previousState(): void {
    window.history.back();
  }
}
