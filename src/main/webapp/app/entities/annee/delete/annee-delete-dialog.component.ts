import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAnnee } from '../annee.model';
import { AnneeService } from '../service/annee.service';

@Component({
  templateUrl: './annee-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AnneeDeleteDialogComponent {
  annee?: IAnnee;

  protected anneeService = inject(AnneeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.anneeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
