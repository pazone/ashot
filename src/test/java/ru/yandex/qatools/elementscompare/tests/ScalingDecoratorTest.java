package ru.yandex.qatools.elementscompare.tests;

import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ScalingDecorator;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;
import ru.yandex.qatools.ashot.util.ImageTool;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static ru.yandex.qatools.elementscompare.tests.DifferTest.loadImage;


/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ScalingDecoratorTest {

    public static WebDriver getDriver() throws IOException {
        WebDriver driverMock = mock(WebDriver.class, withSettings()
                .extraInterfaces(TakesScreenshot.class, JavascriptExecutor.class));

        when(asTakingScreenshot(driverMock).getScreenshotAs(OutputType.BYTES))
                .thenReturn(ImageTool.toByteArray(DifferTest.IMAGE_A_SMALL));

        when(asJavascriptExecutor(driverMock).executeScript(any(String.class))).thenReturn("{0,0,100,100}");

        return driverMock;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Test
    public void testDpr() throws Exception {
        ShootingStrategy dpr2Strategy = new ScalingDecorator(new SimpleShootingStrategy())
                .withDpr(2);
        Screenshot screenshot = new AShot().shootingStrategy(dpr2Strategy).takeScreenshot(getDriver());
        assertThat(screenshot.getImage(), ImageTool.equalImage(loadImage("img/expected/dpr.png")));
    }

    private static TakesScreenshot asTakingScreenshot(WebDriver driver) {
        return (TakesScreenshot) driver;
    }

    private static JavascriptExecutor asJavascriptExecutor(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }

}

