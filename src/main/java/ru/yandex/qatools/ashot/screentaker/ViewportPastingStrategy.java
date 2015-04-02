package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.WebDriver;

import static ru.yandex.qatools.ashot.util.InnerScript.*;

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
        return ((Number) execute(PAGE_HEIGHT_JS, driver)).intValue();
    }

    @Override
    public int getFullWidth(WebDriver driver) {
        return ((Number) execute(VIEWPORT_WIDTH_JS, driver)).intValue();
    }

    @Override
    public int getWindowHeight(WebDriver driver) {
        return ((Number) execute(VIEWPORT_HEIGHT_JS, driver)).intValue();
    }
}
