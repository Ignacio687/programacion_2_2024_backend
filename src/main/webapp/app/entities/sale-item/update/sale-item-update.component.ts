import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IExtra } from 'app/entities/extra/extra.model';
import { ExtraService } from 'app/entities/extra/service/extra.service';
import { ISale } from 'app/entities/sale/sale.model';
import { SaleService } from 'app/entities/sale/service/sale.service';
import { SaleItemService } from '../service/sale-item.service';
import { ISaleItem } from '../sale-item.model';
import { SaleItemFormGroup, SaleItemFormService } from './sale-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sale-item-update',
  templateUrl: './sale-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SaleItemUpdateComponent implements OnInit {
  isSaving = false;
  saleItem: ISaleItem | null = null;

  optionsSharedCollection: IOption[] = [];
  extrasSharedCollection: IExtra[] = [];
  salesSharedCollection: ISale[] = [];

  protected saleItemService = inject(SaleItemService);
  protected saleItemFormService = inject(SaleItemFormService);
  protected optionService = inject(OptionService);
  protected extraService = inject(ExtraService);
  protected saleService = inject(SaleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SaleItemFormGroup = this.saleItemFormService.createSaleItemFormGroup();

  compareOption = (o1: IOption | null, o2: IOption | null): boolean => this.optionService.compareOption(o1, o2);

  compareExtra = (o1: IExtra | null, o2: IExtra | null): boolean => this.extraService.compareExtra(o1, o2);

  compareSale = (o1: ISale | null, o2: ISale | null): boolean => this.saleService.compareSale(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ saleItem }) => {
      this.saleItem = saleItem;
      if (saleItem) {
        this.updateForm(saleItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const saleItem = this.saleItemFormService.getSaleItem(this.editForm);
    if (saleItem.id !== null) {
      this.subscribeToSaveResponse(this.saleItemService.update(saleItem));
    } else {
      this.subscribeToSaveResponse(this.saleItemService.create(saleItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISaleItem>>): void {
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

  protected updateForm(saleItem: ISaleItem): void {
    this.saleItem = saleItem;
    this.saleItemFormService.resetForm(this.editForm, saleItem);

    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing<IOption>(
      this.optionsSharedCollection,
      saleItem.option,
    );
    this.extrasSharedCollection = this.extraService.addExtraToCollectionIfMissing<IExtra>(this.extrasSharedCollection, saleItem.extra);
    this.salesSharedCollection = this.saleService.addSaleToCollectionIfMissing<ISale>(this.salesSharedCollection, saleItem.sale);
  }

  protected loadRelationshipsOptions(): void {
    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(map((options: IOption[]) => this.optionService.addOptionToCollectionIfMissing<IOption>(options, this.saleItem?.option)))
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));

    this.extraService
      .query()
      .pipe(map((res: HttpResponse<IExtra[]>) => res.body ?? []))
      .pipe(map((extras: IExtra[]) => this.extraService.addExtraToCollectionIfMissing<IExtra>(extras, this.saleItem?.extra)))
      .subscribe((extras: IExtra[]) => (this.extrasSharedCollection = extras));

    this.saleService
      .query()
      .pipe(map((res: HttpResponse<ISale[]>) => res.body ?? []))
      .pipe(map((sales: ISale[]) => this.saleService.addSaleToCollectionIfMissing<ISale>(sales, this.saleItem?.sale)))
      .subscribe((sales: ISale[]) => (this.salesSharedCollection = sales));
  }
}
