package io.skyshard.services;

import io.skyshard.domain.Target;
import io.skyshard.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttackServiceImpl implements AttackService {

  private final Robot robot;

  /**
   * Move the mouse to the target location, then offset it slightly to counter the fact we always
   * find the very bottom right/left corner of a template match. Once the mouse has been moved over
   * the target, press the '1' keyboard button.
   */
  @Override
  public void attack(final Target target) {

    robot.mouseMove(target.x() - 5, target.y() - 5);
    robot.keyPress(KeyEvent.VK_1);
    robot.delay(MathUtils.random(25, 50));
    robot.keyRelease(KeyEvent.VK_1);
  }
}
