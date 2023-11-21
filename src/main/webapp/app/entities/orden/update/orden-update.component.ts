import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OrdenFormService, OrdenFormGroup } from './orden-form.service';
import { IOrden } from '../orden.model';
import { OrdenService } from '../service/orden.service';
import { IOperacion } from 'app/entities/operacion/operacion.model';
import { OperacionService } from 'app/entities/operacion/service/operacion.service';

@Component({
  selector: 'jhi-orden-update',
  templateUrl: './orden-update.component.html',
})
export class OrdenUpdateComponent implements OnInit {
  isSaving = false;
  orden: IOrden | null = null;

  operacionsCollection: IOperacion[] = [];

  editForm: OrdenFormGroup = this.ordenFormService.createOrdenFormGroup();

  constructor(
    protected ordenService: OrdenService,
    protected ordenFormService: OrdenFormService,
    protected operacionService: OperacionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOperacion = (o1: IOperacion | null, o2: IOperacion | null): boolean => this.operacionService.compareOperacion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orden }) => {
      this.orden = orden;
      if (orden) {
        this.updateForm(orden);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orden = this.ordenFormService.getOrden(this.editForm);
    if (orden.id !== null) {
      this.subscribeToSaveResponse(this.ordenService.update(orden));
    } else {
      this.subscribeToSaveResponse(this.ordenService.create(orden));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrden>>): void {
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

  protected updateForm(orden: IOrden): void {
    this.orden = orden;
    this.ordenFormService.resetForm(this.editForm, orden);

    this.operacionsCollection = this.operacionService.addOperacionToCollectionIfMissing<IOperacion>(
      this.operacionsCollection,
      orden.operacion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.operacionService
      .query({ filter: 'orden-is-null' })
      .pipe(map((res: HttpResponse<IOperacion[]>) => res.body ?? []))
      .pipe(
        map((operacions: IOperacion[]) =>
          this.operacionService.addOperacionToCollectionIfMissing<IOperacion>(operacions, this.orden?.operacion)
        )
      )
      .subscribe((operacions: IOperacion[]) => (this.operacionsCollection = operacions));
  }
}
