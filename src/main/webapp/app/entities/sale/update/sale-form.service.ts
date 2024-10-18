import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISale, NewSale } from '../sale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISale for edit and NewSaleFormGroupInput for create.
 */
type SaleFormGroupInput = ISale | PartialWithRequiredKeyOf<NewSale>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISale | NewSale> = Omit<T, 'saleDate'> & {
  saleDate?: string | null;
};

type SaleFormRawValue = FormValueOf<ISale>;

type NewSaleFormRawValue = FormValueOf<NewSale>;

type SaleFormDefaults = Pick<NewSale, 'id' | 'saleDate' | 'finalized'>;

type SaleFormGroupContent = {
  id: FormControl<SaleFormRawValue['id'] | NewSale['id']>;
  devicePrice: FormControl<SaleFormRawValue['devicePrice']>;
  finalPrice: FormControl<SaleFormRawValue['finalPrice']>;
  saleDate: FormControl<SaleFormRawValue['saleDate']>;
  currency: FormControl<SaleFormRawValue['currency']>;
  finalized: FormControl<SaleFormRawValue['finalized']>;
  device: FormControl<SaleFormRawValue['device']>;
};

export type SaleFormGroup = FormGroup<SaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SaleFormService {
  createSaleFormGroup(sale: SaleFormGroupInput = { id: null }): SaleFormGroup {
    const saleRawValue = this.convertSaleToSaleRawValue({
      ...this.getFormDefaults(),
      ...sale,
    });
    return new FormGroup<SaleFormGroupContent>({
      id: new FormControl(
        { value: saleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      devicePrice: new FormControl(saleRawValue.devicePrice, {
        validators: [Validators.required],
      }),
      finalPrice: new FormControl(saleRawValue.finalPrice, {
        validators: [Validators.required],
      }),
      saleDate: new FormControl(saleRawValue.saleDate, {
        validators: [Validators.required],
      }),
      currency: new FormControl(saleRawValue.currency),
      finalized: new FormControl(saleRawValue.finalized),
      device: new FormControl(saleRawValue.device),
    });
  }

  getSale(form: SaleFormGroup): ISale | NewSale {
    return this.convertSaleRawValueToSale(form.getRawValue() as SaleFormRawValue | NewSaleFormRawValue);
  }

  resetForm(form: SaleFormGroup, sale: SaleFormGroupInput): void {
    const saleRawValue = this.convertSaleToSaleRawValue({ ...this.getFormDefaults(), ...sale });
    form.reset(
      {
        ...saleRawValue,
        id: { value: saleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SaleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      saleDate: currentTime,
      finalized: false,
    };
  }

  private convertSaleRawValueToSale(rawSale: SaleFormRawValue | NewSaleFormRawValue): ISale | NewSale {
    return {
      ...rawSale,
      saleDate: dayjs(rawSale.saleDate, DATE_TIME_FORMAT),
    };
  }

  private convertSaleToSaleRawValue(
    sale: ISale | (Partial<NewSale> & SaleFormDefaults),
  ): SaleFormRawValue | PartialWithRequiredKeyOf<NewSaleFormRawValue> {
    return {
      ...sale,
      saleDate: sale.saleDate ? sale.saleDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
