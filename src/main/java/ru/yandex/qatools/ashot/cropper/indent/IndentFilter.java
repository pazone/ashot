package ru.yandex.qatools.ashot.cropper.indent;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class IndentFilter implements Serializable {
    public abstract BufferedImage apply(BufferedImage image);
}
