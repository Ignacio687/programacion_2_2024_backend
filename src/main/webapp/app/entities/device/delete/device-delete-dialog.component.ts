import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDevice } from '../device.model';
import { DeviceService } from '../service/device.service';

@Component({
  standalone: true,
  templateUrl: './device-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DeviceDeleteDialogComponent {
  device?: IDevice;

  protected deviceService = inject(DeviceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
