import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../operacion.test-samples';

import { OperacionFormService } from './operacion-form.service';

describe('Operacion Form Service', () => {
  let service: OperacionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OperacionFormService);
  });

  describe('Service methods', () => {
    describe('createOperacionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOperacionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cliente: expect.any(Object),
            accionId: expect.any(Object),
            accion: expect.any(Object),
            precio: expect.any(Object),
            cantidad: expect.any(Object),
            fechaOperacion: expect.any(Object),
            modo: expect.any(Object),
            operacionExitosa: expect.any(Object),
            operacionObservaciones: expect.any(Object),
          })
        );
      });

      it('passing IOperacion should create a new form with FormGroup', () => {
        const formGroup = service.createOperacionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cliente: expect.any(Object),
            accionId: expect.any(Object),
            accion: expect.any(Object),
            precio: expect.any(Object),
            cantidad: expect.any(Object),
            fechaOperacion: expect.any(Object),
            modo: expect.any(Object),
            operacionExitosa: expect.any(Object),
            operacionObservaciones: expect.any(Object),
          })
        );
      });
    });

    describe('getOperacion', () => {
      it('should return NewOperacion for default Operacion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOperacionFormGroup(sampleWithNewData);

        const operacion = service.getOperacion(formGroup) as any;

        expect(operacion).toMatchObject(sampleWithNewData);
      });

      it('should return NewOperacion for empty Operacion initial value', () => {
        const formGroup = service.createOperacionFormGroup();

        const operacion = service.getOperacion(formGroup) as any;

        expect(operacion).toMatchObject({});
      });

      it('should return IOperacion', () => {
        const formGroup = service.createOperacionFormGroup(sampleWithRequiredData);

        const operacion = service.getOperacion(formGroup) as any;

        expect(operacion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOperacion should not enable id FormControl', () => {
        const formGroup = service.createOperacionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOperacion should disable id FormControl', () => {
        const formGroup = service.createOperacionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
