package io.skyshard.services;

import io.skyshard.domain.Target;
import io.skyshard.properties.AppProperties;
import io.skyshard.utils.MatUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindTargetServiceImpl implements FindTargetService {

  private final AppProperties appProperties;

  private final Mat template;

  @Override
  public List<Target> findMultipleTarget(final Mat source) {

    return processMultipleTargets(source);
  }

  @Override
  public Target findSingleTarget(final Mat source) {

    return processSingleTarget(source);
  }

  public Target processSingleTarget(final Mat source) {
    final Mat result = matchTemplate(source);

    final Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);

    double maxValue = minMaxLocResult.maxVal;

    final Point maxLoc = minMaxLocResult.maxLoc;
    final Point point = new Point(maxLoc.x + template.cols(), maxLoc.y + template.rows());

    if (maxValue >= appProperties.getMatchThreshold()) {
      return Target.builder().point(point).build();
    } else {
      return null;
    }
  }

  public List<Target> processMultipleTargets(final Mat source) {
    final Mat result = matchTemplate(source);

    final List<Target> targets = new ArrayList<>();

    double maxValue;
    Mat destination;

    while (true) {
      final Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);
      final Point maxLoc = minMaxLocResult.maxLoc;

      maxValue = minMaxLocResult.maxVal;
      destination = source.clone();

      if (maxValue >= appProperties.getMatchThreshold()) {
        final Point point = new Point(maxLoc.x + template.cols(), maxLoc.y + template.rows());

        // Update the pointer location
        Imgproc.rectangle(result, maxLoc, point, new Scalar(0, 255, 0), -1);
        drawMatch(source, maxLoc, point);

        if (isDuplicate(targets, point)) {
          continue;
        }

        targets.add(Target.builder().point(point).build());
      } else {
        break;
      }
    }

    writeMatchToDisk(destination);

    return targets;
  }

  /** Preform the match between the source and the template file. */
  private Mat matchTemplate(final Mat source) {
    final Mat result = new Mat();

    final Mat normalizeSource = MatUtils.normalize(source);
    final Mat normalizeSample = MatUtils.normalize(template);
    final Mat normalizeResult = MatUtils.normalize(result);

    Imgproc.matchTemplate(
        normalizeSource, normalizeSample, normalizeResult, Imgproc.TM_CCOEFF_NORMED);
    Imgproc.threshold(normalizeResult, normalizeResult, 0.1, 1, Imgproc.THRESH_TOZERO);

    return normalizeResult;
  }

  /** Draw on the source image the location of the match. */
  private void drawMatch(final Mat source, final Point maxLoc, final Point point) {
    if (appProperties.isDebug()) {
      Imgproc.rectangle(source, maxLoc, point, new Scalar(0, 255, 0), 5);
      Imgproc.putText(
          source,
          point.x + "," + point.y,
          point,
          Imgproc.FONT_HERSHEY_PLAIN,
          2.0,
          new Scalar(0, 255, 0),
          1);
    }
  }

  /** Flush the match file to disk. This is slow. */
  private void writeMatchToDisk(final Mat destination) {
    if (appProperties.isDebug()) {
      Imgcodecs.imwrite(Instant.now().toEpochMilli() + "-match.jpg", destination);
    }
  }

  /**
   * Decide if a template match is duplicate based on it being within proximity to another match.
   * Only used for {@link FindTargetService#findMultipleTarget(Mat)}.
   */
  private boolean isDuplicate(final List<Target> targets, final Point point) {

    final double threshold = appProperties.getDuplicateThreshold();

    return targets.stream()
        .anyMatch(
            target ->
                Range.between(target.getPoint().x - threshold, target.getPoint().x + threshold)
                        .contains(point.x)
                    && Range.between(
                            target.getPoint().y - threshold, target.getPoint().y + threshold)
                        .contains(point.y));
  }
}
