import dayjs from 'dayjs/esm';

export interface IOperacion {
  id: number;
  cliente?: number | null;
  accionId?: number | null;
  accion?: string | null;
  precio?: number | null;
  cantidad?: number | null;
  fechaOperacion?: dayjs.Dayjs | null;
  modo?: string | null;
  operacionExitosa?: boolean | null;
  operacionObservaciones?: string | null;
}

export type NewOperacion = Omit<IOperacion, 'id'> & { id: null };
