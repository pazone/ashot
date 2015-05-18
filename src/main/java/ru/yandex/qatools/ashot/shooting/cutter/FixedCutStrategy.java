package ru.yandex.qatools.ashot.shooting.cutter;

import org.openqa.selenium.WebDriver;

/**
 * Strategy for cutting header and footer of a constant height.
 *
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class FixedCutStrategy implements CutStrategy {
    private final int headerToCut;
    private final int footerToCut;

    public FixedCutStrategy(int headerToCut, int footerToCut) {
        this.headerToCut = headerToCut;
        this.footerToCut = footerToCut;
    }

    @Override
    public int getHeaderHeight(WebDriver driver) {
        return headerToCut;
    }

    @Override
    public int getFooterHeight(WebDriver driver) {
        return footerToCut;
    }
}
