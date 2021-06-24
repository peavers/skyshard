package io.skyshard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class RobotConfig {

    @Bean
    public Robot robot() {

        try {
            final Robot robot = new Robot();
            robot.setAutoWaitForIdle(true);

            return robot;
        } catch (final AWTException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
