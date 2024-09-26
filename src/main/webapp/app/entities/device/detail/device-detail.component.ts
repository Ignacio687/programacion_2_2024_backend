import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDevice } from '../device.model';

@Component({
  standalone: true,
  selector: 'jhi-device-detail',
  templateUrl: './device-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DeviceDetailComponent {
  device = input<IDevice | null>(null);

  previousState(): void {
    window.history.back();
  }
}
