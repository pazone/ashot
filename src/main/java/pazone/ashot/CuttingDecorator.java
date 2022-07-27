package pazone.ashot;

import org.openqa.selenium.WebDriver;
import pazone.ashot.cutter.CutStrategy;
import pazone.ashot.cutter.FixedCutStrategy;
import pazone.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * Cuts browser's header/footer off from screenshot.
 *
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */
public class CuttingDecorator extends ShootingDecorator {

    private CutStrategy cutStrategy;

    public CuttingDecorator(ShootingStrategy strategy) {
        super(strategy);
    }

    /**
     * Will use {@link FixedCutStrategy} to cut off header and footer.
     * @param headerToCut - height of header in pixels
     * @param footerToCut - height of footer in pixels
     * @return Cutting decorator
     */
    public CuttingDecorator withCut(int headerToCut, int footerToCut) {
        return withCutStrategy(new FixedCutStrategy(headerToCut, footerToCut));
    }

    /**
     * Will use {@link FixedCutStrategy} to cut off header and footer.
     * @param headerToCut - height of header in pixels
     * @param footerToCut - height of footer in pixels
     * @param leftBarToCut - width of left bar in pixels
     * @param rightBarToCut - width of right bar in pixels
     * @return Cutting decorator
     */
    public CuttingDecorator withCut(int headerToCut, int footerToCut, int leftBarToCut, int rightBarToCut) {
        return withCutStrategy(new FixedCutStrategy(headerToCut, footerToCut, leftBarToCut, rightBarToCut));
    }

    /**
     * Will use custom cut strategy, for example {@link pazone.ashot.cutter.VariableCutStrategy}.
     * @param cutStrategy - strategy to get height of browser's header
     * @return Cutting decorator
     */
    public CuttingDecorator withCutStrategy(CutStrategy cutStrategy) {
        this.cutStrategy = cutStrategy;
        return this;
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd) {
        BufferedImage baseImage = getShootingStrategy().getScreenshot(wd);
        int h = baseImage.getHeight();
        int w = baseImage.getWidth();
        final int headerToCut = getHeaderToCut(wd);
        final int footerToCut = getFooterToCut(wd);
        final int leftBarToCut = getLeftBarToCut(wd);
        final int rightBarToCut = getRightBarToCut(wd);
        return baseImage.getSubimage(leftBarToCut, headerToCut, w - leftBarToCut - rightBarToCut,
            h - headerToCut - footerToCut);
    }

    @Override
    public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coords) {
        return getScreenshot(wd);
    }

    protected int getHeaderToCut(WebDriver wd) {
        return cutStrategy.getHeaderHeight(wd);
    }

    protected int getFooterToCut(WebDriver wd) {
        return cutStrategy.getFooterHeight(wd);
    }

    protected int getLeftBarToCut(WebDriver wd) {
        return cutStrategy.getLeftBarWidth(wd);
    }

    protected int getRightBarToCut(WebDriver wd) {
        return cutStrategy.getRightBarWidth(wd);
    }
}
