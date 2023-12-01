package cn.lz.data.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final Environment environment;

    public Application(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        String[] pros = environment.getActiveProfiles();
        List<String> activeProfiles = Arrays.asList(pros);
        log.info("\n================ activeProfiles list is {}================", activeProfiles);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        int beanCount = ctx.getBeanDefinitionCount();
        log.info("\n========================================= bean 数量为 ：{}", beanCount);
        Environment env = ctx.getEnvironment();
        String port = env.getProperty("server.port");
        log.info("\n========================================= port is {}", port);
        String appName = env.getProperty("spring.application.name");
        log.info("\n========================================= application-name is {}", appName);
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n========================================= context-path is {}", path);
        log.info("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        log.info("\n\n\t=========== 项目启动成功！url:[http://127.0.0.1:" + port + "]==========\n\n");
        log.info("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

}
