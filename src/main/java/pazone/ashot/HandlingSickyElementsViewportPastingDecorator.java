package pazone.ashot;

import java.awt.image.BufferedImage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class HandlingSickyElementsViewportPastingDecorator extends ViewportPastingDecorator {

    private final int stickyHeaderHeight;
    private final int stickyFooterHeight;
    private int currentChunkIndex;

    public HandlingSickyElementsViewportPastingDecorator(ShootingStrategy strategy, int stickyHeaderHeight,
            int stickyFooterHeight) {
        super(strategy);
        this.stickyHeaderHeight = stickyHeaderHeight;
        this.stickyFooterHeight = stickyFooterHeight;
    }

    @Override
    protected PageDimensions getPageDimensions(WebDriver driver) {
        PageDimensions pageDimension = super.getPageDimensions(driver);
        return new PageDimensions(pageDimension.getPageHeight(), pageDimension.getViewportWidth(),
                pageDimension.getViewportHeight() - stickyHeaderHeight - stickyFooterHeight);
    }

    @Override
    protected BufferedImage getChunk(WebDriver wd, int currentChunkIndex, int totalNumberOfChunks) {
        this.currentChunkIndex = currentChunkIndex;
        CuttingDecorator cuttingDecorator = new CuttingDecorator(getShootingStrategy());
        if (currentChunkIndex == 0) {
            cuttingDecorator.withCut(0, stickyFooterHeight);
        } else if (currentChunkIndex == totalNumberOfChunks - 1) {
            cuttingDecorator.withCut(stickyHeaderHeight, 0);
        } else {
            cuttingDecorator.withCut(stickyHeaderHeight, stickyFooterHeight);
        }
        return cuttingDecorator.getScreenshot(wd);
    }

    @Override
    protected int getCurrentScrollY(JavascriptExecutor js) {
        int currentScrollY = super.getCurrentScrollY(js);
        return currentChunkIndex == 0 ? currentScrollY : currentScrollY + stickyHeaderHeight;
    }
}
