package io.skyshard.configuration;

import java.awt.*;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GrabConfig {

  @Bean
  public FFmpegFrameGrabber grabber() {

    final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

    log.info(
        "Using screen dimensions {}x{} as reference points", dimension.width, dimension.height);

    final FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("desktop");
    grabber.setFormat("gdigrab");
    grabber.setFrameRate(1);
    grabber.setImageWidth(dimension.width);
    grabber.setImageHeight(dimension.height);
    grabber.setOption("preset", "ultrafast");

    return grabber;
  }
}
