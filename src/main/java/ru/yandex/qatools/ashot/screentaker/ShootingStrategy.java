package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

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
        };
    }

    public abstract BufferedImage getScreenshot(WebDriver wd);

}
