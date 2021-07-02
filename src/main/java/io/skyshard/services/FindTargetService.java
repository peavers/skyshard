package io.skyshard.services;

import io.skyshard.domain.Target;
import org.opencv.core.Mat;

import java.util.List;
import java.util.Optional;

public interface FindTargetService {

    List<Target> findMultipleTarget(Mat source);

    Optional<Target> findSingleTarget(Mat source);

}
