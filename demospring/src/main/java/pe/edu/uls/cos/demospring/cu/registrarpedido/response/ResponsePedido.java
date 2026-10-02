package pe.edu.uls.cos.demospring.cu.registrarpedido.response;

import java.util.List;

/**
 * ResponsePedido
 */
public record ResponsePedido(int idPedido, List<ResponsePedidoItem> items) {

    public record ResponsePedidoItem(String nombre, int cantidad) {}
}