package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 *         <p/>
 *         Pastes together parts of screenshots
 *         Used when driver shoots viewport only
 */

public abstract class VerticalPastingShootingStrategy extends HeadCuttingShootingStrategy {

    protected int scrollTimeout = 0;

    public void setScrollTimeout(int scrollTimeout) {
        this.scrollTimeout = scrollTimeout;
    }

    protected VerticalPastingShootingStrategy(int scrollTimeout, int headerToCut) {
        super(headerToCut);
        this.scrollTimeout = scrollTimeout;
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd) {
        JavascriptExecutor js = (JavascriptExecutor) wd;

        int allH = getFullHeight(wd);
        int allW = getFullWidth(wd);
        int winH = getWindowHeight(wd);

        int scrollTimes = allH / winH;
        int tail = allH - winH * scrollTimes;

        BufferedImage finalImage = new BufferedImage(allW, allH, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = finalImage.createGraphics();

        for (int n = 0; n < scrollTimes; n++) {
            js.executeScript("scrollTo(0, arguments[0])", winH * n);
            waitForScrolling();
            BufferedImage part = super.getScreenshot(wd);
            graphics.drawImage(part, 0, n * winH, null);
        }

        if (tail > 0) {
            js.executeScript("scrollTo(0, document.body.scrollHeight)");
            waitForScrolling();
            BufferedImage last = super.getScreenshot(wd);
            BufferedImage tailImage = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
            graphics.drawImage(tailImage, 0, scrollTimes * winH, null);
        }
        graphics.dispose();

        return finalImage;
    }

    private void waitForScrolling() {
        try {
            Thread.sleep(scrollTimeout);
        } catch (InterruptedException ignored) {
        }
    }

    public abstract int getFullHeight(WebDriver driver);

    public abstract int getFullWidth(WebDriver driver);

    public abstract int getWindowHeight(WebDriver driver);


}
