package ru.yandex.qatools.ashot.screentaker;

import org.openqa.selenium.WebDriver;

/**
 * Strategy for cutting header of a constant height {@code headerToCut}.
 *
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class FixedHeaderDetectionStrategy extends HeaderDetectionStrategy {
    private final int headerToCut;

    public FixedHeaderDetectionStrategy(int headerToCut) {
        this.headerToCut = headerToCut;
    }

    @Override
    public int getHeaderHeight(WebDriver driver) {
        return headerToCut;
    }
}
