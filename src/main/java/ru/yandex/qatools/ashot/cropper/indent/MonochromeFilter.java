package ru.yandex.qatools.ashot.cropper.indent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class MonochromeFilter extends IndentFilter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        return darken(image);
    }

    private BufferedImage darken(BufferedImage image) {
        return toBufferedImage(GrayFilter.createDisabledImage(image));
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bufferedImage = new BufferedImage(
                img.getWidth(null),
                img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }
}
