package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.senai.cantina_vidal.dto.product.ProductCustomerResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "2. Produtos (Catálogo)", description = "Visualização pública de produtos disponíveis")
public interface ProductApi {

    @Operation(summary = "Cardápio do Dia", description = "Lista produtos disponíveis, com estoque > 0 e não deletados.")
    ResponseEntity<List<ProductCustomerResponseDTO>> findAll();

    @Operation(summary = "Detalhes do Produto", description = "Exibe detalhes de um item específico do cardápio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado ou indisponível")
    })
    ResponseEntity<ProductCustomerResponseDTO> findById(Long id);
}
