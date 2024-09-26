import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IExtra, NewExtra } from '../extra.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExtra for edit and NewExtraFormGroupInput for create.
 */
type ExtraFormGroupInput = IExtra | PartialWithRequiredKeyOf<NewExtra>;

type ExtraFormDefaults = Pick<NewExtra, 'id' | 'devices'>;

type ExtraFormGroupContent = {
  id: FormControl<IExtra['id'] | NewExtra['id']>;
  name: FormControl<IExtra['name']>;
  description: FormControl<IExtra['description']>;
  price: FormControl<IExtra['price']>;
  freePrice: FormControl<IExtra['freePrice']>;
  devices: FormControl<IExtra['devices']>;
};

export type ExtraFormGroup = FormGroup<ExtraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExtraFormService {
  createExtraFormGroup(extra: ExtraFormGroupInput = { id: null }): ExtraFormGroup {
    const extraRawValue = {
      ...this.getFormDefaults(),
      ...extra,
    };
    return new FormGroup<ExtraFormGroupContent>({
      id: new FormControl(
        { value: extraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(extraRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(extraRawValue.description),
      price: new FormControl(extraRawValue.price, {
        validators: [Validators.required],
      }),
      freePrice: new FormControl(extraRawValue.freePrice, {
        validators: [Validators.required],
      }),
      devices: new FormControl(extraRawValue.devices ?? []),
    });
  }

  getExtra(form: ExtraFormGroup): IExtra | NewExtra {
    return form.getRawValue() as IExtra | NewExtra;
  }

  resetForm(form: ExtraFormGroup, extra: ExtraFormGroupInput): void {
    const extraRawValue = { ...this.getFormDefaults(), ...extra };
    form.reset(
      {
        ...extraRawValue,
        id: { value: extraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExtraFormDefaults {
    return {
      id: null,
      devices: [],
    };
  }
}
