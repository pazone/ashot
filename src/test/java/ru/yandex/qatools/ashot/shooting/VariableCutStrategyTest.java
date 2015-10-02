package ru.yandex.qatools.ashot.shooting;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.shooting.cutter.CutStrategy;
import ru.yandex.qatools.ashot.shooting.cutter.VariableCutStrategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static ru.yandex.qatools.ashot.shooting.cutter.VariableCutStrategy.SCRIPT;

/**
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class VariableCutStrategyTest {
    private MockSettings wdSettings = withSettings().extraInterfaces(JavascriptExecutor.class);
    private WebDriver wd = mock(WebDriver.class, wdSettings);
    private static final int MAX_HEADER_HEIGHT = 65;
    private static final int MIN_HEADER_HEIGHT = 41;
    private static final Long MIN_INNER_HEIGHT = 960L;
    private static final Long MAX_INNER_HEIGHT = 984L;
    private CutStrategy strategy = spy(new VariableCutStrategy(
            MIN_HEADER_HEIGHT, MAX_HEADER_HEIGHT, MIN_INNER_HEIGHT.intValue()));
    private int headerHeight;

    @Test
    public void testHeaderHeightIsMaximum() throws Exception {
        givenViewportInnerHeight(MIN_INNER_HEIGHT);
        whenGettingHeaderHeight();
        thenBrowserHeaderHeightIs(MAX_HEADER_HEIGHT);
    }

    @Test
    public void testHeaderHeightIsMinimum() throws Exception {
        givenViewportInnerHeight(MAX_INNER_HEIGHT);
        whenGettingHeaderHeight();
        thenBrowserHeaderHeightIs(MIN_HEADER_HEIGHT);
    }

    @Test(expected = InvalidViewportHeightException.class)
    public void testClassCastException() throws Exception {
        givenViewportInnerHeight("a string");
        whenGettingHeaderHeight();
    }

    @Test(expected = InvalidViewportHeightException.class)
    public void testJavaScriptReturnedNull() throws Exception {
        givenViewportInnerHeight(null);
        whenGettingHeaderHeight();
    }

    private OngoingStubbing<Object> givenViewportInnerHeight(Object obj) {
        return when(((JavascriptExecutor) wd).executeScript(SCRIPT)).thenReturn(obj);
    }

    private void thenBrowserHeaderHeightIs(int headerHeight) {
        assertThat("Header height should be detected correctly", headerHeight, is(this.headerHeight));
    }

    private void whenGettingHeaderHeight() {
        headerHeight = strategy.getHeaderHeight(wd);
    }

}
