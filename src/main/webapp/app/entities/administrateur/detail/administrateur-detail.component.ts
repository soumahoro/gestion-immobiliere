import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAdministrateur } from '../administrateur.model';

@Component({
  selector: 'jhi-administrateur-detail',
  templateUrl: './administrateur-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AdministrateurDetailComponent {
  administrateur = input<IAdministrateur | null>(null);

  previousState(): void {
    window.history.back();
  }
}
