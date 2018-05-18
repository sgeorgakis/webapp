package com.dotsub.webapp;

import com.dotsub.webapp.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties(ApplicationProperties.class)
public class WebApp {

    private static final Logger LOG = LoggerFactory.getLogger(WebApp.class);

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(WebApp.class);
        Environment env = app.run(args).getEnvironment();

        LOG.info("\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\tlocalhost:{}\n\t"
                        + "External: \t{}://{}\n"
                        + "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }
}
