package pazone.ashot;

public final class PageDimensions {
    private final int pageHeight;
    private final int viewportWidth;
    private final int viewportHeight;

    public PageDimensions(int pageHeight, int viewportWidth, int viewportHeight) {
        this.pageHeight = pageHeight;
        this.viewportHeight = viewportHeight;
        this.viewportWidth = viewportWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }
}
