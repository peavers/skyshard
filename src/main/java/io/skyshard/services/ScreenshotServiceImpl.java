package io.skyshard.services;

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

    private final ToOrgOpenCvCoreMat toCore = new ToOrgOpenCvCoreMat();

    @Override
    @SneakyThrows
    public Mat take() {

        grabber.start();
        final Mat output = toCore.convert(grabber.grab());
        grabber.stop();

        return output;
    }

}
