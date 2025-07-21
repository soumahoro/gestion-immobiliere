import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReglement } from '../reglement.model';
import { ReglementService } from '../service/reglement.service';

@Component({
  templateUrl: './reglement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReglementDeleteDialogComponent {
  reglement?: IReglement;

  protected reglementService = inject(ReglementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reglementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
