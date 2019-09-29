package pazone.ashot;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import pazone.ashot.cutter.CutStrategy;
import pazone.ashot.cutter.VariableCutStrategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
@ExtendWith(MockitoExtension.class)
class VariableCutStrategyTest {

    private static final int MAX_HEADER_HEIGHT = 65;
    private static final int MIN_HEADER_HEIGHT = 41;
    private static final int MIN_INNER_HEIGHT = 960;

    private final CutStrategy strategy = new VariableCutStrategy(MIN_HEADER_HEIGHT, MAX_HEADER_HEIGHT,
            MIN_INNER_HEIGHT);

    @Mock(extraInterfaces = JavascriptExecutor.class)
    private WebDriver webDriver;

    @ParameterizedTest
    @CsvSource({
            "960, 65",
            "984, 41"
    })
    void testGetBrowserHeaderHeight(long viewportHeight, int browserHeaderHeight) {
        mockViewportInnerHeight(viewportHeight);
        int headerHeight = strategy.getHeaderHeight(webDriver);
        assertThat("Header height should be detected correctly", browserHeaderHeight, is(headerHeight));
    }

    @ParameterizedTest
    @CsvSource({
            "a string",
            ","
    })
    void testGetBrowserHeaderHeightWithInvalidViewportHeight(String viewportHeight) {
        mockViewportInnerHeight(viewportHeight);
        assertThrows(InvalidViewportHeightException.class, () -> strategy.getHeaderHeight(webDriver));
    }

    private void mockViewportInnerHeight(Object viewportHeight) {
        when(((JavascriptExecutor) webDriver).executeScript(VariableCutStrategy.SCRIPT)).thenReturn(viewportHeight);
    }
}
