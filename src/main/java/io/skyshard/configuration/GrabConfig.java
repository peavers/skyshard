package io.skyshard.configuration;

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

        final Dimension dimension = getDimension();

        final FFmpegFrameGrabber grabber = getSystemSpecificGrabber();

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

            // 2:0 represents the screen you want to grab, if primary display set as 1:0, if external monitor 2:0.
            return buildGrabber("2:0", "avfoundation");
        }

        throw new RuntimeException("Unsupported System. Exiting...");
    }

    /**
     * Function to create a new grabber with predefined settings.
     */
    private FFmpegFrameGrabber buildGrabber(final String filename, final String format) {

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filename);
        grabber.setFormat(format);

        return grabber;
    }

    /**
     * Identify the resolution of the current monitor.
     */
    private Dimension getDimension() {

        final var dimension = Toolkit.getDefaultToolkit().getScreenSize();

        log.info(
                "Using screen dimensions {}x{} as reference points", dimension.width, dimension.height);

        return dimension;
    }

}
