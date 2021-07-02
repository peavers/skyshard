package io.skyshard.configuration;

import java.awt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RobotConfig {

  /**
   * Create a singleton robot, the robot is used for moving the mouse pointer as well as keyboard
   * inputs.
   */
  @Bean
  public Robot robot() {

    try {
      final var robot = new Robot();
      robot.setAutoWaitForIdle(true);

      return robot;
    } catch (final Exception exception) {
      throw new RuntimeException(exception.getMessage());
    }
  }
}
