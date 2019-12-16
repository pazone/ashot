package pazone.ashot.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import static java.lang.Thread.currentThread;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public final class InnerScript {

    private InnerScript() {
        throw new UnsupportedOperationException();
    }

    public static <T> T execute(String path, WebDriver driver, Object... args) {
        try {
            String script = IOUtils.toString(currentThread().getContextClassLoader().getResourceAsStream(path),
                    StandardCharsets.UTF_8);
            //noinspection unchecked
            return (T) ((JavascriptExecutor) driver).executeScript(script, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
