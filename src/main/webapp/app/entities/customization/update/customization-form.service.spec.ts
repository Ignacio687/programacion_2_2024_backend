import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../customization.test-samples';

import { CustomizationFormService } from './customization-form.service';

describe('Customization Form Service', () => {
  let service: CustomizationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomizationFormService);
  });

  describe('Service methods', () => {
    describe('createCustomizationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomizationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            supplierForeignId: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing ICustomization should create a new form with FormGroup', () => {
        const formGroup = service.createCustomizationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            supplierForeignId: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomization', () => {
      it('should return NewCustomization for default Customization initial value', () => {
        const formGroup = service.createCustomizationFormGroup(sampleWithNewData);

        const customization = service.getCustomization(formGroup) as any;

        expect(customization).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomization for empty Customization initial value', () => {
        const formGroup = service.createCustomizationFormGroup();

        const customization = service.getCustomization(formGroup) as any;

        expect(customization).toMatchObject({});
      });

      it('should return ICustomization', () => {
        const formGroup = service.createCustomizationFormGroup(sampleWithRequiredData);

        const customization = service.getCustomization(formGroup) as any;

        expect(customization).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomization should not enable id FormControl', () => {
        const formGroup = service.createCustomizationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomization should disable id FormControl', () => {
        const formGroup = service.createCustomizationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
