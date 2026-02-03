package org.senai.cantina_vidal.controller;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.senai.cantina_vidal.dto.UploadResponseDTO;
import org.senai.cantina_vidal.service.FileStorageService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/uploads")
@Tag(name = "5. Uploads", description = "Upload de imagens para produtos e categorias")
@SecurityRequirement(name = "bearer-key")
public class UploadController {
    private final FileStorageService service;

    @Operation(summary = "Enviar Imagem", description = "Aceita arquivos **JPG, PNG, JPEG, WEBP**.")
    @ApiResponse(responseCode = "200", description = "Upload realizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou malicioso")
    @ApiResponse(responseCode = "413", description = "Arquivo muito grande (Max 5MB)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        String relativePath = service.storeFile(file);

        return ResponseEntity.ok(new UploadResponseDTO(
                file.getOriginalFilename(),
                relativePath,
                file.getContentType(),
                file.getSize()
        ));
    }
}
