import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOption, NewOption } from '../option.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOption for edit and NewOptionFormGroupInput for create.
 */
type OptionFormGroupInput = IOption | PartialWithRequiredKeyOf<NewOption>;

type OptionFormDefaults = Pick<NewOption, 'id' | 'devices'>;

type OptionFormGroupContent = {
  id: FormControl<IOption['id'] | NewOption['id']>;
  supplierForeignId: FormControl<IOption['supplierForeignId']>;
  code: FormControl<IOption['code']>;
  name: FormControl<IOption['name']>;
  description: FormControl<IOption['description']>;
  additionalPrice: FormControl<IOption['additionalPrice']>;
  customization: FormControl<IOption['customization']>;
  devices: FormControl<IOption['devices']>;
};

export type OptionFormGroup = FormGroup<OptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OptionFormService {
  createOptionFormGroup(option: OptionFormGroupInput = { id: null }): OptionFormGroup {
    const optionRawValue = {
      ...this.getFormDefaults(),
      ...option,
    };
    return new FormGroup<OptionFormGroupContent>({
      id: new FormControl(
        { value: optionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      supplierForeignId: new FormControl(optionRawValue.supplierForeignId, {
        validators: [Validators.required],
      }),
      code: new FormControl(optionRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(optionRawValue.name),
      description: new FormControl(optionRawValue.description),
      additionalPrice: new FormControl(optionRawValue.additionalPrice, {
        validators: [Validators.required],
      }),
      customization: new FormControl(optionRawValue.customization),
      devices: new FormControl(optionRawValue.devices ?? []),
    });
  }

  getOption(form: OptionFormGroup): IOption | NewOption {
    return form.getRawValue() as IOption | NewOption;
  }

  resetForm(form: OptionFormGroup, option: OptionFormGroupInput): void {
    const optionRawValue = { ...this.getFormDefaults(), ...option };
    form.reset(
      {
        ...optionRawValue,
        id: { value: optionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OptionFormDefaults {
    return {
      id: null,
      devices: [],
    };
  }
}
