package pe.edu.vallegrande.mspedidos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    private Long id;
    private String codigo;
    private String nombre;
    private Double price;
    private Integer stock;

}
