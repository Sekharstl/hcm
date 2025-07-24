package tech.stl.hcm.vendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(
    info = @Info(
        title = "Vendor Service API",
        version = "1.0.0",
        description = "API documentation for the Vendor Service.\n\nCopyright STLDIGITAL 2025",
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
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"tech.stl.hcm.vendor", "tech.stl.hcm.message.broker"})
@EnableJpaRepositories("tech.stl.hcm.common.db.repositories")
@EntityScan("tech.stl.hcm.common.db.entities")
public class VendorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VendorServiceApplication.class, args);
    }
} 