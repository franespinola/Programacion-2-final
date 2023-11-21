import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOperacion, NewOperacion } from '../operacion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperacion for edit and NewOperacionFormGroupInput for create.
 */
type OperacionFormGroupInput = IOperacion | PartialWithRequiredKeyOf<NewOperacion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOperacion | NewOperacion> = Omit<T, 'fechaOperacion'> & {
  fechaOperacion?: string | null;
};

type OperacionFormRawValue = FormValueOf<IOperacion>;

type NewOperacionFormRawValue = FormValueOf<NewOperacion>;

type OperacionFormDefaults = Pick<NewOperacion, 'id' | 'fechaOperacion' | 'operacionExitosa'>;

type OperacionFormGroupContent = {
  id: FormControl<OperacionFormRawValue['id'] | NewOperacion['id']>;
  cliente: FormControl<OperacionFormRawValue['cliente']>;
  accionId: FormControl<OperacionFormRawValue['accionId']>;
  accion: FormControl<OperacionFormRawValue['accion']>;
  precio: FormControl<OperacionFormRawValue['precio']>;
  cantidad: FormControl<OperacionFormRawValue['cantidad']>;
  fechaOperacion: FormControl<OperacionFormRawValue['fechaOperacion']>;
  modo: FormControl<OperacionFormRawValue['modo']>;
  operacionExitosa: FormControl<OperacionFormRawValue['operacionExitosa']>;
  operacionObservaciones: FormControl<OperacionFormRawValue['operacionObservaciones']>;
};

export type OperacionFormGroup = FormGroup<OperacionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperacionFormService {
  createOperacionFormGroup(operacion: OperacionFormGroupInput = { id: null }): OperacionFormGroup {
    const operacionRawValue = this.convertOperacionToOperacionRawValue({
      ...this.getFormDefaults(),
      ...operacion,
    });
    return new FormGroup<OperacionFormGroupContent>({
      id: new FormControl(
        { value: operacionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cliente: new FormControl(operacionRawValue.cliente),
      accionId: new FormControl(operacionRawValue.accionId),
      accion: new FormControl(operacionRawValue.accion),
      precio: new FormControl(operacionRawValue.precio),
      cantidad: new FormControl(operacionRawValue.cantidad),
      fechaOperacion: new FormControl(operacionRawValue.fechaOperacion),
      modo: new FormControl(operacionRawValue.modo),
      operacionExitosa: new FormControl(operacionRawValue.operacionExitosa),
      operacionObservaciones: new FormControl(operacionRawValue.operacionObservaciones),
    });
  }

  getOperacion(form: OperacionFormGroup): IOperacion | NewOperacion {
    return this.convertOperacionRawValueToOperacion(form.getRawValue() as OperacionFormRawValue | NewOperacionFormRawValue);
  }

  resetForm(form: OperacionFormGroup, operacion: OperacionFormGroupInput): void {
    const operacionRawValue = this.convertOperacionToOperacionRawValue({ ...this.getFormDefaults(), ...operacion });
    form.reset(
      {
        ...operacionRawValue,
        id: { value: operacionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OperacionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaOperacion: currentTime,
      operacionExitosa: false,
    };
  }

  private convertOperacionRawValueToOperacion(rawOperacion: OperacionFormRawValue | NewOperacionFormRawValue): IOperacion | NewOperacion {
    return {
      ...rawOperacion,
      fechaOperacion: dayjs(rawOperacion.fechaOperacion, DATE_TIME_FORMAT),
    };
  }

  private convertOperacionToOperacionRawValue(
    operacion: IOperacion | (Partial<NewOperacion> & OperacionFormDefaults)
  ): OperacionFormRawValue | PartialWithRequiredKeyOf<NewOperacionFormRawValue> {
    return {
      ...operacion,
      fechaOperacion: operacion.fechaOperacion ? operacion.fechaOperacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
