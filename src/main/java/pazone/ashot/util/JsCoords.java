package pazone.ashot.util;

import java.util.List;

import com.google.gson.Gson;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pazone.ashot.coordinates.Coords;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public final class JsCoords {

    public static final String COORDS_JS_PATH = "js/coords-single.js";

    private JsCoords() {
        throw new UnsupportedOperationException();
    }

    public static Coords findCoordsWithJquery(WebDriver driver, WebElement element) {
        List<?> result = InnerScript.execute(COORDS_JS_PATH, driver, element);
        if (result.isEmpty()) {
            throw new RuntimeException("Unable to find coordinates with jQuery.");
        }
        return new Gson().fromJson((String) result.get(0), Coords.class);
    }

}
