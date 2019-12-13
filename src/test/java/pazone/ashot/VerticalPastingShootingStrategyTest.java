package pazone.ashot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pazone.ashot.coordinates.Coords;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerticalPastingShootingStrategyTest {

    private static final int VIEWPORT_HEIGHT = 80;
    private static final int DEFAULT_PAGE_HEIGHT = VIEWPORT_HEIGHT * 10 + VIEWPORT_HEIGHT / 2;
    private static final int PAGE_WIDTH = 100;
    private static final int DEFAULT_COORDS_INDENT = VIEWPORT_HEIGHT / 2;

    private final BufferedImage viewPortShot = new BufferedImage(PAGE_WIDTH, VIEWPORT_HEIGHT, TYPE_4BYTE_ABGR_PRE);
    private Coords shootingCoords;
    private BufferedImage screenshot;
    private Set<Coords> preparedCoords;

    @Mock(extraInterfaces = {JavascriptExecutor.class, TakesScreenshot.class})
    private WebDriver webDriver;

    @Spy
    private ViewportPastingDecorator shootingStrategy = new MockVerticalPastingShootingDecorator(
            new SimpleShootingStrategy()).withScrollTimeout(0);

    static Stream<Arguments> timesData() {
        return Stream.of(
                Arguments.of(VIEWPORT_HEIGHT,     2),
                Arguments.of(VIEWPORT_HEIGHT / 2, 1),
                Arguments.of(VIEWPORT_HEIGHT * 3, 4),
                Arguments.of(0,                   1)
        );
    }

    @ParameterizedTest
    @MethodSource("timesData")
    void testTimes(int height, int times) throws IOException {
        givenCoordsWithHeight(height);
        whenTakingScreenshot(shootingCoords);
        thenScrollTimes(times);
        thenShootTimes(times);
        thenScreenshotIsHeight(height + DEFAULT_COORDS_INDENT);
    }

    @Test
    void testCoordsShiftWithDefaultIndent() throws IOException {
        givenCoordsWithHeight(VIEWPORT_HEIGHT / 3);
        whenTakingScreenshot(shootingCoords);
        whenPreparingCoords(singleton(shootingCoords));
        thenCoordsShifted();
    }

    @Test
    void testScreenshotFullPage() throws IOException {
        whenTakingScreenshot();
        thenShootTimes(11);
        thenScrollTimes(11);
        thenScreenshotIsHeight(DEFAULT_PAGE_HEIGHT);
    }

    private byte[] getImageAsBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(viewPortShot, "PNG", baos);
        return baos.toByteArray();
    }

    private void mockScreenshotting() throws IOException {
        when(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES)).thenReturn(getImageAsBytes());
    }

    private void givenCoordsWithHeight(int height) {
        shootingCoords = new Coords(0, VIEWPORT_HEIGHT * 4, PAGE_WIDTH, height);
    }

    private void whenTakingScreenshot() throws IOException {
        mockScreenshotting();
        screenshot = shootingStrategy.getScreenshot(webDriver);
    }

    private void whenTakingScreenshot(Coords coords) throws IOException {
        mockScreenshotting();
        screenshot = shootingStrategy.getScreenshot(webDriver, singleton(coords));
    }

    private void whenPreparingCoords(Set<Coords> coords) {
        preparedCoords = shootingStrategy.prepareCoords(coords);
    }

    private void thenCoordsShifted() {
        assertThat("Coords should be shifted correctly", preparedCoords,
                everyItem(hasProperty("y", is((double) DEFAULT_COORDS_INDENT / 2))));
    }

    private void thenScreenshotIsHeight(int shotHeight) {
        assertThat("Screenshot height should be correct", screenshot.getHeight(), is(shotHeight));
    }

    private void thenScrollTimes(int times) {
        verify(shootingStrategy, times(times)).scrollVertically(eq((JavascriptExecutor) webDriver), anyInt());
    }

    private void thenShootTimes(int times) {
        verify(((TakesScreenshot) webDriver), times(times)).getScreenshotAs(OutputType.BYTES);
    }

    static class MockVerticalPastingShootingDecorator extends ViewportPastingDecorator {

        MockVerticalPastingShootingDecorator(ShootingStrategy strategy) {
            super(strategy);
        }

        @Override
        protected PageDimensions getPageDimensions(WebDriver driver) {
            return new PageDimensions(DEFAULT_PAGE_HEIGHT, PAGE_WIDTH, VIEWPORT_HEIGHT);
        }

        @Override
        public int getCurrentScrollY(JavascriptExecutor js) {
            return 0;
        }

    }
}
