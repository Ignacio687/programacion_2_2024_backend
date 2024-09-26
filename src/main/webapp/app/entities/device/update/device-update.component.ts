import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICharacteristic } from 'app/entities/characteristic/characteristic.model';
import { CharacteristicService } from 'app/entities/characteristic/service/characteristic.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IExtra } from 'app/entities/extra/extra.model';
import { ExtraService } from 'app/entities/extra/service/extra.service';
import { DeviceService } from '../service/device.service';
import { IDevice } from '../device.model';
import { DeviceFormGroup, DeviceFormService } from './device-form.service';

@Component({
  standalone: true,
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;
  device: IDevice | null = null;

  characteristicsSharedCollection: ICharacteristic[] = [];
  optionsSharedCollection: IOption[] = [];
  extrasSharedCollection: IExtra[] = [];

  protected deviceService = inject(DeviceService);
  protected deviceFormService = inject(DeviceFormService);
  protected characteristicService = inject(CharacteristicService);
  protected optionService = inject(OptionService);
  protected extraService = inject(ExtraService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DeviceFormGroup = this.deviceFormService.createDeviceFormGroup();

  compareCharacteristic = (o1: ICharacteristic | null, o2: ICharacteristic | null): boolean =>
    this.characteristicService.compareCharacteristic(o1, o2);

  compareOption = (o1: IOption | null, o2: IOption | null): boolean => this.optionService.compareOption(o1, o2);

  compareExtra = (o1: IExtra | null, o2: IExtra | null): boolean => this.extraService.compareExtra(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.device = device;
      if (device) {
        this.updateForm(device);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const device = this.deviceFormService.getDevice(this.editForm);
    if (device.id !== null) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(device: IDevice): void {
    this.device = device;
    this.deviceFormService.resetForm(this.editForm, device);

    this.characteristicsSharedCollection = this.characteristicService.addCharacteristicToCollectionIfMissing<ICharacteristic>(
      this.characteristicsSharedCollection,
      ...(device.characteristics ?? []),
    );
    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing<IOption>(
      this.optionsSharedCollection,
      ...(device.options ?? []),
    );
    this.extrasSharedCollection = this.extraService.addExtraToCollectionIfMissing<IExtra>(
      this.extrasSharedCollection,
      ...(device.extras ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.characteristicService
      .query()
      .pipe(map((res: HttpResponse<ICharacteristic[]>) => res.body ?? []))
      .pipe(
        map((characteristics: ICharacteristic[]) =>
          this.characteristicService.addCharacteristicToCollectionIfMissing<ICharacteristic>(
            characteristics,
            ...(this.device?.characteristics ?? []),
          ),
        ),
      )
      .subscribe((characteristics: ICharacteristic[]) => (this.characteristicsSharedCollection = characteristics));

    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(
        map((options: IOption[]) => this.optionService.addOptionToCollectionIfMissing<IOption>(options, ...(this.device?.options ?? []))),
      )
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));

    this.extraService
      .query()
      .pipe(map((res: HttpResponse<IExtra[]>) => res.body ?? []))
      .pipe(map((extras: IExtra[]) => this.extraService.addExtraToCollectionIfMissing<IExtra>(extras, ...(this.device?.extras ?? []))))
      .subscribe((extras: IExtra[]) => (this.extrasSharedCollection = extras));
  }
}
