import dayjs from 'dayjs/esm';

import { IOrden, NewOrden } from './orden.model';

export const sampleWithRequiredData: IOrden = {
  id: 34750,
};

export const sampleWithPartialData: IOrden = {
  id: 86251,
  cliente: 68425,
  accionId: 80323,
  accion: 'Avon deposit',
  fechaOperacion: dayjs('2023-11-22T13:50'),
};

export const sampleWithFullData: IOrden = {
  id: 69395,
  cliente: 5809,
  accionId: 26137,
  accion: 'Guantes',
  precio: 72787,
  cantidad: 43977,
  fechaOperacion: dayjs('2023-11-22T05:13'),
  modo: 'customized País SSL',
};

export const sampleWithNewData: NewOrden = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
