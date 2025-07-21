import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMoi } from '../moi.model';
import { MoiService } from '../service/moi.service';

@Component({
  templateUrl: './moi-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MoiDeleteDialogComponent {
  moi?: IMoi;

  protected moiService = inject(MoiService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.moiService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
