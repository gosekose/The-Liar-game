package liar.waitservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WaitServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaitServiceApplication.class, args);
    }

}
