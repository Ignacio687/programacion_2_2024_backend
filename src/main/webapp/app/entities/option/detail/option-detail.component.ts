import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOption } from '../option.model';

@Component({
  standalone: true,
  selector: 'jhi-option-detail',
  templateUrl: './option-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OptionDetailComponent {
  option = input<IOption | null>(null);

  previousState(): void {
    window.history.back();
  }
}
