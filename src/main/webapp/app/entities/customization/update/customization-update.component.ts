import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomization } from '../customization.model';
import { CustomizationService } from '../service/customization.service';
import { CustomizationFormGroup, CustomizationFormService } from './customization-form.service';

@Component({
  standalone: true,
  selector: 'jhi-customization-update',
  templateUrl: './customization-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomizationUpdateComponent implements OnInit {
  isSaving = false;
  customization: ICustomization | null = null;

  protected customizationService = inject(CustomizationService);
  protected customizationFormService = inject(CustomizationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomizationFormGroup = this.customizationFormService.createCustomizationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customization }) => {
      this.customization = customization;
      if (customization) {
        this.updateForm(customization);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customization = this.customizationFormService.getCustomization(this.editForm);
    if (customization.id !== null) {
      this.subscribeToSaveResponse(this.customizationService.update(customization));
    } else {
      this.subscribeToSaveResponse(this.customizationService.create(customization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomization>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(customization: ICustomization): void {
    this.customization = customization;
    this.customizationFormService.resetForm(this.editForm, customization);
  }
}
