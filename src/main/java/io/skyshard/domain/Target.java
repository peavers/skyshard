package io.skyshard.domain;

import lombok.Builder;
import lombok.Data;
import org.opencv.core.Point;

@Data
@Builder
public class Target {

  private Point point;

  public int x() {

    return (int) Math.round(this.point.x);
  }

  public int y() {

    return (int) Math.round(this.point.y);
  }
}
