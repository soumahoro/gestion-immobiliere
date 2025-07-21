import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProprietaire } from '../proprietaire.model';
import { ProprietaireService } from '../service/proprietaire.service';

@Component({
  templateUrl: './proprietaire-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProprietaireDeleteDialogComponent {
  proprietaire?: IProprietaire;

  protected proprietaireService = inject(ProprietaireService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proprietaireService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
