package tech.stl.hcm.position;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(
        title = "Position Service API",
        version = "1.0.0",
        description = "API documentation for the Position Service.\n\nCopyright STLDIGITAL 2025",
        contact = @Contact(
            name = "STLDIGITAL",
            url = "https://www.stldigital.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://springdoc.org"
        )
    )
)
@SpringBootApplication
public class PositionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PositionServiceApplication.class, args);
    }
} 