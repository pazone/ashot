package pazone.ashot.coordinates;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pazone.ashot.util.JsCoords;

/**
 * @author pazone
 */
public class JqueryCoordsProvider extends CoordsProvider {

    @Override
    public Coords ofElement(WebDriver driver, WebElement element) {
        return JsCoords.findCoordsWithJquery(driver, element);
    }
}
