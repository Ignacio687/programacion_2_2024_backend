import { IOption } from 'app/entities/option/option.model';
import { IExtra } from 'app/entities/extra/extra.model';
import { ISale } from 'app/entities/sale/sale.model';

export interface ISaleItem {
  id: number;
  price?: number | null;
  option?: IOption | null;
  extra?: IExtra | null;
  sale?: ISale | null;
}

export type NewSaleItem = Omit<ISaleItem, 'id'> & { id: null };
