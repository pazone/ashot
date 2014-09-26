package ru.yandex.qatools.ashot.cropper;

import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class DefaultCropper extends ImageCropper {

    @Override
    public Screenshot cropScreenshot(BufferedImage image, Coords cropArea) {
        double width = Math.min(cropArea.width, image.getWidth() - cropArea.x);
        double height = Math.min(cropArea.height, image.getHeight() - cropArea.y);

        //Если кординаты не пересекаются с картинкой, возвращаем исходное изображение
        if (width <= 0 || height <= 0) {
            return new Screenshot(image);
        }

        BufferedImage cropped = new BufferedImage((int) width, (int) height, image.getType());
        Graphics g = cropped.getGraphics();
        g.drawImage(
                image,
                0, 0,
                (int) width, (int) height,
                (int) cropArea.getX(), (int) cropArea.getY(),
                (int) (cropArea.getX() + width), (int) (cropArea.getY() + height),
                null
        );
        g.dispose();
        return new Screenshot(cropped);
    }


}
