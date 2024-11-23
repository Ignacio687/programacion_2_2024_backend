import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { ICustomization } from '../customization.model';
import { CustomizationService } from '../service/customization.service';
import { CustomizationFormService } from './customization-form.service';

import { CustomizationUpdateComponent } from './customization-update.component';

describe('Customization Management Update Component', () => {
  let comp: CustomizationUpdateComponent;
  let fixture: ComponentFixture<CustomizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customizationFormService: CustomizationFormService;
  let customizationService: CustomizationService;
  let optionService: OptionService;
  let deviceService: DeviceService;

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
    optionService = TestBed.inject(OptionService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Option query and add missing value', () => {
      const customization: ICustomization = { id: 456 };
      const options: IOption[] = [{ id: 22381 }];
      customization.options = options;

      const optionCollection: IOption[] = [{ id: 24871 }];
      jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
      const additionalOptions = [...options];
      const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
      jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customization });
      comp.ngOnInit();

      expect(optionService.query).toHaveBeenCalled();
      expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(
        optionCollection,
        ...additionalOptions.map(expect.objectContaining),
      );
      expect(comp.optionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Device query and add missing value', () => {
      const customization: ICustomization = { id: 456 };
      const devices: IDevice[] = [{ id: 26851 }];
      customization.devices = devices;

      const deviceCollection: IDevice[] = [{ id: 12171 }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [...devices];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customization });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining),
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customization: ICustomization = { id: 456 };
      const options: IOption = { id: 13507 };
      customization.options = [options];
      const devices: IDevice = { id: 285 };
      customization.devices = [devices];

      activatedRoute.data = of({ customization });
      comp.ngOnInit();

      expect(comp.optionsSharedCollection).toContain(options);
      expect(comp.devicesSharedCollection).toContain(devices);
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

    describe('compareDevice', () => {
      it('Should forward to deviceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(deviceService, 'compareDevice');
        comp.compareDevice(entity, entity2);
        expect(deviceService.compareDevice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
