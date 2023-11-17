package fran.um.edu.ar.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fran.um.edu.ar.domain.Ordenes} entity. This class is used
 * in {@link fran.um.edu.ar.web.rest.OrdenesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ordenes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdenesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cliente;

    private IntegerFilter accionId;

    private StringFilter accion;

    private StringFilter operacion;

    private FloatFilter precio;

    private IntegerFilter cantidad;

    private InstantFilter fechaOperacion;

    private StringFilter modo;

    private Boolean distinct;

    public OrdenesCriteria() {}

    public OrdenesCriteria(OrdenesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cliente = other.cliente == null ? null : other.cliente.copy();
        this.accionId = other.accionId == null ? null : other.accionId.copy();
        this.accion = other.accion == null ? null : other.accion.copy();
        this.operacion = other.operacion == null ? null : other.operacion.copy();
        this.precio = other.precio == null ? null : other.precio.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.fechaOperacion = other.fechaOperacion == null ? null : other.fechaOperacion.copy();
        this.modo = other.modo == null ? null : other.modo.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrdenesCriteria copy() {
        return new OrdenesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCliente() {
        return cliente;
    }

    public IntegerFilter cliente() {
        if (cliente == null) {
            cliente = new IntegerFilter();
        }
        return cliente;
    }

    public void setCliente(IntegerFilter cliente) {
        this.cliente = cliente;
    }

    public IntegerFilter getAccionId() {
        return accionId;
    }

    public IntegerFilter accionId() {
        if (accionId == null) {
            accionId = new IntegerFilter();
        }
        return accionId;
    }

    public void setAccionId(IntegerFilter accionId) {
        this.accionId = accionId;
    }

    public StringFilter getAccion() {
        return accion;
    }

    public StringFilter accion() {
        if (accion == null) {
            accion = new StringFilter();
        }
        return accion;
    }

    public void setAccion(StringFilter accion) {
        this.accion = accion;
    }

    public StringFilter getOperacion() {
        return operacion;
    }

    public StringFilter operacion() {
        if (operacion == null) {
            operacion = new StringFilter();
        }
        return operacion;
    }

    public void setOperacion(StringFilter operacion) {
        this.operacion = operacion;
    }

    public FloatFilter getPrecio() {
        return precio;
    }

    public FloatFilter precio() {
        if (precio == null) {
            precio = new FloatFilter();
        }
        return precio;
    }

    public void setPrecio(FloatFilter precio) {
        this.precio = precio;
    }

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            cantidad = new IntegerFilter();
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public InstantFilter getFechaOperacion() {
        return fechaOperacion;
    }

    public InstantFilter fechaOperacion() {
        if (fechaOperacion == null) {
            fechaOperacion = new InstantFilter();
        }
        return fechaOperacion;
    }

    public void setFechaOperacion(InstantFilter fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public StringFilter getModo() {
        return modo;
    }

    public StringFilter modo() {
        if (modo == null) {
            modo = new StringFilter();
        }
        return modo;
    }

    public void setModo(StringFilter modo) {
        this.modo = modo;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrdenesCriteria that = (OrdenesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cliente, that.cliente) &&
            Objects.equals(accionId, that.accionId) &&
            Objects.equals(accion, that.accion) &&
            Objects.equals(operacion, that.operacion) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(fechaOperacion, that.fechaOperacion) &&
            Objects.equals(modo, that.modo) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cliente, accionId, accion, operacion, precio, cantidad, fechaOperacion, modo, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdenesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cliente != null ? "cliente=" + cliente + ", " : "") +
            (accionId != null ? "accionId=" + accionId + ", " : "") +
            (accion != null ? "accion=" + accion + ", " : "") +
            (operacion != null ? "operacion=" + operacion + ", " : "") +
            (precio != null ? "precio=" + precio + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (fechaOperacion != null ? "fechaOperacion=" + fechaOperacion + ", " : "") +
            (modo != null ? "modo=" + modo + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
