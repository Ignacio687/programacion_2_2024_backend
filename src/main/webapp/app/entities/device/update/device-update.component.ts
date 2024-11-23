import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICharacteristic } from 'app/entities/characteristic/characteristic.model';
import { CharacteristicService } from 'app/entities/characteristic/service/characteristic.service';
import { IExtra } from 'app/entities/extra/extra.model';
import { ExtraService } from 'app/entities/extra/service/extra.service';
import { ICustomization } from 'app/entities/customization/customization.model';
import { CustomizationService } from 'app/entities/customization/service/customization.service';
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
  extrasSharedCollection: IExtra[] = [];
  customizationsSharedCollection: ICustomization[] = [];

  protected deviceService = inject(DeviceService);
  protected deviceFormService = inject(DeviceFormService);
  protected characteristicService = inject(CharacteristicService);
  protected extraService = inject(ExtraService);
  protected customizationService = inject(CustomizationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DeviceFormGroup = this.deviceFormService.createDeviceFormGroup();

  compareCharacteristic = (o1: ICharacteristic | null, o2: ICharacteristic | null): boolean =>
    this.characteristicService.compareCharacteristic(o1, o2);

  compareExtra = (o1: IExtra | null, o2: IExtra | null): boolean => this.extraService.compareExtra(o1, o2);

  compareCustomization = (o1: ICustomization | null, o2: ICustomization | null): boolean =>
    this.customizationService.compareCustomization(o1, o2);

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
    this.extrasSharedCollection = this.extraService.addExtraToCollectionIfMissing<IExtra>(
      this.extrasSharedCollection,
      ...(device.extras ?? []),
    );
    this.customizationsSharedCollection = this.customizationService.addCustomizationToCollectionIfMissing<ICustomization>(
      this.customizationsSharedCollection,
      ...(device.customizations ?? []),
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

    this.extraService
      .query()
      .pipe(map((res: HttpResponse<IExtra[]>) => res.body ?? []))
      .pipe(map((extras: IExtra[]) => this.extraService.addExtraToCollectionIfMissing<IExtra>(extras, ...(this.device?.extras ?? []))))
      .subscribe((extras: IExtra[]) => (this.extrasSharedCollection = extras));

    this.customizationService
      .query()
      .pipe(map((res: HttpResponse<ICustomization[]>) => res.body ?? []))
      .pipe(
        map((customizations: ICustomization[]) =>
          this.customizationService.addCustomizationToCollectionIfMissing<ICustomization>(
            customizations,
            ...(this.device?.customizations ?? []),
          ),
        ),
      )
      .subscribe((customizations: ICustomization[]) => (this.customizationsSharedCollection = customizations));
  }
}
