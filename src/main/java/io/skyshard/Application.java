package io.skyshard;

import io.skyshard.properties.SkyshardProperties;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(SkyshardProperties.class)
public class Application {

    public static void main(final String[] args) {

        Loader.load(opencv_java.class);

        avutil.av_log_set_level(avutil.AV_LOG_QUIET);

        final var builder = new SpringApplicationBuilder(Application.class);
        builder.headless(false);
        builder.run(args);
    }

}
