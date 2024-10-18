import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { ExtraService } from '../service/extra.service';
import { IExtra } from '../extra.model';
import { ExtraFormService } from './extra-form.service';

import { ExtraUpdateComponent } from './extra-update.component';

describe('Extra Management Update Component', () => {
  let comp: ExtraUpdateComponent;
  let fixture: ComponentFixture<ExtraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let extraFormService: ExtraFormService;
  let extraService: ExtraService;
  let deviceService: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExtraUpdateComponent],
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
      .overrideTemplate(ExtraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExtraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    extraFormService = TestBed.inject(ExtraFormService);
    extraService = TestBed.inject(ExtraService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Device query and add missing value', () => {
      const extra: IExtra = { id: 456 };
      const devices: IDevice[] = [{ id: 22529 }];
      extra.devices = devices;

      const deviceCollection: IDevice[] = [{ id: 2050 }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [...devices];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ extra });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining),
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const extra: IExtra = { id: 456 };
      const devices: IDevice = { id: 26979 };
      extra.devices = [devices];

      activatedRoute.data = of({ extra });
      comp.ngOnInit();

      expect(comp.devicesSharedCollection).toContain(devices);
      expect(comp.extra).toEqual(extra);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtra>>();
      const extra = { id: 123 };
      jest.spyOn(extraFormService, 'getExtra').mockReturnValue(extra);
      jest.spyOn(extraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extra }));
      saveSubject.complete();

      // THEN
      expect(extraFormService.getExtra).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(extraService.update).toHaveBeenCalledWith(expect.objectContaining(extra));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtra>>();
      const extra = { id: 123 };
      jest.spyOn(extraFormService, 'getExtra').mockReturnValue({ id: null });
      jest.spyOn(extraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extra: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extra }));
      saveSubject.complete();

      // THEN
      expect(extraFormService.getExtra).toHaveBeenCalled();
      expect(extraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtra>>();
      const extra = { id: 123 };
      jest.spyOn(extraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(extraService.update).toHaveBeenCalled();
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
