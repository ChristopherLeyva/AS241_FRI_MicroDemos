package pe.edu.vallegrande.mspedidos.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.edu.vallegrande.mspedidos.domain.model.Pedido;

public interface PedidoRepository extends ReactiveCrudRepository<Pedido, Long> {

}
