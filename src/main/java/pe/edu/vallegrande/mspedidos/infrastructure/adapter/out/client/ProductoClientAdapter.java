   package pe.edu.vallegrande.mspedidos.infrastructure.adapter.out.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.mspedidos.application.port.out.IProductoClientPort;
import pe.edu.vallegrande.mspedidos.domain.model.Producto;
import reactor.core.publisher.Mono;

@Component
public class ProductoClientAdapter implements IProductoClientPort {

    private final WebClient webClient;

    public ProductoClientAdapter(WebClient.Builder webClientBuilder,
                                  @Value("${servicios.productos-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<Producto> findById(Long id) {
        return webClient.get()
                .uri("/api/productos/{id}", id)
                .retrieve()
                .bodyToMono(Producto.class);
    }

    @Override
    public Mono<Producto> decreaseStock(Long id, Integer quantity) {
        return webClient.put()
                .uri("/api/productos/{id}/stock", id)
                .bodyValue(quantity)
                .retrieve()
                .bodyToMono(Producto.class);
    }

}
