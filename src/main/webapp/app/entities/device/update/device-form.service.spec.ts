import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../device.test-samples';

import { DeviceFormService } from './device-form.service';

describe('Device Form Service', () => {
  let service: DeviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            supplierForeignId: expect.any(Object),
            supplier: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            basePrice: expect.any(Object),
            currency: expect.any(Object),
            active: expect.any(Object),
            characteristics: expect.any(Object),
            extras: expect.any(Object),
            customizations: expect.any(Object),
          }),
        );
      });

      it('passing IDevice should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            supplierForeignId: expect.any(Object),
            supplier: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            basePrice: expect.any(Object),
            currency: expect.any(Object),
            active: expect.any(Object),
            characteristics: expect.any(Object),
            extras: expect.any(Object),
            customizations: expect.any(Object),
          }),
        );
      });
    });

    describe('getDevice', () => {
      it('should return NewDevice for default Device initial value', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithNewData);

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject(sampleWithNewData);
      });

      it('should return NewDevice for empty Device initial value', () => {
        const formGroup = service.createDeviceFormGroup();

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject({});
      });

      it('should return IDevice', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDevice should not enable id FormControl', () => {
        const formGroup = service.createDeviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDevice should disable id FormControl', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
