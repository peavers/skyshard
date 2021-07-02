package io.skyshard.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Instant;

@UtilityClass
public class MatUtils {

    /**
     * Given a Mat image, make sure its as basic as it can be and is the same as all other images.
     * This is important with matchTemplating as if the number of channels are different things can
     * get awkward and hard to debug.
     */
    public Mat normalize(final Mat mat) {

        final var result = new Mat();
        Core.normalize(mat, result, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);

        return result;
    }

    @SneakyThrows
    public void write(final Mat mat, final String name) {

        final var path = Files.createDirectories(Paths.get("./.debug"));

        Imgcodecs.imwrite(
                MessageFormat.format("{0}/{1}-{2}.jpg", path, Instant.now().toEpochMilli(), name), mat);
    }

    public void drawRectangle(final Mat source, final Point maxLoc, final Point point) {

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
