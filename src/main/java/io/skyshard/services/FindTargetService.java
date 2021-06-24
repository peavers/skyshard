package io.skyshard.services;

import io.skyshard.domain.Target;
import org.opencv.core.Mat;

import java.util.List;

public interface FindTargetService {

    List<Target> findMultipleTarget(Mat source);

    Target findSingleTarget(Mat source);

}
