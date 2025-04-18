import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomization } from 'app/entities/customization/customization.model';
import { CustomizationService } from 'app/entities/customization/service/customization.service';
import { OptionService } from '../service/option.service';
import { IOption } from '../option.model';
import { OptionFormService } from './option-form.service';

import { OptionUpdateComponent } from './option-update.component';

describe('Option Management Update Component', () => {
  let comp: OptionUpdateComponent;
  let fixture: ComponentFixture<OptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let optionFormService: OptionFormService;
  let optionService: OptionService;
  let customizationService: CustomizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OptionUpdateComponent],
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
      .overrideTemplate(OptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    optionFormService = TestBed.inject(OptionFormService);
    optionService = TestBed.inject(OptionService);
    customizationService = TestBed.inject(CustomizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customization query and add missing value', () => {
      const option: IOption = { id: 456 };
      const customizations: ICustomization[] = [{ id: 28253 }];
      option.customizations = customizations;

      const customizationCollection: ICustomization[] = [{ id: 27469 }];
      jest.spyOn(customizationService, 'query').mockReturnValue(of(new HttpResponse({ body: customizationCollection })));
      const additionalCustomizations = [...customizations];
      const expectedCollection: ICustomization[] = [...additionalCustomizations, ...customizationCollection];
      jest.spyOn(customizationService, 'addCustomizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ option });
      comp.ngOnInit();

      expect(customizationService.query).toHaveBeenCalled();
      expect(customizationService.addCustomizationToCollectionIfMissing).toHaveBeenCalledWith(
        customizationCollection,
        ...additionalCustomizations.map(expect.objectContaining),
      );
      expect(comp.customizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const option: IOption = { id: 456 };
      const customization: ICustomization = { id: 10264 };
      option.customizations = [customization];

      activatedRoute.data = of({ option });
      comp.ngOnInit();

      expect(comp.customizationsSharedCollection).toContain(customization);
      expect(comp.option).toEqual(option);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 123 };
      jest.spyOn(optionFormService, 'getOption').mockReturnValue(option);
      jest.spyOn(optionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: option }));
      saveSubject.complete();

      // THEN
      expect(optionFormService.getOption).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(optionService.update).toHaveBeenCalledWith(expect.objectContaining(option));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 123 };
      jest.spyOn(optionFormService, 'getOption').mockReturnValue({ id: null });
      jest.spyOn(optionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: option }));
      saveSubject.complete();

      // THEN
      expect(optionFormService.getOption).toHaveBeenCalled();
      expect(optionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 123 };
      jest.spyOn(optionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(optionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomization', () => {
      it('Should forward to customizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customizationService, 'compareCustomization');
        comp.compareCustomization(entity, entity2);
        expect(customizationService.compareCustomization).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
