import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICharacteristic } from 'app/entities/characteristic/characteristic.model';
import { CharacteristicService } from 'app/entities/characteristic/service/characteristic.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IExtra } from 'app/entities/extra/extra.model';
import { ExtraService } from 'app/entities/extra/service/extra.service';
import { IDevice } from '../device.model';
import { DeviceService } from '../service/device.service';
import { DeviceFormService } from './device-form.service';

import { DeviceUpdateComponent } from './device-update.component';

describe('Device Management Update Component', () => {
  let comp: DeviceUpdateComponent;
  let fixture: ComponentFixture<DeviceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceFormService: DeviceFormService;
  let deviceService: DeviceService;
  let characteristicService: CharacteristicService;
  let optionService: OptionService;
  let extraService: ExtraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DeviceUpdateComponent],
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
      .overrideTemplate(DeviceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceFormService = TestBed.inject(DeviceFormService);
    deviceService = TestBed.inject(DeviceService);
    characteristicService = TestBed.inject(CharacteristicService);
    optionService = TestBed.inject(OptionService);
    extraService = TestBed.inject(ExtraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Characteristic query and add missing value', () => {
      const device: IDevice = { id: 456 };
      const characteristics: ICharacteristic[] = [{ id: 15763 }];
      device.characteristics = characteristics;

      const characteristicCollection: ICharacteristic[] = [{ id: 23672 }];
      jest.spyOn(characteristicService, 'query').mockReturnValue(of(new HttpResponse({ body: characteristicCollection })));
      const additionalCharacteristics = [...characteristics];
      const expectedCollection: ICharacteristic[] = [...additionalCharacteristics, ...characteristicCollection];
      jest.spyOn(characteristicService, 'addCharacteristicToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(characteristicService.query).toHaveBeenCalled();
      expect(characteristicService.addCharacteristicToCollectionIfMissing).toHaveBeenCalledWith(
        characteristicCollection,
        ...additionalCharacteristics.map(expect.objectContaining),
      );
      expect(comp.characteristicsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Option query and add missing value', () => {
      const device: IDevice = { id: 456 };
      const options: IOption[] = [{ id: 10910 }];
      device.options = options;

      const optionCollection: IOption[] = [{ id: 22381 }];
      jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
      const additionalOptions = [...options];
      const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
      jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(optionService.query).toHaveBeenCalled();
      expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(
        optionCollection,
        ...additionalOptions.map(expect.objectContaining),
      );
      expect(comp.optionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Extra query and add missing value', () => {
      const device: IDevice = { id: 456 };
      const extras: IExtra[] = [{ id: 10293 }];
      device.extras = extras;

      const extraCollection: IExtra[] = [{ id: 9612 }];
      jest.spyOn(extraService, 'query').mockReturnValue(of(new HttpResponse({ body: extraCollection })));
      const additionalExtras = [...extras];
      const expectedCollection: IExtra[] = [...additionalExtras, ...extraCollection];
      jest.spyOn(extraService, 'addExtraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(extraService.query).toHaveBeenCalled();
      expect(extraService.addExtraToCollectionIfMissing).toHaveBeenCalledWith(
        extraCollection,
        ...additionalExtras.map(expect.objectContaining),
      );
      expect(comp.extrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const device: IDevice = { id: 456 };
      const characteristics: ICharacteristic = { id: 2357 };
      device.characteristics = [characteristics];
      const options: IOption = { id: 24871 };
      device.options = [options];
      const extras: IExtra = { id: 3463 };
      device.extras = [extras];

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(comp.characteristicsSharedCollection).toContain(characteristics);
      expect(comp.optionsSharedCollection).toContain(options);
      expect(comp.extrasSharedCollection).toContain(extras);
      expect(comp.device).toEqual(device);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDevice>>();
      const device = { id: 123 };
      jest.spyOn(deviceFormService, 'getDevice').mockReturnValue(device);
      jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: device }));
      saveSubject.complete();

      // THEN
      expect(deviceFormService.getDevice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceService.update).toHaveBeenCalledWith(expect.objectContaining(device));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDevice>>();
      const device = { id: 123 };
      jest.spyOn(deviceFormService, 'getDevice').mockReturnValue({ id: null });
      jest.spyOn(deviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: device }));
      saveSubject.complete();

      // THEN
      expect(deviceFormService.getDevice).toHaveBeenCalled();
      expect(deviceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDevice>>();
      const device = { id: 123 };
      jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCharacteristic', () => {
      it('Should forward to characteristicService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(characteristicService, 'compareCharacteristic');
        comp.compareCharacteristic(entity, entity2);
        expect(characteristicService.compareCharacteristic).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
  });
});
