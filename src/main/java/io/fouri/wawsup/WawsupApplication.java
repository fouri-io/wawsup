package io.fouri.wawsup;

import io.fouri.wawsup.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
public class WawsupApplication {

	private final Environment env;

	public WawsupApplication(Environment env) {
		this.env = env;
	}

	/**
	 * Initializes Wawsup.
	 * <p>
	 * Can be controlled via command line argument
	 * mvn spring-boot:run -Dspring-boot.run.profiles=dev
	 */
	@PostConstruct
	public void initApplication() {

	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(WawsupApplication.class);
		Environment env = app.run(args).getEnvironment();
	}

}
