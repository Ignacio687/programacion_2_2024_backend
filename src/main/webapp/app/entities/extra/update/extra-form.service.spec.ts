import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../extra.test-samples';

import { ExtraFormService } from './extra-form.service';

describe('Extra Form Service', () => {
  let service: ExtraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExtraFormService);
  });

  describe('Service methods', () => {
    describe('createExtraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExtraFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            supplierForeignId: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            freePrice: expect.any(Object),
            devices: expect.any(Object),
          }),
        );
      });

      it('passing IExtra should create a new form with FormGroup', () => {
        const formGroup = service.createExtraFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            supplierForeignId: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            freePrice: expect.any(Object),
            devices: expect.any(Object),
          }),
        );
      });
    });

    describe('getExtra', () => {
      it('should return NewExtra for default Extra initial value', () => {
        const formGroup = service.createExtraFormGroup(sampleWithNewData);

        const extra = service.getExtra(formGroup) as any;

        expect(extra).toMatchObject(sampleWithNewData);
      });

      it('should return NewExtra for empty Extra initial value', () => {
        const formGroup = service.createExtraFormGroup();

        const extra = service.getExtra(formGroup) as any;

        expect(extra).toMatchObject({});
      });

      it('should return IExtra', () => {
        const formGroup = service.createExtraFormGroup(sampleWithRequiredData);

        const extra = service.getExtra(formGroup) as any;

        expect(extra).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExtra should not enable id FormControl', () => {
        const formGroup = service.createExtraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExtra should disable id FormControl', () => {
        const formGroup = service.createExtraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
