package ru.yandex.qatools.ashot.cropper.indent;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class IndentFilerFactory {

    public static IndentFilter blur() {
        return new BlurFilter();
    }

    public static IndentFilter monochrome() {
        return new MonochromeFilter();
    }
}
