package pazone.ashot;

import org.openqa.selenium.WebDriver;
import pazone.ashot.coordinates.Coords;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * Will scale down image that was aquired by WebDriver.
 * Scaling is performed according to device pixel ratio (DPR)
 * Useful for browsers on portable devices with Retina displays.
 */
public class ScalingDecorator extends ShootingDecorator {

    private static final Float STANDARD_DRP = 1F;
    private Float dprX = STANDARD_DRP;
    private Float dprY = STANDARD_DRP;

    public ScalingDecorator(ShootingStrategy strategy) {
        super(strategy);
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd) {
        return scale(getShootingStrategy().getScreenshot(wd));
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords) {
        return scale(getShootingStrategy().getScreenshot(wd, coords));
    }

    public ScalingDecorator withDprX(float dprX) {
        this.dprX = dprX;
        return this;
    }

    public ScalingDecorator withDprY(float dprY) {
        this.dprY = dprY;
        return this;
    }

    public ScalingDecorator withDpr(float dpr) {
        return this
                .withDprX(dpr)
                .withDprY(dpr);
    }

    private BufferedImage scale(BufferedImage image) {
        if (STANDARD_DRP.equals(dprY) && STANDARD_DRP.equals(dprX)) {
            return image;
        }
        int scaledWidth = (int) (image.getWidth() / dprX);
        int scaledHeight = (int) (image.getHeight() / dprY);

        final BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_3BYTE_BGR);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        graphics2D.dispose();
        return bufferedImage;
    }
}
