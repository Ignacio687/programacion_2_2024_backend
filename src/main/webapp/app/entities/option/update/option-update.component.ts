import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomization } from 'app/entities/customization/customization.model';
import { CustomizationService } from 'app/entities/customization/service/customization.service';
import { IOption } from '../option.model';
import { OptionService } from '../service/option.service';
import { OptionFormGroup, OptionFormService } from './option-form.service';

@Component({
  standalone: true,
  selector: 'jhi-option-update',
  templateUrl: './option-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OptionUpdateComponent implements OnInit {
  isSaving = false;
  option: IOption | null = null;

  customizationsSharedCollection: ICustomization[] = [];

  protected optionService = inject(OptionService);
  protected optionFormService = inject(OptionFormService);
  protected customizationService = inject(CustomizationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OptionFormGroup = this.optionFormService.createOptionFormGroup();

  compareCustomization = (o1: ICustomization | null, o2: ICustomization | null): boolean =>
    this.customizationService.compareCustomization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ option }) => {
      this.option = option;
      if (option) {
        this.updateForm(option);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const option = this.optionFormService.getOption(this.editForm);
    if (option.id !== null) {
      this.subscribeToSaveResponse(this.optionService.update(option));
    } else {
      this.subscribeToSaveResponse(this.optionService.create(option));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOption>>): void {
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

  protected updateForm(option: IOption): void {
    this.option = option;
    this.optionFormService.resetForm(this.editForm, option);

    this.customizationsSharedCollection = this.customizationService.addCustomizationToCollectionIfMissing<ICustomization>(
      this.customizationsSharedCollection,
      ...(option.customizations ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customizationService
      .query()
      .pipe(map((res: HttpResponse<ICustomization[]>) => res.body ?? []))
      .pipe(
        map((customizations: ICustomization[]) =>
          this.customizationService.addCustomizationToCollectionIfMissing<ICustomization>(
            customizations,
            ...(this.option?.customizations ?? []),
          ),
        ),
      )
      .subscribe((customizations: ICustomization[]) => (this.customizationsSharedCollection = customizations));
  }
}
