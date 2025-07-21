import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IResidence } from '../residence.model';
import { ResidenceService } from '../service/residence.service';

@Component({
  templateUrl: './residence-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResidenceDeleteDialogComponent {
  residence?: IResidence;

  protected residenceService = inject(ResidenceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.residenceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
