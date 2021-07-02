package io.skyshard.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("skyshard")
public class SkyshardProperties {

    private boolean debug = false;

    private String template = "templates/example.png";

    private boolean singleTargetMode = true;

    private double matchThreshold = 0.90;

    private double duplicateThreshold = 30;

    private String primaryScreen = "2:0";
}
