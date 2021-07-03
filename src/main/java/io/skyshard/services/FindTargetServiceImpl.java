package io.skyshard.services;

import io.skyshard.domain.Target;
import io.skyshard.properties.SkyshardProperties;
import io.skyshard.utils.MatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindTargetServiceImpl implements FindTargetService {

    private final SkyshardProperties skyshardProperties;

    private final Mat template;

    @Override
    public List<Target> findMultipleTarget(final Mat source) {

        return processMultipleTargets(source);
    }

    @Override
    public Optional<Target> findSingleTarget(final Mat source) {

        return processSingleTarget(source);
    }

    /**
     * Find the first target on the screen. This works better with fast moving targets since you're
     * just hitting one before looking again to see if anything has moved.
     */
    private Optional<Target> processSingleTarget(final Mat source) {

        final var result = matchTemplate(source);
        final var minMaxLocResult = Core.minMaxLoc(result);
        final var maxLoc = minMaxLocResult.maxLoc;
        final var point = new Point(maxLoc.x + template.cols(), maxLoc.y + template.rows());

        return minMaxLocResult.maxVal >= skyshardProperties.getMatchThreshold()
                ? Optional.of(Target.builder().point(point).build())
                : Optional.empty();
    }

    /**
     * Find all the targets on the screen. This is probably faster if you've got a lot of targets on
     * the screen which don't move. You can attack each one before taking a new screenshot.
     *
     * <p>If you want to debug the screenshot and matching, you'll need to use this method for
     * outputs.
     */
    private List<Target> processMultipleTargets(final Mat source) {

        final var result = matchTemplate(source);
        final List<Target> targets = new ArrayList<>();

        double maxValue;
        Mat destination;

        while (true) {
            final var minMaxLocResult = Core.minMaxLoc(result);
            final var maxLoc = minMaxLocResult.maxLoc;

            maxValue = minMaxLocResult.maxVal;
            destination = source.clone();

            if (maxValue >= skyshardProperties.getMatchThreshold()) {
                final var point = new Point(maxLoc.x + template.cols(), maxLoc.y + template.rows());

                // Update the pointer location to the next target
                Imgproc.rectangle(result, maxLoc, point, new Scalar(0, 255, 0), -1);

                if (skyshardProperties.isDebug()) {
                    MatUtils.drawRectangle(source, maxLoc, point);
                }

                if (isDuplicate(targets, point)) {
                    continue;
                }

                targets.add(Target.builder().point(point).build());
            } else {
                break;
            }
        }

        if (skyshardProperties.isDebug()) {
            MatUtils.write(destination, "match");
        }

        return targets;
    }

    /**
     * Preform the match between the source and the template file.
     */
    private Mat matchTemplate(final Mat source) {

        final var result = new Mat();

        Imgproc.matchTemplate(source, template, result, Imgproc.TM_CCOEFF_NORMED);
        Imgproc.threshold(result, result, 0.1, 1, Imgproc.THRESH_TOZERO);

        return result;
    }

    /**
     * Decide if a template match is duplicate based on it being within proximity to another match.
     * Only used for {@link FindTargetService#findMultipleTarget(Mat)}.
     */
    private boolean isDuplicate(final List<Target> targets, final Point point) {

        final double threshold = skyshardProperties.getDuplicateThreshold();

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
