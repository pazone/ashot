package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Strategy for getting size of header with variable height.<br>
 * For example, Safari browser in iOS 8 introduced a feature (so called 'minimal-ui')<br>
 * when browser's header might be 65px or 41px (with address bar hidden).<br>
 * This strategy will get current height of browser's header.
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class VariableHeaderDetectionStrategy extends HeaderDetectionStrategy {

    static final String SCRIPT = "var h = window.innerHeight || document.documentElement.clientHeight; return h;";
    private final int headerMin;
    private final int headerMax;
    private final int windowInnerHeightMin;

    /**
     * @param headerMin - minimal header height (for Safari iOS 8 it is 41px)
     * @param headerMax - maximum header height (for Safari iOS 8 it is 65px)
     * @param windowInnerHeightMin - minimal height of viewport (when header with address bar shown).<br />
     *                             For real device iPad 2 it is 960px (portrait) and 674px (landscape).<br />
     *                             For simulated iPad 2 it is 1225px (portrait) and 687px (landscape).
     */
    public VariableHeaderDetectionStrategy(int headerMin, int headerMax, int windowInnerHeightMin) {
        this.headerMin = headerMin;
        this.headerMax = headerMax;
        this.windowInnerHeightMin = windowInnerHeightMin;
    }

    @Override
    public int getHeaderHeight(WebDriver driver) {
        final Number innerHeight;

        try {
            innerHeight = (Number) ((JavascriptExecutor) driver).executeScript(SCRIPT);
        } catch (ClassCastException e) {
            throw new InvalidViewportHeightException("Could not acquire window.innerHeight property!", e);
        }

        if (innerHeight == null) {
            throw new InvalidViewportHeightException("Could not acquire window.innerHeight property! Returned value is null.");
        }

        if (windowInnerHeightMin >= innerHeight.intValue()) {
            return headerMax;
        } else {
            return headerMin;
        }

    }
}
