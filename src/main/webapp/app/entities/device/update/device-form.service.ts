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

type DeviceFormDefaults = Pick<NewDevice, 'id' | 'characteristics' | 'options' | 'extras'>;

type DeviceFormGroupContent = {
  id: FormControl<IDevice['id'] | NewDevice['id']>;
  code: FormControl<IDevice['code']>;
  name: FormControl<IDevice['name']>;
  description: FormControl<IDevice['description']>;
  basePrice: FormControl<IDevice['basePrice']>;
  currency: FormControl<IDevice['currency']>;
  characteristics: FormControl<IDevice['characteristics']>;
  options: FormControl<IDevice['options']>;
  extras: FormControl<IDevice['extras']>;
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
      code: new FormControl(deviceRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(deviceRawValue.name),
      description: new FormControl(deviceRawValue.description),
      basePrice: new FormControl(deviceRawValue.basePrice, {
        validators: [Validators.required],
      }),
      currency: new FormControl(deviceRawValue.currency),
      characteristics: new FormControl(deviceRawValue.characteristics ?? []),
      options: new FormControl(deviceRawValue.options ?? []),
      extras: new FormControl(deviceRawValue.extras ?? []),
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
      characteristics: [],
      options: [],
      extras: [],
    };
  }
}
