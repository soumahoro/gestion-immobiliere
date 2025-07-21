import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ILocataire } from '../locataire.model';

@Component({
  selector: 'jhi-locataire-detail',
  templateUrl: './locataire-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class LocataireDetailComponent {
  locataire = input<ILocataire | null>(null);

  previousState(): void {
    window.history.back();
  }
}
