import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CustomizationService } from '../service/customization.service';
import { ICustomization } from '../customization.model';
import { CustomizationFormService } from './customization-form.service';

import { CustomizationUpdateComponent } from './customization-update.component';

describe('Customization Management Update Component', () => {
  let comp: CustomizationUpdateComponent;
  let fixture: ComponentFixture<CustomizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customizationFormService: CustomizationFormService;
  let customizationService: CustomizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CustomizationUpdateComponent],
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
      .overrideTemplate(CustomizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customizationFormService = TestBed.inject(CustomizationFormService);
    customizationService = TestBed.inject(CustomizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const customization: ICustomization = { id: 456 };

      activatedRoute.data = of({ customization });
      comp.ngOnInit();

      expect(comp.customization).toEqual(customization);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomization>>();
      const customization = { id: 123 };
      jest.spyOn(customizationFormService, 'getCustomization').mockReturnValue(customization);
      jest.spyOn(customizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customization }));
      saveSubject.complete();

      // THEN
      expect(customizationFormService.getCustomization).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customizationService.update).toHaveBeenCalledWith(expect.objectContaining(customization));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomization>>();
      const customization = { id: 123 };
      jest.spyOn(customizationFormService, 'getCustomization').mockReturnValue({ id: null });
      jest.spyOn(customizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customization: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customization }));
      saveSubject.complete();

      // THEN
      expect(customizationFormService.getCustomization).toHaveBeenCalled();
      expect(customizationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomization>>();
      const customization = { id: 123 };
      jest.spyOn(customizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customizationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
