package ru.yandex.qatools.ashot.cropper.indent;

import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class IndentFilter {
    public abstract BufferedImage apply(BufferedImage image);
}
