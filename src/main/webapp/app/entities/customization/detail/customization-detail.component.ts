import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICustomization } from '../customization.model';

@Component({
  standalone: true,
  selector: 'jhi-customization-detail',
  templateUrl: './customization-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CustomizationDetailComponent {
  customization = input<ICustomization | null>(null);

  previousState(): void {
    window.history.back();
  }
}
