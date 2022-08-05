package pazone.ashot.cutter;

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

    /**
     * Obtains width of left bar that will be cut off from initial screenshot.
     *
     * @param driver - webDriver
     * @return width of the left bar in pixels
     */
    default int getLeftBarWidth(WebDriver driver) {
        return 0;
    }

    /**
     * Obtains width of right bar that will be cut off from initial screenshot.
     *
     * @param driver - webDriver
     * @return width of the right bar in pixels
     */
    default int getRightBarWidth(WebDriver driver) {
        return 0;
    }
}
