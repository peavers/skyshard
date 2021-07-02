package io.skyshard.tasks;

import io.skyshard.properties.AppProperties;
import io.skyshard.services.AttackService;
import io.skyshard.services.FindTargetService;
import io.skyshard.services.ScreenshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUp {

  private final AppProperties appProperties;

  private final ScreenshotService screenshotService;

  private final FindTargetService findTargetService;

  private final AttackService attackService;

  @Scheduled(fixedDelay = 250, initialDelay = 0)
  public void process() {

    final var screenshot = screenshotService.take();

    if (appProperties.isSingleTargetMode()) {
      findTargetService.findSingleTarget(screenshot).ifPresent((attackService::attack));
    } else {
      findTargetService.findMultipleTarget(screenshot).forEach(attackService::attack);
    }
  }
}
