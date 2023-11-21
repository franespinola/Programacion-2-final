import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperacion, NewOperacion } from '../operacion.model';

export type PartialUpdateOperacion = Partial<IOperacion> & Pick<IOperacion, 'id'>;

type RestOf<T extends IOperacion | NewOperacion> = Omit<T, 'fechaOperacion'> & {
  fechaOperacion?: string | null;
};

export type RestOperacion = RestOf<IOperacion>;

export type NewRestOperacion = RestOf<NewOperacion>;

export type PartialUpdateRestOperacion = RestOf<PartialUpdateOperacion>;

export type EntityResponseType = HttpResponse<IOperacion>;
export type EntityArrayResponseType = HttpResponse<IOperacion[]>;

@Injectable({ providedIn: 'root' })
export class OperacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operacion: NewOperacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operacion);
    return this.http
      .post<RestOperacion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(operacion: IOperacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operacion);
    return this.http
      .put<RestOperacion>(`${this.resourceUrl}/${this.getOperacionIdentifier(operacion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(operacion: PartialUpdateOperacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operacion);
    return this.http
      .patch<RestOperacion>(`${this.resourceUrl}/${this.getOperacionIdentifier(operacion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOperacion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOperacion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOperacionIdentifier(operacion: Pick<IOperacion, 'id'>): number {
    return operacion.id;
  }

  compareOperacion(o1: Pick<IOperacion, 'id'> | null, o2: Pick<IOperacion, 'id'> | null): boolean {
    return o1 && o2 ? this.getOperacionIdentifier(o1) === this.getOperacionIdentifier(o2) : o1 === o2;
  }

  addOperacionToCollectionIfMissing<Type extends Pick<IOperacion, 'id'>>(
    operacionCollection: Type[],
    ...operacionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operacions: Type[] = operacionsToCheck.filter(isPresent);
    if (operacions.length > 0) {
      const operacionCollectionIdentifiers = operacionCollection.map(operacionItem => this.getOperacionIdentifier(operacionItem)!);
      const operacionsToAdd = operacions.filter(operacionItem => {
        const operacionIdentifier = this.getOperacionIdentifier(operacionItem);
        if (operacionCollectionIdentifiers.includes(operacionIdentifier)) {
          return false;
        }
        operacionCollectionIdentifiers.push(operacionIdentifier);
        return true;
      });
      return [...operacionsToAdd, ...operacionCollection];
    }
    return operacionCollection;
  }

  protected convertDateFromClient<T extends IOperacion | NewOperacion | PartialUpdateOperacion>(operacion: T): RestOf<T> {
    return {
      ...operacion,
      fechaOperacion: operacion.fechaOperacion?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOperacion: RestOperacion): IOperacion {
    return {
      ...restOperacion,
      fechaOperacion: restOperacion.fechaOperacion ? dayjs(restOperacion.fechaOperacion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOperacion>): HttpResponse<IOperacion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOperacion[]>): HttpResponse<IOperacion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
