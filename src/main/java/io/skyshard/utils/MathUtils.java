package io.skyshard.utils;

import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {

  private static final Random random = new Random();

  /** Return a random number between the two values. */
  public int random(final int min, final int max) {

    return random.nextInt(max - min) + min;
  }
}
