import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OperacionFormService, OperacionFormGroup } from './operacion-form.service';
import { IOperacion } from '../operacion.model';
import { OperacionService } from '../service/operacion.service';

@Component({
  selector: 'jhi-operacion-update',
  templateUrl: './operacion-update.component.html',
})
export class OperacionUpdateComponent implements OnInit {
  isSaving = false;
  operacion: IOperacion | null = null;

  editForm: OperacionFormGroup = this.operacionFormService.createOperacionFormGroup();

  constructor(
    protected operacionService: OperacionService,
    protected operacionFormService: OperacionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operacion }) => {
      this.operacion = operacion;
      if (operacion) {
        this.updateForm(operacion);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operacion = this.operacionFormService.getOperacion(this.editForm);
    if (operacion.id !== null) {
      this.subscribeToSaveResponse(this.operacionService.update(operacion));
    } else {
      this.subscribeToSaveResponse(this.operacionService.create(operacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperacion>>): void {
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

  protected updateForm(operacion: IOperacion): void {
    this.operacion = operacion;
    this.operacionFormService.resetForm(this.editForm, operacion);
  }
}
