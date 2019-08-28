package pazone.ashot.cropper.indent;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public interface IndentFilter extends Serializable {

    BufferedImage apply(BufferedImage image);

}
