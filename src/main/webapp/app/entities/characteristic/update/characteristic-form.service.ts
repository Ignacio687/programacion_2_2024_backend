import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICharacteristic, NewCharacteristic } from '../characteristic.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICharacteristic for edit and NewCharacteristicFormGroupInput for create.
 */
type CharacteristicFormGroupInput = ICharacteristic | PartialWithRequiredKeyOf<NewCharacteristic>;

type CharacteristicFormDefaults = Pick<NewCharacteristic, 'id' | 'devices'>;

type CharacteristicFormGroupContent = {
  id: FormControl<ICharacteristic['id'] | NewCharacteristic['id']>;
  supplierForeignId: FormControl<ICharacteristic['supplierForeignId']>;
  name: FormControl<ICharacteristic['name']>;
  description: FormControl<ICharacteristic['description']>;
  devices: FormControl<ICharacteristic['devices']>;
};

export type CharacteristicFormGroup = FormGroup<CharacteristicFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CharacteristicFormService {
  createCharacteristicFormGroup(characteristic: CharacteristicFormGroupInput = { id: null }): CharacteristicFormGroup {
    const characteristicRawValue = {
      ...this.getFormDefaults(),
      ...characteristic,
    };
    return new FormGroup<CharacteristicFormGroupContent>({
      id: new FormControl(
        { value: characteristicRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      supplierForeignId: new FormControl(characteristicRawValue.supplierForeignId, {
        validators: [Validators.required],
      }),
      name: new FormControl(characteristicRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(characteristicRawValue.description),
      devices: new FormControl(characteristicRawValue.devices ?? []),
    });
  }

  getCharacteristic(form: CharacteristicFormGroup): ICharacteristic | NewCharacteristic {
    return form.getRawValue() as ICharacteristic | NewCharacteristic;
  }

  resetForm(form: CharacteristicFormGroup, characteristic: CharacteristicFormGroupInput): void {
    const characteristicRawValue = { ...this.getFormDefaults(), ...characteristic };
    form.reset(
      {
        ...characteristicRawValue,
        id: { value: characteristicRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CharacteristicFormDefaults {
    return {
      id: null,
      devices: [],
    };
  }
}
