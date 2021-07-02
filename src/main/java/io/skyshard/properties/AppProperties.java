package io.skyshard.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application")
public class AppProperties {

    /**
     * Includes writing images to disk, don't run in production Default: false
     */
    private boolean debug = false;

    /**
     * Template file location relative to resource root. Default: /templates/default.png
     */
    private String template = "templates/default-1.png";

    /**
     * If true, only a single target is processed before a new image is scanned. Default: false
     */
    private boolean singleTargetMode = true;

    /**
     * Threshold used when searching for a template match via OpenCV. Default: 0.90
     */
    private double matchThreshold = 0.90;

    /**
     * How many pixels on X and Y to check for the same template image. This stops the same target
     * being picked up many times. Default: 30
     */
    private double duplicateThreshold = 30;

}
