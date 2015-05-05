package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import ru.yandex.qatools.ashot.coordinates.Coords;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class ShootingStrategy implements Serializable {

    public static ShootingStrategy simple() {
        return new ShootingStrategy() {
            @Override
            public BufferedImage getScreenshot(WebDriver wd){
                ByteArrayInputStream imageArrayStream = null;
                TakesScreenshot takesScreenshot = (TakesScreenshot) new Augmenter().augment(wd);
                try {
                    imageArrayStream = new ByteArrayInputStream(takesScreenshot.getScreenshotAs(OutputType.BYTES));
                    return ImageIO.read(imageArrayStream);
                } catch (IOException e) {
                    throw new RuntimeException("Can not parse screenshot data", e);
                } finally {
                    try {
                        if (imageArrayStream != null) {
                            imageArrayStream.close();
                        }
                    } catch (IOException ignored) {
                    }
                }
            }

            @Override
            public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords) {
                return getScreenshot(wd);
            }
        };
    }

    public abstract BufferedImage getScreenshot(WebDriver wd);

    public abstract BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords);
}
