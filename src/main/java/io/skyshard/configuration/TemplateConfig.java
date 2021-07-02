package io.skyshard.configuration;

import io.skyshard.properties.AppProperties;
import java.io.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TemplateConfig {

  private final AppProperties appProperties;

  /**
   * Read in the matching template file from the resource directory. If this can't be found or its
   * not set in the application properties file, exit the application.
   */
  @Bean
  public Mat loadTemplate() {

    try {
      final var templateSource =
          ResourceUtils.getFile(String.format("classpath:%s", appProperties.getTemplate()));

      log.info("Using template file {}", templateSource.getAbsolutePath());

      return Imgcodecs.imread(templateSource.getAbsolutePath());

    } catch (final FileNotFoundException fileNotFoundException) {
      log.error("Cannot find template file {}, exiting...", appProperties.getTemplate());
      throw new RuntimeException(fileNotFoundException.getMessage());
    }
  }
}
