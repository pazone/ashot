package pazone.ashot.cropper.indent;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class BlurFilter implements IndentFilter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        Kernel kernel = new Kernel(3, 3,
            new float[] {
                1f / 9f, 1f / 9f, 1f / 9f,
                1f / 9f, 1f / 9f, 1f / 9f,
                1f / 9f, 1f / 9f, 1f / 9f
            }
        );

        BufferedImageOp blurOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return blurOp.filter(image, null);
    }
}
