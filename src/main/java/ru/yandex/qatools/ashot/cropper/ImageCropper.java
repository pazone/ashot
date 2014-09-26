package ru.yandex.qatools.ashot.cropper;

import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class ImageCropper {

    public Screenshot crop(BufferedImage image, Coords cropArea) {
        return cropArea == null
                ? new Screenshot(image)
                : cropScreenshot(image, cropArea);
    }

    protected abstract Screenshot cropScreenshot(BufferedImage image, Coords cropArea);


}
