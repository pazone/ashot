package ru.yandex.qatools.ashot.shooting;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.coordinates.Coords;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class VerticalPastingShootingStrategyTest {

    private static final int VIEWPORT_HEIGHT = 80;
    private static final int DEFAULT_PAGE_HEIGHT = VIEWPORT_HEIGHT * 10 + VIEWPORT_HEIGHT / 2;
    private static final int PAGE_WIDTH = 100;
    private static final int DEFAULT_COORDS_INDENT = VIEWPORT_HEIGHT / 2;
    private static final double DEFAULT_COORDS_SHIFT = DEFAULT_COORDS_INDENT / 2;
    private static final int SHOOT_COORDS_OFFSET_Y = VIEWPORT_HEIGHT * 4;
    private MockSettings wdSettings = withSettings().extraInterfaces(JavascriptExecutor.class, TakesScreenshot.class);
    private WebDriver wd = mock(WebDriver.class, wdSettings);
    private BufferedImage viewPortShot = new BufferedImage(PAGE_WIDTH, VIEWPORT_HEIGHT, TYPE_4BYTE_ABGR_PRE);
    private Set<Coords> coordsSet;
    private BufferedImage screenshot;
    private Set<Coords> preparedCoords;
    private MockVerticalPastingShootingDecorator shootingStrategy;
    private Coords shootingCoords;

    @Before
    public void setUp() throws Exception {
        coordsSet = new HashSet<>();
        MockVerticalPastingShootingDecorator shootingStrategy =
                new MockVerticalPastingShootingDecorator(new SimpleShootingStrategy());
        shootingStrategy.withScrollTimeout(0);
        this.shootingStrategy = spy(shootingStrategy);
        when(((TakesScreenshot) wd).getScreenshotAs(any(OutputType.class))).thenReturn(getImageAsBytes());
    }

    @Test
    public void testCoordsShiftWithDefaultIndent() throws Exception {
        givenCoordsWithHeight(VIEWPORT_HEIGHT / 3);
        whenTakingScreenshot(shootingCoords);
        whenPreparingCoords(singleton(shootingCoords));
        thenCoordsShiftedWith(DEFAULT_COORDS_SHIFT);
    }

    @Test
    public void testTimesWhenCoordsEqualsViewport() throws Exception {
        givenCoordsWithHeight(VIEWPORT_HEIGHT);
        whenTakingScreenshot(shootingCoords);
        thenScrollTimes(2);
        thenShootTimes(2);
    }

    @Test
    public void testTimesWhenCoordsLargerThanViewport() throws Exception {
        givenCoordsWithHeight(VIEWPORT_HEIGHT * 3);
        whenTakingScreenshot(shootingCoords);
        thenScrollTimes(4);
        thenShootTimes(4);
    }

    @Test
    public void testTimesWhenCoordsLessThanViewport() throws Exception {
        givenCoordsWithHeight(VIEWPORT_HEIGHT / 2);
        whenTakingScreenshot(shootingCoords);
        thenScrollTimes(1);
        thenShootTimes(1);
    }

    @Test
    public void testScreenshotHeight() throws Exception {
        givenCoordsWithHeight(VIEWPORT_HEIGHT / 3);
        whenTakingScreenshot(shootingCoords);
        thenScreenshotIsHeight(VIEWPORT_HEIGHT / 3 + DEFAULT_COORDS_INDENT);
    }

    @Test
    public void testScreenshotCoordsZeroHeight() throws Exception {
        shootingCoords = new Coords(0, SHOOT_COORDS_OFFSET_Y, PAGE_WIDTH, 0);
        whenTakingScreenshot(shootingCoords);
        thenScreenshotIsHeight(DEFAULT_COORDS_INDENT);
    }

    @Test
    public void testScreenshotFullPage() throws Exception {
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

    private void givenCoordsWithHeight(int height) {
        shootingCoords = new Coords(0, SHOOT_COORDS_OFFSET_Y, PAGE_WIDTH, height);
    }

    private void whenTakingScreenshot() {
        screenshot = shootingStrategy.getScreenshot(wd);
    }

    private void whenTakingScreenshot(Coords coords) {
        coordsSet.add(coords);
        screenshot = shootingStrategy.getScreenshot(wd, coordsSet);
    }

    private void whenPreparingCoords(Set<Coords> coords) {
        preparedCoords = shootingStrategy.prepareCoords(coords);
    }

    private void thenCoordsShiftedWith(double shootCoordsOffsetY) {
        assertThat("Coords should be shifted correctly", preparedCoords, everyItem(Matchers.<Coords>hasProperty("y", Matchers.is(shootCoordsOffsetY))));
    }

    private void thenScreenshotIsHeight(int shotHeight) {
        assertThat("Screenshot height should be correct", screenshot.getHeight(), is(shotHeight));
    }

    /**
     * Math.ceil(DEFAULT_PAGE_HEIGHT / VIEWPORT_HEIGHT)
     */
    private void thenScrollTimes(int times) {
        verify(shootingStrategy, times(times)).scrollVertically(any(JavascriptExecutor.class), anyInt());
    }

    private void thenShootTimes(int times) {
        verify(((TakesScreenshot) wd), times(times)).getScreenshotAs(any(OutputType.class));
    }

    class MockVerticalPastingShootingDecorator extends ViewportPastingDecorator {

        int pageHeight = DEFAULT_PAGE_HEIGHT;
        int pageWidth = PAGE_WIDTH;
        int viewportHeight = VIEWPORT_HEIGHT;
        int currentScrollY = 0;

        public MockVerticalPastingShootingDecorator(ShootingStrategy strategy) {
            super(strategy);
        }

        @Override
        public int getFullHeight(WebDriver driver) {
            return pageHeight;
        }

        @Override
        public int getFullWidth(WebDriver driver) {
            return pageWidth;
        }

        @Override
        public int getWindowHeight(WebDriver driver) {
            return viewportHeight;
        }

        @Override
        public int getCurrentScrollY(JavascriptExecutor js) {
            return currentScrollY;
        }

    }
}
