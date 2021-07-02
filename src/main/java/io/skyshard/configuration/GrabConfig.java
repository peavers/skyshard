package io.skyshard.configuration;

import io.skyshard.exceptions.UnsupportedSystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Slf4j
@Configuration
public class GrabConfig {

    /**
     * Create a FFmpegFrameGrabber to capture that screen region. We use this instead of a more
     * 'common' robot.createScreenCapture() approach as its faster, and works correctly on Mac OS.
     */
    @Bean
    public FFmpegFrameGrabber grabber() {

        final var dimension = getDimension();
        final var grabber = getSystemSpecificGrabber();

        grabber.setFrameRate(30);
        grabber.setImageWidth(dimension.width);
        grabber.setImageHeight(dimension.height);
        grabber.setOption("preset", "ultrafast");

        return grabber;
    }

    /**
     * Return a grabber for either windows or mac; If you're trying to run this on anything else it will most likely
     * fail.
     */
    private FFmpegFrameGrabber getSystemSpecificGrabber() {

        if (SystemUtils.IS_OS_WINDOWS) {
            return buildGrabber("desktop", "gdigrab");
        }

        if (SystemUtils.IS_OS_MAC) {
            // On a Mac, the filename represents the screen you want to capture. In this case 2:0 is my second screen
            // with no audio input, and 1:0 would be your primary screen with no audio input. There is an FFMPEG command
            // you can execute to identify what screen is which (google it).
            return buildGrabber("2:0", "avfoundation");
        }

        throw new UnsupportedSystemException();
    }

    /**
     * Function to create a new grabber with predefined settings.
     */
    private FFmpegFrameGrabber buildGrabber(final String filename, final String format) {

        final var grabber = new FFmpegFrameGrabber(filename);
        grabber.setFormat(format);

        return grabber;
    }

    /**
     * Identify the resolution of the current monitor.
     */
    private Dimension getDimension() {

        final var dimension = Toolkit.getDefaultToolkit().getScreenSize();

        log.info("Screen dimensions {}x{}", dimension.width, dimension.height);

        return dimension;
    }

}
