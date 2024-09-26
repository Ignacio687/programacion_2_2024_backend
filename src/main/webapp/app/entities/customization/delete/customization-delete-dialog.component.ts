import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICustomization } from '../customization.model';
import { CustomizationService } from '../service/customization.service';

@Component({
  standalone: true,
  templateUrl: './customization-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CustomizationDeleteDialogComponent {
  customization?: ICustomization;

  protected customizationService = inject(CustomizationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customizationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
