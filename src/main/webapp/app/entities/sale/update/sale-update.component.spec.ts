import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { ISale } from '../sale.model';
import { SaleService } from '../service/sale.service';
import { SaleFormService } from './sale-form.service';

import { SaleUpdateComponent } from './sale-update.component';

describe('Sale Management Update Component', () => {
  let comp: SaleUpdateComponent;
  let fixture: ComponentFixture<SaleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let saleFormService: SaleFormService;
  let saleService: SaleService;
  let userService: UserService;
  let deviceService: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SaleUpdateComponent],
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
      .overrideTemplate(SaleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SaleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    saleFormService = TestBed.inject(SaleFormService);
    saleService = TestBed.inject(SaleService);
    userService = TestBed.inject(UserService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const sale: ISale = { id: 456 };
      const user: IUser = { id: 26108 };
      sale.user = user;

      const userCollection: IUser[] = [{ id: 25182 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sale });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Device query and add missing value', () => {
      const sale: ISale = { id: 456 };
      const device: IDevice = { id: 24359 };
      sale.device = device;

      const deviceCollection: IDevice[] = [{ id: 5252 }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [device];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sale });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining),
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sale: ISale = { id: 456 };
      const user: IUser = { id: 12440 };
      sale.user = user;
      const device: IDevice = { id: 7904 };
      sale.device = device;

      activatedRoute.data = of({ sale });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.devicesSharedCollection).toContain(device);
      expect(comp.sale).toEqual(sale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISale>>();
      const sale = { id: 123 };
      jest.spyOn(saleFormService, 'getSale').mockReturnValue(sale);
      jest.spyOn(saleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sale }));
      saveSubject.complete();

      // THEN
      expect(saleFormService.getSale).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(saleService.update).toHaveBeenCalledWith(expect.objectContaining(sale));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISale>>();
      const sale = { id: 123 };
      jest.spyOn(saleFormService, 'getSale').mockReturnValue({ id: null });
      jest.spyOn(saleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sale: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sale }));
      saveSubject.complete();

      // THEN
      expect(saleFormService.getSale).toHaveBeenCalled();
      expect(saleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISale>>();
      const sale = { id: 123 };
      jest.spyOn(saleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(saleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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
