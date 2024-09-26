import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { ICharacteristic } from '../characteristic.model';
import { CharacteristicService } from '../service/characteristic.service';
import { CharacteristicFormGroup, CharacteristicFormService } from './characteristic-form.service';

@Component({
  standalone: true,
  selector: 'jhi-characteristic-update',
  templateUrl: './characteristic-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CharacteristicUpdateComponent implements OnInit {
  isSaving = false;
  characteristic: ICharacteristic | null = null;

  devicesSharedCollection: IDevice[] = [];

  protected characteristicService = inject(CharacteristicService);
  protected characteristicFormService = inject(CharacteristicFormService);
  protected deviceService = inject(DeviceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CharacteristicFormGroup = this.characteristicFormService.createCharacteristicFormGroup();

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characteristic }) => {
      this.characteristic = characteristic;
      if (characteristic) {
        this.updateForm(characteristic);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const characteristic = this.characteristicFormService.getCharacteristic(this.editForm);
    if (characteristic.id !== null) {
      this.subscribeToSaveResponse(this.characteristicService.update(characteristic));
    } else {
      this.subscribeToSaveResponse(this.characteristicService.create(characteristic));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharacteristic>>): void {
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

  protected updateForm(characteristic: ICharacteristic): void {
    this.characteristic = characteristic;
    this.characteristicFormService.resetForm(this.editForm, characteristic);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
      this.devicesSharedCollection,
      ...(characteristic.devices ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(
        map((devices: IDevice[]) =>
          this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, ...(this.characteristic?.devices ?? [])),
        ),
      )
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }
}
