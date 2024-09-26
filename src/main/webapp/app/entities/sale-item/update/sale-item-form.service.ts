import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISaleItem, NewSaleItem } from '../sale-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISaleItem for edit and NewSaleItemFormGroupInput for create.
 */
type SaleItemFormGroupInput = ISaleItem | PartialWithRequiredKeyOf<NewSaleItem>;

type SaleItemFormDefaults = Pick<NewSaleItem, 'id'>;

type SaleItemFormGroupContent = {
  id: FormControl<ISaleItem['id'] | NewSaleItem['id']>;
  price: FormControl<ISaleItem['price']>;
  option: FormControl<ISaleItem['option']>;
  extra: FormControl<ISaleItem['extra']>;
  sale: FormControl<ISaleItem['sale']>;
};

export type SaleItemFormGroup = FormGroup<SaleItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SaleItemFormService {
  createSaleItemFormGroup(saleItem: SaleItemFormGroupInput = { id: null }): SaleItemFormGroup {
    const saleItemRawValue = {
      ...this.getFormDefaults(),
      ...saleItem,
    };
    return new FormGroup<SaleItemFormGroupContent>({
      id: new FormControl(
        { value: saleItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      price: new FormControl(saleItemRawValue.price, {
        validators: [Validators.required],
      }),
      option: new FormControl(saleItemRawValue.option),
      extra: new FormControl(saleItemRawValue.extra),
      sale: new FormControl(saleItemRawValue.sale),
    });
  }

  getSaleItem(form: SaleItemFormGroup): ISaleItem | NewSaleItem {
    return form.getRawValue() as ISaleItem | NewSaleItem;
  }

  resetForm(form: SaleItemFormGroup, saleItem: SaleItemFormGroupInput): void {
    const saleItemRawValue = { ...this.getFormDefaults(), ...saleItem };
    form.reset(
      {
        ...saleItemRawValue,
        id: { value: saleItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SaleItemFormDefaults {
    return {
      id: null,
    };
  }
}
