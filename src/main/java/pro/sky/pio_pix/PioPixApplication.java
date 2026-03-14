package pro.sky.pio_pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PioPixApplication {

	public static void main(String[] args) {
		SpringApplication.run(PioPixApplication.class, args);
	}

}
