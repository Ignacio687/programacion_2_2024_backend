import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICharacteristic } from '../characteristic.model';

@Component({
  standalone: true,
  selector: 'jhi-characteristic-detail',
  templateUrl: './characteristic-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CharacteristicDetailComponent {
  characteristic = input<ICharacteristic | null>(null);

  previousState(): void {
    window.history.back();
  }
}
