import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMoi } from '../moi.model';
import { MoiService } from '../service/moi.service';
import { MoiFormGroup, MoiFormService } from './moi-form.service';

@Component({
  selector: 'jhi-moi-update',
  templateUrl: './moi-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MoiUpdateComponent implements OnInit {
  isSaving = false;
  moi: IMoi | null = null;

  protected moiService = inject(MoiService);
  protected moiFormService = inject(MoiFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MoiFormGroup = this.moiFormService.createMoiFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moi }) => {
      this.moi = moi;
      if (moi) {
        this.updateForm(moi);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moi = this.moiFormService.getMoi(this.editForm);
    if (moi.id !== null) {
      this.subscribeToSaveResponse(this.moiService.update(moi));
    } else {
      this.subscribeToSaveResponse(this.moiService.create(moi));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoi>>): void {
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

  protected updateForm(moi: IMoi): void {
    this.moi = moi;
    this.moiFormService.resetForm(this.editForm, moi);
  }
}
