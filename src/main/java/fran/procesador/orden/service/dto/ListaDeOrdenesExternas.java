package fran.procesador.orden.service.dto;

import java.util.List;

public class ListaDeOrdenesExternas {

    private List<OrdenExternaDTO> ordenes;

    public List<OrdenExternaDTO> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<OrdenExternaDTO> ordenes) {
        this.ordenes = ordenes;
    }

    @Override
    public String toString() {
        return "ListaDeOrdenesExternas{" + "ordenes=" + ordenes + '}';
    }
}
