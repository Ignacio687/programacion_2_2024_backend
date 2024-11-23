import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { CustomizationService } from '../service/customization.service';
import { ICustomization } from '../customization.model';
import { CustomizationFormGroup, CustomizationFormService } from './customization-form.service';

@Component({
  standalone: true,
  selector: 'jhi-customization-update',
  templateUrl: './customization-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomizationUpdateComponent implements OnInit {
  isSaving = false;
  customization: ICustomization | null = null;

  optionsSharedCollection: IOption[] = [];
  devicesSharedCollection: IDevice[] = [];

  protected customizationService = inject(CustomizationService);
  protected customizationFormService = inject(CustomizationFormService);
  protected optionService = inject(OptionService);
  protected deviceService = inject(DeviceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomizationFormGroup = this.customizationFormService.createCustomizationFormGroup();

  compareOption = (o1: IOption | null, o2: IOption | null): boolean => this.optionService.compareOption(o1, o2);

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customization }) => {
      this.customization = customization;
      if (customization) {
        this.updateForm(customization);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customization = this.customizationFormService.getCustomization(this.editForm);
    if (customization.id !== null) {
      this.subscribeToSaveResponse(this.customizationService.update(customization));
    } else {
      this.subscribeToSaveResponse(this.customizationService.create(customization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomization>>): void {
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

  protected updateForm(customization: ICustomization): void {
    this.customization = customization;
    this.customizationFormService.resetForm(this.editForm, customization);

    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing<IOption>(
      this.optionsSharedCollection,
      ...(customization.options ?? []),
    );
    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
      this.devicesSharedCollection,
      ...(customization.devices ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(
        map((options: IOption[]) =>
          this.optionService.addOptionToCollectionIfMissing<IOption>(options, ...(this.customization?.options ?? [])),
        ),
      )
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));

    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(
        map((devices: IDevice[]) =>
          this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, ...(this.customization?.devices ?? [])),
        ),
      )
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }
}
