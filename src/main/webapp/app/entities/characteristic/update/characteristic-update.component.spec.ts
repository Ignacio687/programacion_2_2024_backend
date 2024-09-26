import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { CharacteristicService } from '../service/characteristic.service';
import { ICharacteristic } from '../characteristic.model';
import { CharacteristicFormService } from './characteristic-form.service';

import { CharacteristicUpdateComponent } from './characteristic-update.component';

describe('Characteristic Management Update Component', () => {
  let comp: CharacteristicUpdateComponent;
  let fixture: ComponentFixture<CharacteristicUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let characteristicFormService: CharacteristicFormService;
  let characteristicService: CharacteristicService;
  let deviceService: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CharacteristicUpdateComponent],
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
      .overrideTemplate(CharacteristicUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CharacteristicUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    characteristicFormService = TestBed.inject(CharacteristicFormService);
    characteristicService = TestBed.inject(CharacteristicService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Device query and add missing value', () => {
      const characteristic: ICharacteristic = { id: 456 };
      const devices: IDevice[] = [{ id: 14632 }];
      characteristic.devices = devices;

      const deviceCollection: IDevice[] = [{ id: 11206 }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [...devices];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ characteristic });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining),
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const characteristic: ICharacteristic = { id: 456 };
      const devices: IDevice = { id: 3327 };
      characteristic.devices = [devices];

      activatedRoute.data = of({ characteristic });
      comp.ngOnInit();

      expect(comp.devicesSharedCollection).toContain(devices);
      expect(comp.characteristic).toEqual(characteristic);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharacteristic>>();
      const characteristic = { id: 123 };
      jest.spyOn(characteristicFormService, 'getCharacteristic').mockReturnValue(characteristic);
      jest.spyOn(characteristicService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characteristic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characteristic }));
      saveSubject.complete();

      // THEN
      expect(characteristicFormService.getCharacteristic).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(characteristicService.update).toHaveBeenCalledWith(expect.objectContaining(characteristic));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharacteristic>>();
      const characteristic = { id: 123 };
      jest.spyOn(characteristicFormService, 'getCharacteristic').mockReturnValue({ id: null });
      jest.spyOn(characteristicService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characteristic: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characteristic }));
      saveSubject.complete();

      // THEN
      expect(characteristicFormService.getCharacteristic).toHaveBeenCalled();
      expect(characteristicService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharacteristic>>();
      const characteristic = { id: 123 };
      jest.spyOn(characteristicService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characteristic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(characteristicService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
