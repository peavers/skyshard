package io.skyshard.services;

import io.skyshard.domain.Target;
import io.skyshard.utils.MathUtils;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttackServiceImpl implements AttackService {

  private final Robot robot;

  @Override
  public void attack(final Target target) {

    process(target, false);
  }

  @Override
  public void attack(final List<Target> targets) {

    targets.forEach(target -> process(target, true));
  }

  private void process(final Target target, final boolean pause) {

    if (target == null) {
      return;
    }

    robot.mouseMove(target.x() - 5, target.y() - 5);
    robot.keyPress(KeyEvent.VK_1);
    robot.delay(MathUtils.random(25, 50));
    robot.keyRelease(KeyEvent.VK_1);

    if (pause) {
      pause();
    }
  }

  @SneakyThrows
  private void pause() {

    TimeUnit.MILLISECONDS.sleep(MathUtils.random(200, 300));
  }
}
