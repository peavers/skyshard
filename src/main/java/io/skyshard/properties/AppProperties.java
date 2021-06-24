package io.skyshard.properties;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application")
public class AppProperties {

    /**
     * Includes writing images to disk, don't run in production
     * </p>
     * Default: false
     */
    private boolean debug;

    /**
     * Template file location relative to resource root.
     * </p>
     * Default: /templates/default.png
     */
    @Builder.Default
    private String template = "/templates/default.png";

    /**
     * If true, only a single target is processed before a new image is scanned.
     * </p>
     * Default: false
     */
    private boolean singleTargetMode;

    /**
     * Threshold used when searching for a template match via OpenCV.
     * </p>
     * Default: 0.90
     */
    @Builder.Default
    private double matchThreshold = 0.90;

    /**
     * How many pixels on X and Y to check for the same template image. This stops the same target being picked up many
     * times.
     * </p>
     * Default: 30
     */
    @Builder.Default
    private double duplicateThreshold = 30;

}
