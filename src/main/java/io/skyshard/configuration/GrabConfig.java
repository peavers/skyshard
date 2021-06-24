package io.skyshard.configuration;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Slf4j
@Configuration
public class GrabConfig {

    private static final String MAC_OSX_FORMAT = "avfoundation";

    /**
     * Use output from running `ffmpeg -hide_banner -f avfoundation -list_devices true -i ""` to find the location of
     * the primary input/screen
     */
    private static final String DISPLAY_LOCATION = "1:0";

    @Bean
    public FFmpegFrameGrabber grabber() {

        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        log.info("Using screen dimensions {}x{} (width|height) as reference points", dimension.width, dimension.height);

        final FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(DISPLAY_LOCATION);
        grabber.setFormat(MAC_OSX_FORMAT);
        grabber.setImageWidth(dimension.width);
        grabber.setImageHeight(dimension.height);

        return grabber;
    }

}
