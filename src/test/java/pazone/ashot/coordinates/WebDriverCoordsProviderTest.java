package pazone.ashot.coordinates;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

class WebDriverCoordsProviderTest {
    private WebDriverCoordsProvider webDriverCoordsProvider = new WebDriverCoordsProvider();

    @Test
    void testGetCoordinatesOfElement() {
        WebElement element = mock(WebElement.class);
        int x = 1;
        int y = 2;
        int height = 3;
        int width = 4;
        when(element.getLocation()).thenReturn(new Point(x, y));
        when(element.getSize()).thenReturn(new Dimension(width, height));
        Coords coords = webDriverCoordsProvider.ofElement(null, element);
        assertAll(
                () -> assertEquals(x, coords.x),
                () -> assertEquals(y, coords.y),
                () -> assertEquals(height, coords.height),
                () -> assertEquals(width, coords.width)
        );
    }
}
