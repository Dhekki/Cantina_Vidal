package org.senai.cantina_vidal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadResponseDTO(
        String fileName,
        String fileDownloadUri,

        @Schema(description = "Tipo MIME do arquivo armazenado", example = "image/png")
        String fileType,

        @Schema(description = "Tamanho do arquivo em Bytes", example = "40960")
        long size
) {
}
