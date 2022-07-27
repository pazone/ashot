package pazone.ashot.cutter;

import org.openqa.selenium.WebDriver;

/**
 * Strategy for cutting header and footer of a constant height.
 *
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class FixedCutStrategy implements CutStrategy {
    private final int headerToCut;
    private final int footerToCut;
    private final int leftBarToCut;
    private final int rightBarToCut;

    public FixedCutStrategy(int headerToCut, int footerToCut) {
        this(headerToCut, footerToCut, 0, 0);
    }

    public FixedCutStrategy(int headerToCut, int footerToCut, int leftBarToCut, int rightBarToCut) {
        this.headerToCut = headerToCut;
        this.footerToCut = footerToCut;
        this.leftBarToCut = leftBarToCut;
        this.rightBarToCut = rightBarToCut;
    }

    @Override
    public int getHeaderHeight(WebDriver driver) {
        return headerToCut;
    }

    @Override
    public int getFooterHeight(WebDriver driver) {
        return footerToCut;
    }

    @Override
    public int getLeftBarWidth(WebDriver driver) {
        return leftBarToCut;
    }

    @Override
    public int getRightBarWidth(WebDriver driver) {
        return rightBarToCut;
    }
}
