package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ViewportPastingStrategy extends VerticalPastingShootingStrategy {

    public ViewportPastingStrategy(int scrollTimeout, int headerToCut) {
        super(scrollTimeout, headerToCut);
    }

    public ViewportPastingStrategy(int scrollTimeout) {
        super(scrollTimeout, 0);
    }

    @Override
    public int getFullHeight(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return ((Long) js.executeScript("return $(document).height()")).intValue();
    }

    @Override
    public int getFullWidth(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return  ((Long) js.executeScript("return $(window).width()")).intValue();
    }

    @Override
    public int getWindowHeight(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return  ((Long) js.executeScript("return $(window).height()")).intValue();
    }
}
