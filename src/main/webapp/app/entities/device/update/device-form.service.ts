import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDevice, NewDevice } from '../device.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDevice for edit and NewDeviceFormGroupInput for create.
 */
type DeviceFormGroupInput = IDevice | PartialWithRequiredKeyOf<NewDevice>;

type DeviceFormDefaults = Pick<NewDevice, 'id' | 'active' | 'characteristics' | 'extras' | 'customizations'>;

type DeviceFormGroupContent = {
  id: FormControl<IDevice['id'] | NewDevice['id']>;
  supplierForeignId: FormControl<IDevice['supplierForeignId']>;
  supplier: FormControl<IDevice['supplier']>;
  code: FormControl<IDevice['code']>;
  name: FormControl<IDevice['name']>;
  description: FormControl<IDevice['description']>;
  basePrice: FormControl<IDevice['basePrice']>;
  currency: FormControl<IDevice['currency']>;
  active: FormControl<IDevice['active']>;
  characteristics: FormControl<IDevice['characteristics']>;
  extras: FormControl<IDevice['extras']>;
  customizations: FormControl<IDevice['customizations']>;
};

export type DeviceFormGroup = FormGroup<DeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeviceFormService {
  createDeviceFormGroup(device: DeviceFormGroupInput = { id: null }): DeviceFormGroup {
    const deviceRawValue = {
      ...this.getFormDefaults(),
      ...device,
    };
    return new FormGroup<DeviceFormGroupContent>({
      id: new FormControl(
        { value: deviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      supplierForeignId: new FormControl(deviceRawValue.supplierForeignId, {
        validators: [Validators.required],
      }),
      supplier: new FormControl(deviceRawValue.supplier, {
        validators: [Validators.required],
      }),
      code: new FormControl(deviceRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(deviceRawValue.name),
      description: new FormControl(deviceRawValue.description),
      basePrice: new FormControl(deviceRawValue.basePrice, {
        validators: [Validators.required],
      }),
      currency: new FormControl(deviceRawValue.currency),
      active: new FormControl(deviceRawValue.active),
      characteristics: new FormControl(deviceRawValue.characteristics ?? []),
      extras: new FormControl(deviceRawValue.extras ?? []),
      customizations: new FormControl(deviceRawValue.customizations ?? []),
    });
  }

  getDevice(form: DeviceFormGroup): IDevice | NewDevice {
    return form.getRawValue() as IDevice | NewDevice;
  }

  resetForm(form: DeviceFormGroup, device: DeviceFormGroupInput): void {
    const deviceRawValue = { ...this.getFormDefaults(), ...device };
    form.reset(
      {
        ...deviceRawValue,
        id: { value: deviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeviceFormDefaults {
    return {
      id: null,
      active: false,
      characteristics: [],
      extras: [],
      customizations: [],
    };
  }
}
