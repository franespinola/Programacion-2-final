package fran.procesador.orden.service.dto;

import java.time.Instant;

public class OrdenExternaDTO {

    private Integer cliente;
    private Integer accionId;
    private String accion;
    private String operacion;
    private Float precio;
    private Integer cantidad;
    private Instant fechaOperacion;
    private ModoOrdenExterna modo;

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public Integer getAccionId() {
        return accionId;
    }

    public void setAccionId(Integer accionId) {
        this.accionId = accionId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(Instant fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public ModoOrdenExterna getModo() {
        return modo;
    }

    public void setModo(ModoOrdenExterna modo) {
        this.modo = modo;
    }

    @Override
    public String toString() {
        return (
            "OrdenExternaDTO{" +
            "cliente=" +
            cliente +
            ", accionId=" +
            accionId +
            ", accion='" +
            accion +
            '\'' +
            ", operacion='" +
            operacion +
            '\'' +
            ", precio=" +
            precio +
            ", cantidad=" +
            cantidad +
            ", fechaOperacion=" +
            fechaOperacion +
            ", modo=" +
            modo +
            '}'
        );
    }
}
