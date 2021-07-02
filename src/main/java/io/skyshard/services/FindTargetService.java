package io.skyshard.services;

import io.skyshard.domain.Target;
import java.util.List;
import org.opencv.core.Mat;

public interface FindTargetService {

  List<Target> findMultipleTarget(Mat source);

  Target findSingleTarget(Mat source);
}
