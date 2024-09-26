import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../characteristic.test-samples';

import { CharacteristicFormService } from './characteristic-form.service';

describe('Characteristic Form Service', () => {
  let service: CharacteristicFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CharacteristicFormService);
  });

  describe('Service methods', () => {
    describe('createCharacteristicFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCharacteristicFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            devices: expect.any(Object),
          }),
        );
      });

      it('passing ICharacteristic should create a new form with FormGroup', () => {
        const formGroup = service.createCharacteristicFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            devices: expect.any(Object),
          }),
        );
      });
    });

    describe('getCharacteristic', () => {
      it('should return NewCharacteristic for default Characteristic initial value', () => {
        const formGroup = service.createCharacteristicFormGroup(sampleWithNewData);

        const characteristic = service.getCharacteristic(formGroup) as any;

        expect(characteristic).toMatchObject(sampleWithNewData);
      });

      it('should return NewCharacteristic for empty Characteristic initial value', () => {
        const formGroup = service.createCharacteristicFormGroup();

        const characteristic = service.getCharacteristic(formGroup) as any;

        expect(characteristic).toMatchObject({});
      });

      it('should return ICharacteristic', () => {
        const formGroup = service.createCharacteristicFormGroup(sampleWithRequiredData);

        const characteristic = service.getCharacteristic(formGroup) as any;

        expect(characteristic).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICharacteristic should not enable id FormControl', () => {
        const formGroup = service.createCharacteristicFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCharacteristic should disable id FormControl', () => {
        const formGroup = service.createCharacteristicFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
