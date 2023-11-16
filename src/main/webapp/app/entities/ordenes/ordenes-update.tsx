import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrdenes } from 'app/shared/model/ordenes.model';
import { getEntity, updateEntity, createEntity, reset } from './ordenes.reducer';

export const OrdenesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ordenesEntity = useAppSelector(state => state.ordenes.entity);
  const loading = useAppSelector(state => state.ordenes.loading);
  const updating = useAppSelector(state => state.ordenes.updating);
  const updateSuccess = useAppSelector(state => state.ordenes.updateSuccess);

  const handleClose = () => {
    navigate('/ordenes' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.fechaOperacion = convertDateTimeToServer(values.fechaOperacion);

    const entity = {
      ...ordenesEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fechaOperacion: displayDefaultDateTime(),
        }
      : {
          ...ordenesEntity,
          fechaOperacion: convertDateTimeFromServer(ordenesEntity.fechaOperacion),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="programacion2App.ordenes.home.createOrEditLabel" data-cy="OrdenesCreateUpdateHeading">
            <Translate contentKey="programacion2App.ordenes.home.createOrEditLabel">Create or edit a Ordenes</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="ordenes-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('programacion2App.ordenes.cliente')}
                id="ordenes-cliente"
                name="cliente"
                data-cy="cliente"
                type="text"
              />
              <ValidatedField
                label={translate('programacion2App.ordenes.accionId')}
                id="ordenes-accionId"
                name="accionId"
                data-cy="accionId"
                type="text"
              />
              <ValidatedField
                label={translate('programacion2App.ordenes.accion')}
                id="ordenes-accion"
                name="accion"
                data-cy="accion"
                type="text"
              />
              <ValidatedField
                label={translate('programacion2App.ordenes.operacion')}
                id="ordenes-operacion"
                name="operacion"
                data-cy="operacion"
                type="text"
              />
              <ValidatedField
                label={translate('programacion2App.ordenes.precio')}
                id="ordenes-precio"
                name="precio"
                data-cy="precio"
                type="text"
              />
              <ValidatedField
                label={translate('programacion2App.ordenes.cantidad')}
                id="ordenes-cantidad"
                name="cantidad"
                data-cy="cantidad"
                type="text"
              />
              <ValidatedField
                label={translate('programacion2App.ordenes.fechaOperacion')}
                id="ordenes-fechaOperacion"
                name="fechaOperacion"
                data-cy="fechaOperacion"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('programacion2App.ordenes.modo')} id="ordenes-modo" name="modo" data-cy="modo" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ordenes" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrdenesUpdate;
