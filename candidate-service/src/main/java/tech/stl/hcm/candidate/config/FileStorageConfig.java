package tech.stl.hcm.candidate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageConfig {
    
    private String uploadDir = "uploads/candidates";
    private long maxFileSize = 10485760; // 10MB default
    private String allowedExtensions = "pdf,doc,docx,jpg,jpeg,png,zip,rar";
    private boolean createDirectories = true;
    private String tempDir = "temp";
} 