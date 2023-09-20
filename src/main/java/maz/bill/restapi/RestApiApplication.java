package maz.bill.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({"maz.bill.restapi.controller", "maz.bill.restapi.service","maz.bill.restapi.repository"})
@EntityScan("maz.bill.restapi.entity")
@EnableJpaRepositories("maz.bill.restapi.repository")
//@EnableSwagger2
public class RestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

}
