import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustomization, NewCustomization } from '../customization.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomization for edit and NewCustomizationFormGroupInput for create.
 */
type CustomizationFormGroupInput = ICustomization | PartialWithRequiredKeyOf<NewCustomization>;

type CustomizationFormDefaults = Pick<NewCustomization, 'id'>;

type CustomizationFormGroupContent = {
  id: FormControl<ICustomization['id'] | NewCustomization['id']>;
  name: FormControl<ICustomization['name']>;
  description: FormControl<ICustomization['description']>;
};

export type CustomizationFormGroup = FormGroup<CustomizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomizationFormService {
  createCustomizationFormGroup(customization: CustomizationFormGroupInput = { id: null }): CustomizationFormGroup {
    const customizationRawValue = {
      ...this.getFormDefaults(),
      ...customization,
    };
    return new FormGroup<CustomizationFormGroupContent>({
      id: new FormControl(
        { value: customizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(customizationRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(customizationRawValue.description),
    });
  }

  getCustomization(form: CustomizationFormGroup): ICustomization | NewCustomization {
    return form.getRawValue() as ICustomization | NewCustomization;
  }

  resetForm(form: CustomizationFormGroup, customization: CustomizationFormGroupInput): void {
    const customizationRawValue = { ...this.getFormDefaults(), ...customization };
    form.reset(
      {
        ...customizationRawValue,
        id: { value: customizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomizationFormDefaults {
    return {
      id: null,
    };
  }
}
