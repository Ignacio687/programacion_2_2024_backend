import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISaleItem } from '../sale-item.model';

@Component({
  standalone: true,
  selector: 'jhi-sale-item-detail',
  templateUrl: './sale-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SaleItemDetailComponent {
  saleItem = input<ISaleItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
