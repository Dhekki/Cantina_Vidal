package org.senai.cantina_vidal.service;

import org.apache.tika.Tika;
import org.senai.cantina_vidal.exception.FileStorageException;
import org.senai.cantina_vidal.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Tika tika = new Tika();
    private final Path fileStorageLocation;
    private final List<String> allowedMimeTypes = List.of("image/jpeg", "image/jpg", "image/png", "image/webp");

    public FileStorageService(@Value("${upload.path}") String uploadDir) {
        fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("Não foi possível criar o diretório de upload em: " + uploadDir, e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty())
            throw new InvalidFileException("Falha ao armazenar arquivo vazio.");

        try {
            String detectedType = tika.detect(file.getInputStream());

            if (!allowedMimeTypes.contains(detectedType))
                throw new InvalidFileException("Arquivo inválido/malicioso. O tipo real detectado foi: " + detectedType);
        } catch (IOException e) {
            throw new FileStorageException("Erro ao verificar integridade do arquivo", e);
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank())
            throw new InvalidFileException("O arquivo enviado não possui nome ou o nome está vazio");

        originalFilename = StringUtils.cleanPath(originalFilename);

        if (originalFilename.contains(".."))
            throw new InvalidFileException("Nome de arquivo inválido (contém caminho relativo): " + originalFilename);

        String extension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID() + "." + extension;

        try {
            Path targetLocation = fileStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + newFilename;
        } catch (IOException e) {
            throw new FileStorageException("Não foi possível armazenar o arquivo " + newFilename, e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1)
            return "jpg";
        
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
