import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IExtra } from 'app/entities/extra/extra.model';
import { ExtraService } from 'app/entities/extra/service/extra.service';
import { ISale } from 'app/entities/sale/sale.model';
import { SaleService } from 'app/entities/sale/service/sale.service';
import { ISaleItem } from '../sale-item.model';
import { SaleItemService } from '../service/sale-item.service';
import { SaleItemFormService } from './sale-item-form.service';

import { SaleItemUpdateComponent } from './sale-item-update.component';

describe('SaleItem Management Update Component', () => {
  let comp: SaleItemUpdateComponent;
  let fixture: ComponentFixture<SaleItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let saleItemFormService: SaleItemFormService;
  let saleItemService: SaleItemService;
  let optionService: OptionService;
  let extraService: ExtraService;
  let saleService: SaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SaleItemUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SaleItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SaleItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    saleItemFormService = TestBed.inject(SaleItemFormService);
    saleItemService = TestBed.inject(SaleItemService);
    optionService = TestBed.inject(OptionService);
    extraService = TestBed.inject(ExtraService);
    saleService = TestBed.inject(SaleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Option query and add missing value', () => {
      const saleItem: ISaleItem = { id: 456 };
      const option: IOption = { id: 25546 };
      saleItem.option = option;

      const optionCollection: IOption[] = [{ id: 30783 }];
      jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
      const additionalOptions = [option];
      const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
      jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ saleItem });
      comp.ngOnInit();

      expect(optionService.query).toHaveBeenCalled();
      expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(
        optionCollection,
        ...additionalOptions.map(expect.objectContaining),
      );
      expect(comp.optionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Extra query and add missing value', () => {
      const saleItem: ISaleItem = { id: 456 };
      const extra: IExtra = { id: 10294 };
      saleItem.extra = extra;

      const extraCollection: IExtra[] = [{ id: 16872 }];
      jest.spyOn(extraService, 'query').mockReturnValue(of(new HttpResponse({ body: extraCollection })));
      const additionalExtras = [extra];
      const expectedCollection: IExtra[] = [...additionalExtras, ...extraCollection];
      jest.spyOn(extraService, 'addExtraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ saleItem });
      comp.ngOnInit();

      expect(extraService.query).toHaveBeenCalled();
      expect(extraService.addExtraToCollectionIfMissing).toHaveBeenCalledWith(
        extraCollection,
        ...additionalExtras.map(expect.objectContaining),
      );
      expect(comp.extrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Sale query and add missing value', () => {
      const saleItem: ISaleItem = { id: 456 };
      const sale: ISale = { id: 17188 };
      saleItem.sale = sale;

      const saleCollection: ISale[] = [{ id: 14344 }];
      jest.spyOn(saleService, 'query').mockReturnValue(of(new HttpResponse({ body: saleCollection })));
      const additionalSales = [sale];
      const expectedCollection: ISale[] = [...additionalSales, ...saleCollection];
      jest.spyOn(saleService, 'addSaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ saleItem });
      comp.ngOnInit();

      expect(saleService.query).toHaveBeenCalled();
      expect(saleService.addSaleToCollectionIfMissing).toHaveBeenCalledWith(
        saleCollection,
        ...additionalSales.map(expect.objectContaining),
      );
      expect(comp.salesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const saleItem: ISaleItem = { id: 456 };
      const option: IOption = { id: 7549 };
      saleItem.option = option;
      const extra: IExtra = { id: 21428 };
      saleItem.extra = extra;
      const sale: ISale = { id: 9677 };
      saleItem.sale = sale;

      activatedRoute.data = of({ saleItem });
      comp.ngOnInit();

      expect(comp.optionsSharedCollection).toContain(option);
      expect(comp.extrasSharedCollection).toContain(extra);
      expect(comp.salesSharedCollection).toContain(sale);
      expect(comp.saleItem).toEqual(saleItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISaleItem>>();
      const saleItem = { id: 123 };
      jest.spyOn(saleItemFormService, 'getSaleItem').mockReturnValue(saleItem);
      jest.spyOn(saleItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saleItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: saleItem }));
      saveSubject.complete();

      // THEN
      expect(saleItemFormService.getSaleItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(saleItemService.update).toHaveBeenCalledWith(expect.objectContaining(saleItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISaleItem>>();
      const saleItem = { id: 123 };
      jest.spyOn(saleItemFormService, 'getSaleItem').mockReturnValue({ id: null });
      jest.spyOn(saleItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saleItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: saleItem }));
      saveSubject.complete();

      // THEN
      expect(saleItemFormService.getSaleItem).toHaveBeenCalled();
      expect(saleItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISaleItem>>();
      const saleItem = { id: 123 };
      jest.spyOn(saleItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saleItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(saleItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOption', () => {
      it('Should forward to optionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(optionService, 'compareOption');
        comp.compareOption(entity, entity2);
        expect(optionService.compareOption).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareExtra', () => {
      it('Should forward to extraService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(extraService, 'compareExtra');
        comp.compareExtra(entity, entity2);
        expect(extraService.compareExtra).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSale', () => {
      it('Should forward to saleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(saleService, 'compareSale');
        comp.compareSale(entity, entity2);
        expect(saleService.compareSale).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
