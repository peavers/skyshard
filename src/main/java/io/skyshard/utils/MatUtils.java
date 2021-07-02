package io.skyshard.utils;

import lombok.experimental.UtilityClass;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

@UtilityClass
public class MatUtils {

  public Mat normalize(Mat mat) {
    Mat result = new Mat();
    Core.normalize(mat, result, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);

    return result;
  }
}
