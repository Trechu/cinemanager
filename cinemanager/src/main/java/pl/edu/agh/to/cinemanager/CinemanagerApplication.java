package pl.edu.agh.to.cinemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.edu.agh.to.cinemanager.configuration.CorsProperties;
import pl.edu.agh.to.cinemanager.configuration.RsaKeyProperties;

@EnableConfigurationProperties({RsaKeyProperties.class, CorsProperties.class})
@SpringBootApplication
public class CinemanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemanagerApplication.class, args);
    }

}
