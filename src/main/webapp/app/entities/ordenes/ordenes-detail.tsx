import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ordenes.reducer';

export const OrdenesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ordenesEntity = useAppSelector(state => state.ordenes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ordenesDetailsHeading">
          <Translate contentKey="programacion2App.ordenes.detail.title">Ordenes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ordenesEntity.id}</dd>
          <dt>
            <span id="cliente">
              <Translate contentKey="programacion2App.ordenes.cliente">Cliente</Translate>
            </span>
          </dt>
          <dd>{ordenesEntity.cliente}</dd>
          <dt>
            <span id="accionId">
              <Translate contentKey="programacion2App.ordenes.accionId">Accion Id</Translate>
            </span>
          </dt>
          <dd>{ordenesEntity.accionId}</dd>
          <dt>
            <span id="accion">
              <Translate contentKey="programacion2App.ordenes.accion">Accion</Translate>
            </span>
          </dt>
          <dd>{ordenesEntity.accion}</dd>
          <dt>
            <span id="operacion">
              <Translate contentKey="programacion2App.ordenes.operacion">Operacion</Translate>
            </span>
          </dt>
          <dd>{ordenesEntity.operacion}</dd>
          <dt>
            <span id="precio">
              <Translate contentKey="programacion2App.ordenes.precio">Precio</Translate>
            </span>
          </dt>
          <dd>{ordenesEntity.precio}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="programacion2App.ordenes.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{ordenesEntity.cantidad}</dd>
          <dt>
            <span id="fechaOperacion">
              <Translate contentKey="programacion2App.ordenes.fechaOperacion">Fecha Operacion</Translate>
            </span>
          </dt>
          <dd>
            {ordenesEntity.fechaOperacion ? <TextFormat value={ordenesEntity.fechaOperacion} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="modo">
              <Translate contentKey="programacion2App.ordenes.modo">Modo</Translate>
            </span>
          </dt>
          <dd>{ordenesEntity.modo}</dd>
        </dl>
        <Button tag={Link} to="/ordenes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ordenes/${ordenesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrdenesDetail;
