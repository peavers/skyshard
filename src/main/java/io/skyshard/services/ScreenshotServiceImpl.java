package io.skyshard.services;

import static org.bytedeco.javacv.OpenCVFrameConverter.ToOrgOpenCvCoreMat;

import io.skyshard.properties.AppProperties;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenshotServiceImpl implements ScreenshotService {

  private final FFmpegFrameGrabber grabber;

  private final AppProperties appProperties = new AppProperties();

  private final ToOrgOpenCvCoreMat toCore = new ToOrgOpenCvCoreMat();

  @Override
  @SneakyThrows
  public Mat take() {

    if (!grabber.isCloseInputStream()) {
      grabber.stop();
    }

    grabber.start();

    final Mat screenshot = toCore.convert(grabber.grabImage());

    if (appProperties.isDebug()) {
      Imgcodecs.imwrite(Instant.now().toEpochMilli() + "-screenshot.jpg", screenshot);
    }

    return screenshot;
  }
}
