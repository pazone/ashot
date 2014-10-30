package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ScreenTaker implements Serializable {

    public static final int STANDARD_DRP = 1;

    protected float dprX = STANDARD_DRP;
    protected float dprY = STANDARD_DRP;

    protected ShootingStrategy shootingStrategy = ShootingStrategy.simple();

    public BufferedImage take(WebDriver driver) {
        BufferedImage screen = shootingStrategy.getScreenshot(driver);
        return scale(screen);
    }

    public ScreenTaker(ScreenTaker other) {
        this.dprX = other.dprX;
        this.dprY = other.dprY;
        this.shootingStrategy = other.shootingStrategy;
    }

    public ScreenTaker() {
    }

    public ScreenTaker withShootingStrategy(ShootingStrategy shootingStrategy) {
        this.shootingStrategy = shootingStrategy;
        return this;
    }

    public ScreenTaker withDpr(float dpr) {
        this.dprX = dpr;
        this.dprY = dpr;
        return this;
    }

    public ScreenTaker withDprX(float dprX) {
        this.dprX = dprX;
        return this;
    }

    public ScreenTaker withDprY(float dprY) {
        this.dprY = dprY;
        return this;
    }

    private BufferedImage scale(BufferedImage screen) {
        if (dprY == 1 && dprX == 1) {
            return screen;
        }
        int scaledWidth = (int) (screen.getWidth() / dprX);
        int scaledHeight = (int) (screen.getHeight() / dprY);
        final BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(screen, 0, 0, scaledWidth, scaledHeight, null);
        graphics2D.dispose();
        return bufferedImage;
    }

}
