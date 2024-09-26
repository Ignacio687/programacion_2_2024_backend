import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICharacteristic } from '../characteristic.model';
import { CharacteristicService } from '../service/characteristic.service';

@Component({
  standalone: true,
  templateUrl: './characteristic-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CharacteristicDeleteDialogComponent {
  characteristic?: ICharacteristic;

  protected characteristicService = inject(CharacteristicService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.characteristicService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
