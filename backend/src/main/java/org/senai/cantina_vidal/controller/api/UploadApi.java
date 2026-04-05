package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.senai.cantina_vidal.dto.UploadResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "5. Uploads", description = "Upload de imagens para produtos e categorias")
@SecurityRequirement(name = "cookieAuth")
public interface UploadApi {

    @Operation(summary = "Enviar Imagem", description = "Aceita arquivos **JPG, PNG, JPEG, WEBP**.")
    @ApiResponse(responseCode = "200", description = "Upload realizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou malicioso")
    @ApiResponse(responseCode = "413", description = "Arquivo muito grande (Max 5MB)")
    ResponseEntity<UploadResponseDTO> uploadFile(MultipartFile file);
}
