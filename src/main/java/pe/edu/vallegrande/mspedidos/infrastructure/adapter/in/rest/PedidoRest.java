package pe.edu.vallegrande.mspedidos.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import pe.edu.vallegrande.mspedidos.application.port.in.IPedidoServicePort;
import pe.edu.vallegrande.mspedidos.domain.model.Pedido;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoRest {

    private final IPedidoServicePort pedidoService;

    @GetMapping
    public Flux<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Pedido> findById(@PathVariable Long id) {
        return pedidoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pedido> create(@RequestBody Pedido order) {
        return pedidoService.create(order);
    }

    @DeleteMapping("/{id}")
    public Mono<Pedido> cancel(@PathVariable Long id) {
        return pedidoService.cancel(id);
    }

}
