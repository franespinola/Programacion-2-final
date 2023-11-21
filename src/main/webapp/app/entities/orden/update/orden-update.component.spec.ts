import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrdenFormService } from './orden-form.service';
import { OrdenService } from '../service/orden.service';
import { IOrden } from '../orden.model';
import { IOperacion } from 'app/entities/operacion/operacion.model';
import { OperacionService } from 'app/entities/operacion/service/operacion.service';

import { OrdenUpdateComponent } from './orden-update.component';

describe('Orden Management Update Component', () => {
  let comp: OrdenUpdateComponent;
  let fixture: ComponentFixture<OrdenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ordenFormService: OrdenFormService;
  let ordenService: OrdenService;
  let operacionService: OperacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrdenUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrdenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordenFormService = TestBed.inject(OrdenFormService);
    ordenService = TestBed.inject(OrdenService);
    operacionService = TestBed.inject(OperacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call operacion query and add missing value', () => {
      const orden: IOrden = { id: 456 };
      const operacion: IOperacion = { id: 90931 };
      orden.operacion = operacion;

      const operacionCollection: IOperacion[] = [{ id: 91824 }];
      jest.spyOn(operacionService, 'query').mockReturnValue(of(new HttpResponse({ body: operacionCollection })));
      const expectedCollection: IOperacion[] = [operacion, ...operacionCollection];
      jest.spyOn(operacionService, 'addOperacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(operacionService.query).toHaveBeenCalled();
      expect(operacionService.addOperacionToCollectionIfMissing).toHaveBeenCalledWith(operacionCollection, operacion);
      expect(comp.operacionsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orden: IOrden = { id: 456 };
      const operacion: IOperacion = { id: 67929 };
      orden.operacion = operacion;

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(comp.operacionsCollection).toContain(operacion);
      expect(comp.orden).toEqual(orden);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrden>>();
      const orden = { id: 123 };
      jest.spyOn(ordenFormService, 'getOrden').mockReturnValue(orden);
      jest.spyOn(ordenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orden }));
      saveSubject.complete();

      // THEN
      expect(ordenFormService.getOrden).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordenService.update).toHaveBeenCalledWith(expect.objectContaining(orden));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrden>>();
      const orden = { id: 123 };
      jest.spyOn(ordenFormService, 'getOrden').mockReturnValue({ id: null });
      jest.spyOn(ordenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orden }));
      saveSubject.complete();

      // THEN
      expect(ordenFormService.getOrden).toHaveBeenCalled();
      expect(ordenService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrden>>();
      const orden = { id: 123 };
      jest.spyOn(ordenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordenService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOperacion', () => {
      it('Should forward to operacionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(operacionService, 'compareOperacion');
        comp.compareOperacion(entity, entity2);
        expect(operacionService.compareOperacion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
