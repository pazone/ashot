package pazone.ashot.cropper.indent;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public final class IndentFilerFactory {

    private IndentFilerFactory() {
        throw new UnsupportedOperationException();
    }


    public static BlurFilter blur() {
        return new BlurFilter();
    }

    public static MonochromeFilter monochrome() {
        return new MonochromeFilter();
    }

}
