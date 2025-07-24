package tech.stl.hcm.candidate.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    
    String storeFile(MultipartFile file, String candidateId, String documentType) throws IOException;
    
    Path loadFileAsPath(String fileName);
    
    void deleteFile(String fileName);
    
    boolean isValidFileType(String fileName, String allowedExtensions);
    
    boolean isValidFileSize(long fileSize, long maxSize);
    
    String getFileExtension(String fileName);
    
    String generateUniqueFileName(String originalFileName, String candidateId, String documentType);
} 