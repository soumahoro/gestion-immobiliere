import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAppartement } from '../appartement.model';
import { AppartementService } from '../service/appartement.service';

@Component({
  templateUrl: './appartement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AppartementDeleteDialogComponent {
  appartement?: IAppartement;

  protected appartementService = inject(AppartementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appartementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
