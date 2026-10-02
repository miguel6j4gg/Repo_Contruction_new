package pe.edu.uls.cos.demospring.cu.registrarpedido;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.uls.cos.demospring.cu.registrarpedido.request.RequestPedido;
import pe.edu.uls.cos.demospring.cu.registrarpedido.request.RequestPedido.RequestPedidoItem;
import pe.edu.uls.cos.demospring.cu.registrarpedido.response.ResponsePedido;
import pe.edu.uls.cos.demospring.dominio.entity.Pedido;
import pe.edu.uls.cos.demospring.dominio.entity.Producto;
import pe.edu.uls.cos.demospring.dominio.repository.RepoPedido;
import pe.edu.uls.cos.demospring.dominio.repository.RepoProducto;

@Service 
public class ServiceRegistrarPedido {

    RepoProducto repoProducto;

    RepoPedido repoPedido;

    public ServiceRegistrarPedido(RepoProducto repoProducto, RepoPedido repoPedido) {
        this.repoProducto = repoProducto;
        this.repoPedido = repoPedido;
    }

    public ResponsePedido registrarPedido(RequestPedido pedido) {
        Pedido p = new Pedido();    
        List<ResponsePedido.ResponsePedidoItem> lst = new ArrayList<ResponsePedido.ResponsePedidoItem>();
        for (RequestPedidoItem item : pedido.items()) {
            Producto producto = repoProducto.findById(item.idProducto()).get();
            p.agregarItem(producto, item.cantidad(), item.precioUnitario());
            lst.add(new ResponsePedido.ResponsePedidoItem(producto.getNombre(), item.cantidad()));
     }
        repoPedido.save(p);
        ResponsePedido respPedido = new ResponsePedido(p.getId(), lst);
        return respPedido;
    }
}
