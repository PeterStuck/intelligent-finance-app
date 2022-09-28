package pl.intelligent.finance.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "pl.intelligent.finance.persistence.entity",
        "pl.intelligent.finance.persistence.entity",
        "pl.intelligent.finance.persistence.repository"
})
@EntityScan(basePackages = "pl.intelligent.finance.persistence.entity.impl")
@EnableJpaRepositories(basePackages = "pl.intelligent.finance.persistence.repository")
public class SomeApp {

    public static void main(String[] args) {
        SpringApplication.run(SomeApp.class, args);
    }

}
