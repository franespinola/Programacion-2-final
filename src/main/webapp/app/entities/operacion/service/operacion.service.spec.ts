import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOperacion } from '../operacion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../operacion.test-samples';

import { OperacionService, RestOperacion } from './operacion.service';

const requireRestSample: RestOperacion = {
  ...sampleWithRequiredData,
  fechaOperacion: sampleWithRequiredData.fechaOperacion?.toJSON(),
};

describe('Operacion Service', () => {
  let service: OperacionService;
  let httpMock: HttpTestingController;
  let expectedResult: IOperacion | IOperacion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperacionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Operacion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const operacion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(operacion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Operacion', () => {
      const operacion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(operacion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Operacion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Operacion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Operacion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOperacionToCollectionIfMissing', () => {
      it('should add a Operacion to an empty array', () => {
        const operacion: IOperacion = sampleWithRequiredData;
        expectedResult = service.addOperacionToCollectionIfMissing([], operacion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operacion);
      });

      it('should not add a Operacion to an array that contains it', () => {
        const operacion: IOperacion = sampleWithRequiredData;
        const operacionCollection: IOperacion[] = [
          {
            ...operacion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOperacionToCollectionIfMissing(operacionCollection, operacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Operacion to an array that doesn't contain it", () => {
        const operacion: IOperacion = sampleWithRequiredData;
        const operacionCollection: IOperacion[] = [sampleWithPartialData];
        expectedResult = service.addOperacionToCollectionIfMissing(operacionCollection, operacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operacion);
      });

      it('should add only unique Operacion to an array', () => {
        const operacionArray: IOperacion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const operacionCollection: IOperacion[] = [sampleWithRequiredData];
        expectedResult = service.addOperacionToCollectionIfMissing(operacionCollection, ...operacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operacion: IOperacion = sampleWithRequiredData;
        const operacion2: IOperacion = sampleWithPartialData;
        expectedResult = service.addOperacionToCollectionIfMissing([], operacion, operacion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operacion);
        expect(expectedResult).toContain(operacion2);
      });

      it('should accept null and undefined values', () => {
        const operacion: IOperacion = sampleWithRequiredData;
        expectedResult = service.addOperacionToCollectionIfMissing([], null, operacion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operacion);
      });

      it('should return initial array if no Operacion is added', () => {
        const operacionCollection: IOperacion[] = [sampleWithRequiredData];
        expectedResult = service.addOperacionToCollectionIfMissing(operacionCollection, undefined, null);
        expect(expectedResult).toEqual(operacionCollection);
      });
    });

    describe('compareOperacion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOperacion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOperacion(entity1, entity2);
        const compareResult2 = service.compareOperacion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOperacion(entity1, entity2);
        const compareResult2 = service.compareOperacion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOperacion(entity1, entity2);
        const compareResult2 = service.compareOperacion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
