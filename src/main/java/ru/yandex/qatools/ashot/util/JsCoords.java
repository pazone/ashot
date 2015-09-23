package ru.yandex.qatools.ashot.util;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.currentThread;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public final class JsCoords {

    public static final String COORDS_JS_PATH = "js/coords-single.js";

    private JsCoords() {
        throw new UnsupportedOperationException();
    }

    public static Coords findCoordsWithJquery(WebDriver driver, WebElement element) {
        try {
            String script = IOUtils.toString(currentThread().getContextClassLoader().getResourceAsStream(COORDS_JS_PATH));
            List result = (List) ((JavascriptExecutor) driver).executeScript(script, element);
            if (result.isEmpty()) {
                throw new RuntimeException("Unable to find coordinates with jQuery.");
            }
            return new Gson().fromJson((String) result.get(0), Coords.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
