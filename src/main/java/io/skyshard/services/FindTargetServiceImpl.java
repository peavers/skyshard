package io.skyshard.services;

import io.skyshard.domain.Target;
import io.skyshard.properties.AppProperties;
import io.skyshard.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindTargetServiceImpl implements FindTargetService {

    private final AppProperties appProperties;

    private final Mat template = loadTemplate();

    @Override
    public List<Target> findMultipleTarget(final Mat source) {

        return processMultipleTargets(source, template);
    }

    @Override
    public Target findSingleTarget(final Mat source) {

        return processSingleTarget(source, template);
    }

    public Target processSingleTarget(final Mat source, final Mat template) {

        final Mat result = matchTemplate(source);

        Target target = null;

        double maxValue;
        Mat destination;

        // TODO should be able to remove this while loop fo single targets
        while (true) {
            final Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);
            final Point maxLoc = minMaxLocResult.maxLoc;
            maxValue = minMaxLocResult.maxVal;
            destination = source.clone();

            if (maxValue >= appProperties.getMatchThreshold()) {
                final Point point = new Point(maxLoc.x + template.cols(), maxLoc.y + template.rows());

                // Update the pointer location
                Imgproc.rectangle(result, maxLoc, point, new Scalar(0, 255, 0), -1);
                drawMatch(source, maxLoc, point, appProperties.isDebug());

                target = Target.builder().point(point).build();

            } else {
                break;
            }
        }

        writeMatchToDisk(destination);

        return target;
    }

    public List<Target> processMultipleTargets(final Mat source, final Mat template) {

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
                drawMatch(source, maxLoc, point, Constants.WRITE_TO_DISK);

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

    /**
     * Read in the matching template file from the resource directory. If this can't be found or its not set in the
     * application properties file, exit the application.
     */
    private Mat loadTemplate() {

        if (StringUtils.isEmpty(appProperties.getTemplate())) {
            log.error("Cannot find template file {}, exiting...", appProperties.getTemplate());
            System.exit(1);
        }

        try {
            final File templateSource =
                    ResourceUtils.getFile(String.format("classpath:%s", appProperties.getTemplate()));

            log.info("Using template file {}", templateSource.getAbsolutePath());
            return Imgcodecs.imread(templateSource.getAbsolutePath());
        } catch (final FileNotFoundException fileNotFoundException) {
            log.error("Cannot find template file {}, exiting...", appProperties.getTemplate());
            throw new RuntimeException(fileNotFoundException.getMessage());
        }
    }

    /**
     * Preform the match between the source and the template file.
     */
    private Mat matchTemplate(final Mat source) {

        final Mat result = new Mat();
        Imgproc.matchTemplate(source, template, result, Imgproc.TM_CCOEFF_NORMED);
        Imgproc.threshold(result, result, 0.1, 1, Imgproc.THRESH_TOZERO);

        return result;
    }

    /**
     * Draw on the source image the location of the match.
     */
    private void drawMatch(final Mat source, final Point maxLoc, final Point point, final boolean debug) {

        if (debug) {
            Imgproc.rectangle(source, maxLoc, point, new Scalar(0, 255, 0), 5);
            Imgproc.putText(source, point.x + "," + point.y, point, Imgproc.FONT_HERSHEY_PLAIN, 2.0,
                    new Scalar(0, 255, 0), 1);
        }
    }

    /**
     * Flush the match file to disk. This is slow.
     */
    private void writeMatchToDisk(final Mat destination) {

        if (Constants.WRITE_TO_DISK) {
            Imgcodecs.imwrite(Instant.now().toEpochMilli() + "-match.jpg", destination);
        }
    }

    /**
     * Decide if a template match is duplicate based on it being within proximity to another match. Only used for {@link
     * FindTargetService#findMultipleTarget(Mat)}.
     */
    private boolean isDuplicate(final List<Target> targets, final Point point) {

        final double threshold = appProperties.getDuplicateThreshold();

        return targets.stream()
                .anyMatch(target -> Range.between(target.getPoint().x - threshold, target.getPoint().x + threshold)
                                            .contains(point.x) &&
                                    Range.between(target.getPoint().y - threshold, target.getPoint().y + threshold)
                                            .contains(point.y));
    }

}
