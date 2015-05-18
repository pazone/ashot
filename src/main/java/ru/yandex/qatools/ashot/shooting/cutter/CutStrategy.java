package ru.yandex.qatools.ashot.shooting.cutter;

import org.openqa.selenium.WebDriver;

import java.io.Serializable;

/**
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public interface CutStrategy extends Serializable {

    /**
     * Obtains height of header that will be cut off from initial screenshot.
     * @param driver - webDriver
     * @return height of header in pixels
     */
    int getHeaderHeight(WebDriver driver);

    /**
     * Obtains height of footer that will be cut off from initial screenshot.
     * @param driver - webDriver
     * @return height of header in pixels
     */
    int getFooterHeight(WebDriver driver);
}
