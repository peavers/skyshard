package io.skyshard.domain;

import lombok.Builder;
import lombok.Data;
import org.opencv.core.Point;

/**
 * Represents where a target is found on the screen.
 */
@Data
@Builder
public class Target {

    private Point point;

    /**
     * Get the X location of the point. Since we're talking about pixels here we don't care about the
     * decimal point.
     */
    public int x() {

        return (int) Math.round(this.point.x);
    }

    /**
     * Get the Y location of the point. Since we're talking about pixels here we don't care about the
     * decimal point.
     */
    public int y() {

        return (int) Math.round(this.point.y);
    }

}
