import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMoi } from '../moi.model';

@Component({
  selector: 'jhi-moi-detail',
  templateUrl: './moi-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MoiDetailComponent {
  moi = input<IMoi | null>(null);

  previousState(): void {
    window.history.back();
  }
}
