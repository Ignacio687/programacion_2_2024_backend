import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExtra } from '../extra.model';
import { ExtraService } from '../service/extra.service';

@Component({
  standalone: true,
  templateUrl: './extra-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExtraDeleteDialogComponent {
  extra?: IExtra;

  protected extraService = inject(ExtraService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.extraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
