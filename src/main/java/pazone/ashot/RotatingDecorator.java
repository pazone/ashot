package pazone.ashot;

import org.openqa.selenium.WebDriver;
import pazone.ashot.coordinates.Coords;
import pazone.ashot.cutter.CutStrategy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;

/**
 * @author <a href="rovner@yandex-team.ru">Rovniakov Viacheslav</a>
 */

public class RotatingDecorator implements ShootingStrategy {

    private final CutStrategy cutStrategy;
    private final ShootingStrategy shootingStrategy;

    public RotatingDecorator(CutStrategy cutStrategy, ShootingStrategy shootingStrategy) {
        this.cutStrategy = cutStrategy;
        this.shootingStrategy = shootingStrategy;
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd) {
        return rotate(shootingStrategy.getScreenshot(wd), wd);
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords) {
        return getScreenshot(wd);
    }

    @Override
    public Set<Coords> prepareCoords(Set<Coords> coordsSet) {
        return coordsSet;
    }

    private BufferedImage rotate(BufferedImage baseImage, WebDriver wd) {
        BufferedImage rotated = new BufferedImage(baseImage.getHeight(), baseImage.getWidth(), TYPE_4BYTE_ABGR);
        Graphics2D graphics = rotated.createGraphics();
        double theta = 3 * Math.PI / 2;
        int origin = baseImage.getWidth() / 2;
        graphics.rotate(theta, origin, origin);
        graphics.drawImage(baseImage, null, 0, 0);
        int rotatedHeight = rotated.getHeight();
        int rotatedWidth = rotated.getWidth();
        int headerToCut = cutStrategy.getHeaderHeight(wd);
        return rotated.getSubimage(0, headerToCut, rotatedWidth, rotatedHeight - headerToCut);
    }
}
