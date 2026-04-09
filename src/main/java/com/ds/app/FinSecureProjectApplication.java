package com.ds.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   // ← for @Scheduled salary job
@EnableAsync        // ← for @Async email sending
@EnableCaching
public class FinSecureProjectApplication {
	public static void main(String[] args) {

		SpringApplication.run(FinSecureProjectApplication.class, args);
	}

}
<<<<<<< HEAD



=======
>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
