package pe.edu.vallegrande.mspedidos.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import pe.edu.vallegrande.mspedidos.application.port.out.IPedidoRepositoryPort;
import pe.edu.vallegrande.mspedidos.domain.model.Pedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PedidoRepositoryAdapter implements IPedidoRepositoryPort {

    private final PedidoRepository pedidoRepository;

    @Override
    public Flux<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Mono<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Mono<Pedido> save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

}
