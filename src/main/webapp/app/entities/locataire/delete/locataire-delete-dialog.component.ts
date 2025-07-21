import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILocataire } from '../locataire.model';
import { LocataireService } from '../service/locataire.service';

@Component({
  templateUrl: './locataire-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LocataireDeleteDialogComponent {
  locataire?: ILocataire;

  protected locataireService = inject(LocataireService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.locataireService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
