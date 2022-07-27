package pazone.ashot;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pazone.ashot.util.TestImageUtils.assertImageEquals;

import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;

import pazone.ashot.cutter.FixedCutStrategy;
import pazone.ashot.util.TestImageUtils;

class CuttingDecoratorTest {
    public static final String CUT_FROM_ALL_THE_SIDES_PNG = "cut-from-all-the-sides.png";

    @Test
    void shouldCutFromAllTheSides() {
        testCuttingFromAllTheSides(cd -> cd.withCut(35, 35, 40, 40), CUT_FROM_ALL_THE_SIDES_PNG);
    }

    @Test
    void shouldCutFromAllTheSidesUsingFixedCutStrategy() {
        testCuttingFromAllTheSides(cd -> cd.withCutStrategy(new FixedCutStrategy(35, 35, 40, 40)),
            CUT_FROM_ALL_THE_SIDES_PNG);
    }

    @Test
    void shouldCutOnlyFooterAndHeader() {
        testCuttingFromAllTheSides(cd -> cd.withCut(35, 35), "cut-footer-header.png");
    }

    private void testCuttingFromAllTheSides(UnaryOperator<CuttingDecorator> settings, String expectedImageName) {
        ShootingStrategy shootingStrategy = mock(ShootingStrategy.class);
        CuttingDecorator cuttingDecorator = new CuttingDecorator(shootingStrategy);
        when(shootingStrategy.getScreenshot(null)).thenReturn(TestImageUtils.IMAGE_B_SMALL);
        assertImageEquals(settings.apply(cuttingDecorator).getScreenshot(null),  "img/expected/" + expectedImageName);
    }
}
