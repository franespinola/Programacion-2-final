import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperacionFormService } from './operacion-form.service';
import { OperacionService } from '../service/operacion.service';
import { IOperacion } from '../operacion.model';

import { OperacionUpdateComponent } from './operacion-update.component';

describe('Operacion Management Update Component', () => {
  let comp: OperacionUpdateComponent;
  let fixture: ComponentFixture<OperacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operacionFormService: OperacionFormService;
  let operacionService: OperacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperacionUpdateComponent],
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
      .overrideTemplate(OperacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operacionFormService = TestBed.inject(OperacionFormService);
    operacionService = TestBed.inject(OperacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const operacion: IOperacion = { id: 456 };

      activatedRoute.data = of({ operacion });
      comp.ngOnInit();

      expect(comp.operacion).toEqual(operacion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperacion>>();
      const operacion = { id: 123 };
      jest.spyOn(operacionFormService, 'getOperacion').mockReturnValue(operacion);
      jest.spyOn(operacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operacion }));
      saveSubject.complete();

      // THEN
      expect(operacionFormService.getOperacion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(operacionService.update).toHaveBeenCalledWith(expect.objectContaining(operacion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperacion>>();
      const operacion = { id: 123 };
      jest.spyOn(operacionFormService, 'getOperacion').mockReturnValue({ id: null });
      jest.spyOn(operacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operacion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operacion }));
      saveSubject.complete();

      // THEN
      expect(operacionFormService.getOperacion).toHaveBeenCalled();
      expect(operacionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperacion>>();
      const operacion = { id: 123 };
      jest.spyOn(operacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operacionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
