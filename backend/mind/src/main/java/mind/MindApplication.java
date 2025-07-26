package mind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class MindApplication {
	public static void main(String[] args) {
		SpringApplication.run(MindApplication.class, args);
	}

}
