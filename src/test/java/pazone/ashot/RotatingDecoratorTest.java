package pazone.ashot;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pazone.ashot.comparison.ImageDiff;
import pazone.ashot.coordinates.Coords;
import pazone.ashot.cutter.FixedCutStrategy;
import pazone.ashot.comparison.ImageDiffer;

import java.awt.image.BufferedImage;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

/**
 * @author <a href="rovner@yandex-team.ru">Rovniakov Viacheslav</a>
 */

public class RotatingDecoratorTest {

    private WebDriver wd = mock(WebDriver.class);

    @Test
    public void testRotating() throws Exception {
        RotatingDecorator strategy = new RotatingDecorator(new FixedCutStrategy(0, 0), new MockShootingStrategy());
        BufferedImage screenshot = strategy.getScreenshot(wd);
        ImageDiff diff = new ImageDiffer().makeDiff(DifferTest.loadImage("img/expected/rotated.png"), screenshot);
        assertFalse(diff.hasDiff());

    }

    private static class MockShootingStrategy implements ShootingStrategy {
        @Override
        public BufferedImage getScreenshot(WebDriver wd) {
            return DifferTest.loadImage("img/A_s.png");
        }

        @Override
        public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords) {
            return getScreenshot(wd);
        }

        @Override
        public Set<Coords> prepareCoords(Set<Coords> coordsSet) {
            return coordsSet;
        }
    }
}
