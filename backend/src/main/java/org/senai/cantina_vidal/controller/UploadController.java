package org.senai.cantina_vidal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.UploadResponseDTO;
import org.senai.cantina_vidal.service.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/uploads")
public class UploadController {
    private final FileStorageService service;


    @Operation(
            description = "Aceita arquivos de imagem (**JPG, PNG, JPEG, WEBP**)."
    )
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
