package org.senai.cantina_vidal.controller;

import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.UploadApi;
import org.senai.cantina_vidal.dto.UploadResponseDTO;
import org.senai.cantina_vidal.service.FileStorageService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/uploads")
public class UploadController implements UploadApi {
    private final FileStorageService service;

    @Override
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
