package io.skyshard.services;

import io.skyshard.domain.Target;
import java.util.List;
import java.util.Optional;
import org.opencv.core.Mat;

public interface FindTargetService {

  List<Target> findMultipleTarget(Mat source);

  Optional<Target> findSingleTarget(Mat source);
}
