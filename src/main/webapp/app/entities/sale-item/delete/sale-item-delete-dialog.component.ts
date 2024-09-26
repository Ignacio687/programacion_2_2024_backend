import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISaleItem } from '../sale-item.model';
import { SaleItemService } from '../service/sale-item.service';

@Component({
  standalone: true,
  templateUrl: './sale-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SaleItemDeleteDialogComponent {
  saleItem?: ISaleItem;

  protected saleItemService = inject(SaleItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.saleItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
