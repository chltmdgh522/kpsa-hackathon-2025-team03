package mind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class MindApplication {
	public static void main(String[] args) {
		SpringApplication.run(MindApplication.class, args);
	}

}
