package pe.edu.vallegrande.mspedidos.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.vallegrande.mspedidos.application.port.out.IPedidoRepositoryPort;
import pe.edu.vallegrande.mspedidos.infrastructure.adapter.out.persistence.PedidoRepository;
import pe.edu.vallegrande.mspedidos.infrastructure.adapter.out.persistence.PedidoRepositoryAdapter;

@Configuration
public class AppConfig {

    @Bean
    public IPedidoRepositoryPort pedidoRepositoryPort(PedidoRepository pedidoRepository) {
        return new PedidoRepositoryAdapter(pedidoRepository);
    }

}
