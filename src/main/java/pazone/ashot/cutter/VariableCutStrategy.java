package pazone.ashot.cutter;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import pazone.ashot.InvalidViewportHeightException;

/**
 * Strategy for cutting header and footer with variable height.<br>
 * For example, Safari browser in iOS 8 introduced a feature (so called 'minimal-ui')<br>
 * when browser's header might be 65px or 41px (with address bar hidden).<br>
 * This strategy will get current height of browser's header and footer.
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class VariableCutStrategy implements CutStrategy {

    public static final String SCRIPT = "var h = window.innerHeight || document.documentElement.clientHeight; return h;";
    private final int headerMin;
    private final int headerMax;
    private final int windowInnerHeightMin;
    private final int footerMax;
    private final int footerMin;

    /**
     * @param headerMin - minimal header height (for Safari iOS 8 it is 41px)
     * @param headerMax - maximum header height (for Safari iOS 8 it is 65px)
     * @param footerMin - minimal footer height (for Safari iOS 8 it is 0px)
     * @param footerMax - maximum footer height (for Safari iOS 8 it is 89px)
     * @param windowInnerHeightMin - minimal height of viewportPasting (when header with address bar shown).<br />
     *                             For real device iPad 2 it is 960px (portrait) and 674px (landscape).<br />
     *                             For simulated iPad 2 it is 1225px (portrait) and 687px (landscape).
     */
    public VariableCutStrategy(int headerMin, int headerMax, int footerMin, int footerMax, int windowInnerHeightMin) {
        this.headerMin = headerMin;
        this.headerMax = headerMax;
        this.footerMin = footerMin;
        this.footerMax = footerMax;
        this.windowInnerHeightMin = windowInnerHeightMin;
    }

    public VariableCutStrategy(int headerMin, int headerMax, int footerMax, int windowInnerHeightMin) {
        this(headerMin, headerMax, 0, footerMax, windowInnerHeightMin);
    }

    public VariableCutStrategy(int headerMin, int headerMax, int windowInnerHeightMin) {
        this(headerMin, headerMax, 0, windowInnerHeightMin);
    }

    @Override
    public int getHeaderHeight(WebDriver driver) {
        return getCutHeight((JavascriptExecutor) driver, headerMin, headerMax);

    }

    @Override
    public int getFooterHeight(WebDriver driver) {
        if (0 == footerMax && 0 == footerMin) {
            return 0;
        }
        return getCutHeight((JavascriptExecutor) driver, footerMin, footerMax);
    }

    private int getCutHeight(JavascriptExecutor driver, int heightMin, int heightMax) {
        final int innerHeight = getWindowInnerHeight(driver);
        return innerHeight > windowInnerHeightMin ? heightMin : heightMax;
    }

    private int getWindowInnerHeight(JavascriptExecutor driver) {
        final Number innerHeight;

        try {
            innerHeight = (Number) driver.executeScript(SCRIPT);
        } catch (ClassCastException e) {
            throw new InvalidViewportHeightException("Could not acquire window.innerHeight property!", e);
        }

        if (innerHeight == null) {
            throw new InvalidViewportHeightException("Could not acquire window.innerHeight property! Returned value is null.");
        }

        return innerHeight.intValue();
    }
}
