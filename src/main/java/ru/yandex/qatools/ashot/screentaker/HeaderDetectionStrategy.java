package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.WebDriver;

import java.io.Serializable;

/**
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public abstract class HeaderDetectionStrategy implements Serializable {

    /**
     * Obtains height of header that will be cut off from initial screenshot.
     * @param driver - webDriver
     * @return height of header in pixels
     */
    public abstract int getHeaderHeight(WebDriver driver);
}
