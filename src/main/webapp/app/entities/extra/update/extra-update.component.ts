import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IExtra } from '../extra.model';
import { ExtraService } from '../service/extra.service';
import { ExtraFormGroup, ExtraFormService } from './extra-form.service';

@Component({
  standalone: true,
  selector: 'jhi-extra-update',
  templateUrl: './extra-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExtraUpdateComponent implements OnInit {
  isSaving = false;
  extra: IExtra | null = null;

  devicesSharedCollection: IDevice[] = [];

  protected extraService = inject(ExtraService);
  protected extraFormService = inject(ExtraFormService);
  protected deviceService = inject(DeviceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExtraFormGroup = this.extraFormService.createExtraFormGroup();

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extra }) => {
      this.extra = extra;
      if (extra) {
        this.updateForm(extra);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const extra = this.extraFormService.getExtra(this.editForm);
    if (extra.id !== null) {
      this.subscribeToSaveResponse(this.extraService.update(extra));
    } else {
      this.subscribeToSaveResponse(this.extraService.create(extra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtra>>): void {
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

  protected updateForm(extra: IExtra): void {
    this.extra = extra;
    this.extraFormService.resetForm(this.editForm, extra);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(
      this.devicesSharedCollection,
      ...(extra.devices ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(
        map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, ...(this.extra?.devices ?? []))),
      )
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }
}
