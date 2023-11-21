import dayjs from 'dayjs/esm';

import { IOperacion, NewOperacion } from './operacion.model';

export const sampleWithRequiredData: IOperacion = {
  id: 50393,
};

export const sampleWithPartialData: IOperacion = {
  id: 88658,
  accion: 'Directo',
  precio: 15232,
  operacionExitosa: true,
};

export const sampleWithFullData: IOperacion = {
  id: 56400,
  cliente: 72491,
  accionId: 9960,
  accion: 'Grecia',
  precio: 55585,
  cantidad: 94163,
  fechaOperacion: dayjs('2023-11-21T03:27'),
  modo: 'eyeballs Arquitecto',
  operacionExitosa: true,
  operacionObservaciones: 'Directo Bacon clicks-and-mortar',
};

export const sampleWithNewData: NewOperacion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
