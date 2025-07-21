import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IProprietaire } from '../proprietaire.model';

@Component({
  selector: 'jhi-proprietaire-detail',
  templateUrl: './proprietaire-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ProprietaireDetailComponent {
  proprietaire = input<IProprietaire | null>(null);

  previousState(): void {
    window.history.back();
  }
}
