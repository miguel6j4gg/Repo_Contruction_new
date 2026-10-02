package pe.edu.uls.cos.demospring.cu.registrarpedido;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import pe.edu.uls.cos.demospring.cu.registrarpedido.exception.StockInsuficienteException;
import pe.edu.uls.cos.demospring.cu.registrarpedido.request.RequestPedido;
import pe.edu.uls.cos.demospring.cu.registrarpedido.request.RequestPedido.RequestPedidoItem;
import pe.edu.uls.cos.demospring.cu.registrarpedido.response.ResponsePedido;
import pe.edu.uls.cos.demospring.dominio.entity.Pedido;
import pe.edu.uls.cos.demospring.dominio.entity.Producto;
import pe.edu.uls.cos.demospring.dominio.repository.RepoPedido;
import pe.edu.uls.cos.demospring.dominio.repository.RepoProducto;

@Service
public class ServiceRegistrarPedido {

    private final RepoProducto repoProducto;
    private final RepoPedido repoPedido;

    public ServiceRegistrarPedido(RepoProducto repoProducto, RepoPedido repoPedido) {
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
    }

    @Transactional
    public ResponsePedido registrarPedido(RequestPedido pedido) {
        Pedido p = new Pedido();
        List<ResponsePedido.ResponsePedidoItem> lst = new ArrayList<>();

        for (RequestPedidoItem item : pedido.items()) {
            System.out.println(System.currentTimeMillis() + " INICIO " + Thread.currentThread().getName());

            Producto producto = repoProducto.findByIdForUpdate(item.idProducto())
                    .orElseThrow(() -> new StockInsuficienteException("El producto con ID " + item.idProducto() + " no existe"));

            System.out.println(System.currentTimeMillis() + " DESPUES DE FIND " + Thread.currentThread().getName() + " stock=" + producto.getStock());

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(System.currentTimeMillis() + " DESPUES DE SLEEP " + Thread.currentThread().getName());

            if (producto.getStock() < item.cantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para el producto: " + producto.getNombre() +
                        ". Disponible: " + producto.getStock() + ", Solicitado: " + item.cantidad());
            }

            p.agregarItem(producto, item.cantidad(), item.precioUnitario());
            lst.add(new ResponsePedido.ResponsePedidoItem(producto.getNombre(), item.cantidad()));

            producto.setStock(producto.getStock() - item.cantidad());
            repoProducto.save(producto);
        }

        repoPedido.save(p);
        return new ResponsePedido(p.getId(), lst);
    }
}