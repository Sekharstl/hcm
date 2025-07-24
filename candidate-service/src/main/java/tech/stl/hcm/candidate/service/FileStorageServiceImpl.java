package tech.stl.hcm.candidate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.stl.hcm.candidate.config.FileStorageConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageConfig fileStorageConfig;
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
        
        try {
            if (fileStorageConfig.isCreateDirectories()) {
                Files.createDirectories(this.fileStorageLocation);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String candidateId, String documentType) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = generateUniqueFileName(originalFileName, candidateId, documentType);
        
        // Create candidate-specific directory
        Path candidateDir = fileStorageLocation.resolve(candidateId);
        if (!Files.exists(candidateDir)) {
            Files.createDirectories(candidateDir);
        }
        
        Path targetLocation = candidateDir.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        log.info("File stored successfully: {}", targetLocation);
        return fileName;
    }

    @Override
    public Path loadFileAsPath(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                return filePath;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("File not found: " + fileName, ex);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: {}", fileName);
            }
        } catch (IOException ex) {
            log.error("Error deleting file: {}", fileName, ex);
            throw new RuntimeException("Could not delete file: " + fileName, ex);
        }
    }

    @Override
    public boolean isValidFileType(String fileName, String allowedExtensions) {
        String fileExtension = getFileExtension(fileName);
        return Arrays.asList(allowedExtensions.toLowerCase().split(","))
                .contains(fileExtension.toLowerCase());
    }

    @Override
    public boolean isValidFileSize(long fileSize, long maxSize) {
        return fileSize <= maxSize;
    }

    @Override
    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public String generateUniqueFileName(String originalFileName, String candidateId, String documentType) {
        String fileExtension = getFileExtension(originalFileName);
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        
        return String.format("%s_%s_%s_%s.%s", 
                documentType.toLowerCase(), 
                candidateId, 
                timestamp, 
                uniqueId, 
                fileExtension);
    }
} 