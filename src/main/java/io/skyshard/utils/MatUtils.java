package io.skyshard.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Files;
import java.nio.file.Path;
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

    var result = new Mat();
    Core.normalize(mat, result, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);

    return result;
  }

  @SneakyThrows
  public void write(final Mat mat, final String name) {

    final Path path = Files.createDirectories(Paths.get("./.debug"));

    Imgcodecs.imwrite(
        MessageFormat.format(path + "/{0}-{1}.jpg", Instant.now().toEpochMilli(), name), mat);
  }
}
