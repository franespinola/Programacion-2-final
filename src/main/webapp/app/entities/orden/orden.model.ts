import dayjs from 'dayjs/esm';
import { IOperacion } from 'app/entities/operacion/operacion.model';

export interface IOrden {
  id: number;
  cliente?: number | null;
  accionId?: number | null;
  accion?: string | null;
  precio?: number | null;
  cantidad?: number | null;
  fechaOperacion?: dayjs.Dayjs | null;
  modo?: string | null;
  operacion?: Pick<IOperacion, 'id'> | null;
}

export type NewOrden = Omit<IOrden, 'id'> & { id: null };
