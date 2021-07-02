package io.skyshard.configuration;

import java.awt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
