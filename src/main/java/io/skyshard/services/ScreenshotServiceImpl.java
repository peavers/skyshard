package io.skyshard.services;

import io.skyshard.properties.SkyshardProperties;
import io.skyshard.utils.MatUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import static org.bytedeco.javacv.OpenCVFrameConverter.ToOrgOpenCvCoreMat;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenshotServiceImpl implements ScreenshotService {

    private final FFmpegFrameGrabber grabber;

    private final SkyshardProperties skyshardProperties;

    private final ToOrgOpenCvCoreMat toCore = new ToOrgOpenCvCoreMat();

    @Override
    @SneakyThrows
    public Mat take() {

        if (!grabber.isCloseInputStream()) {
            grabber.stop();
        }

        grabber.start();

        final Mat screenshot = toCore.convert(grabber.grabImage());

        if (skyshardProperties.isDebug()) {
            MatUtils.write(screenshot, "screenshot");
        }

        return MatUtils.normalize(screenshot);
    }

}
