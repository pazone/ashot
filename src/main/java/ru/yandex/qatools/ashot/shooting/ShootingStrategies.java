package ru.yandex.qatools.ashot.shooting;

import ru.yandex.qatools.ashot.shooting.cutter.CutStrategy;
import ru.yandex.qatools.ashot.shooting.cutter.FixedCutStrategy;
import ru.yandex.qatools.ashot.shooting.cutter.VariableCutStrategy;

/**
 * Utility class for different shooting strategies.
 */
public final class ShootingStrategies {

    private static final int SCROLL_TIMEOUT_IOS = 500;
    private static final int HEADER_IOS_7 = 98;
    private static final int HEADER_IOS_8_MIN = 41;
    private static final int HEADER_IOS_8_MAX = 65;
    private static final int VIEWPORT_MIN_IOS_8 = 960;
    private static final int VIEWPORT_MIN_IOS_8_SIM = 1250;

    private ShootingStrategies() {
        throw new UnsupportedOperationException();
    }

    /**
     * Simple shooting strategy. No image processing is performed.
     * @return new instance of SimpleShootingStrategy
     */
    public static ShootingStrategy simple() {
        return new SimpleShootingStrategy();
    }

    /**
     * Will scale down image according to dpr specified.
     *
     * @param dpr device pixel ratio
     * @return {@code ShootingStrategy} that will scale image according to {@code dpr}
     */
    public static ShootingStrategy scaling(float dpr) {
        return new ScalingDecorator(new SimpleShootingStrategy()).withDpr(dpr);
    }

    /**
     * Will cut header and footer off from screen shot.
     *
     * @param strategy {@link CutStrategy} to use. See {@link FixedCutStrategy} or {@link VariableCutStrategy}
     * @return {@code ShootingStrategy} with custom cutting strategy
     */
    public static ShootingStrategy cutting(CutStrategy strategy) {
        return new CuttingDecorator(new SimpleShootingStrategy()).withCutStrategy(strategy);
    }

    /**
     * Will cut header and footer off from screen shot.
     *
     * @param headerToCut header to cut in pixels
     * @param footerToCut footer to cut in pixels
     * @return {@code ShootingStrategy} with {@link FixedCutStrategy} cutting strategy
     */
    public static ShootingStrategy cutting(int headerToCut, int footerToCut) {
        return new CuttingDecorator(new SimpleShootingStrategy())
                .withCutStrategy(new FixedCutStrategy(headerToCut, footerToCut));
    }

    /**
     * Will scroll viewport while shooting.
     *
     * @param scrollTimeout time between viewportPasting scrolls in milliseconds
     * @return {@code ShootingStrategy} with custom timeout between scrolls
     */
    public static ShootingStrategy viewportPasting(int scrollTimeout) {
        return new ViewportPastingDecorator(new SimpleShootingStrategy())
                .withScrollTimeout(scrollTimeout);
    }

    /**
     * Will scroll viewport while shooting and cut off browser's header and footer
     *
     * @param scrollTimeout time between scrolls in milliseconds
     * @param cutStrategy strategy to cut header and footer from image
     *
     * @return {@code ShootingStrategy} with custom scroll timeout and cutting strategy
     */
    public static ShootingStrategy viewportNonRetina(int scrollTimeout, CutStrategy cutStrategy) {
        return new ViewportPastingDecorator(cutting(cutStrategy)).withScrollTimeout(scrollTimeout);
    }

    /**
     * Will scroll viewportPasting while shooting and cut off browser's header and footer
     *
     * @param scrollTimeout time between scrolls in milliseconds
     * @param headerToCut height of header to cut from image
     * @param footerToCut height of footer to cut from image
     *
     * @return {@code ShootingStrategy} with custom scroll timeout and header/footer to cut
     */
    public static ShootingStrategy viewportNonRetina(int scrollTimeout, int headerToCut, int footerToCut) {
        return viewportNonRetina(scrollTimeout, new FixedCutStrategy(headerToCut, footerToCut));
    }

    /**
     * Will scale screenshots and scroll viewportPasting while shooting and cut off browser's header/footer
     *
     * @param scrollTimeout time between scrolls in milliseconds
     * @param cutStrategy strategy to cut header and footer from image
     * @param dpr device pixel ratio
     *
     * @return {@code ShootingStrategy} with custom DPR scaling and scroll timeout and cutting strategy
     */
    public static ShootingStrategy viewportRetina(int scrollTimeout, CutStrategy cutStrategy, float dpr) {
        SimpleShootingStrategy shootingStrategy = new SimpleShootingStrategy();
        ScalingDecorator scalingDecorator = new ScalingDecorator(shootingStrategy).withDpr(dpr);
        CuttingDecorator cuttingDecorator = new CuttingDecorator(scalingDecorator).withCutStrategy(cutStrategy);
        return new ViewportPastingDecorator(cuttingDecorator).withScrollTimeout(scrollTimeout);
    }

    /**
     * Will scale screenshots and scroll viewportPasting while shooting and cut off browser's header/footer
     *
     * @param scrollTimeout time between scrolls in milliseconds
     * @param headerToCut height of header to cut from image
     * @param footerToCut height of footer to cut from image
     * @param dpr device pixel ratio
     *
     * @return {@code ShootingStrategy} with custom DPR scaling and scroll timeout and cutting strategy
     */
    public static ShootingStrategy viewportRetina(int scrollTimeout, int headerToCut, int footerToCut, float dpr) {
        return viewportRetina(scrollTimeout, new FixedCutStrategy(headerToCut, footerToCut), dpr);
    }

    public static ShootingStrategy iPad2WithIOS7() {
        return viewportNonRetina(SCROLL_TIMEOUT_IOS, HEADER_IOS_7, 0);
    }

    public static ShootingStrategy iPad2WithIOS8() {
        VariableCutStrategy cutStrategy =
                new VariableCutStrategy(HEADER_IOS_8_MIN, HEADER_IOS_8_MAX, VIEWPORT_MIN_IOS_8);
        return viewportNonRetina(SCROLL_TIMEOUT_IOS, cutStrategy);
    }

    public static ShootingStrategy iPad2WithIOS8Simulator() {
        VariableCutStrategy cutStrategy =
                new VariableCutStrategy(HEADER_IOS_8_MIN, HEADER_IOS_8_MAX, VIEWPORT_MIN_IOS_8_SIM);
        return viewportNonRetina(SCROLL_TIMEOUT_IOS, cutStrategy);
    }

    public static ShootingStrategy iPad2WithIOS8Retina() {
        VariableCutStrategy cutStrategy =
                new VariableCutStrategy(HEADER_IOS_8_MIN, HEADER_IOS_8_MAX, VIEWPORT_MIN_IOS_8);
        return viewportRetina(SCROLL_TIMEOUT_IOS, cutStrategy, 2F);
    }

    public static ShootingStrategy iPad2WithIOS8RetinaSimulator() {
        VariableCutStrategy cutStrategy =
                new VariableCutStrategy(HEADER_IOS_8_MIN, HEADER_IOS_8_MAX, VIEWPORT_MIN_IOS_8_SIM);
        return viewportRetina(SCROLL_TIMEOUT_IOS, cutStrategy, 2F);
    }

    /**
     * Will create screenshot's of the whole page and rotate landscape images
     *
     * @param scrollTimeout time between scrolls in milliseconds
     * @param cutStrategy strategy to cut header and footer from image
     * @return {@code ShootingStrategy} witch will shoot whole page and rotate landscape images
     */
    public static ShootingStrategy iPadLandscapeOrientation(int scrollTimeout, CutStrategy cutStrategy) {
        return new ViewportPastingDecorator(new RotatingDecorator(cutStrategy, simple()))
                .withScrollTimeout(scrollTimeout);
    }

    /**
     * Will rotate screenshot's made on iPad in landscape orientation
     *
     * @param cutStrategy strategy to cut header and footer from image
     * @return {@code ShootingStrategy} witch will rotate image
     */
    public static ShootingStrategy iPadLandscapeOrientationSimple(CutStrategy cutStrategy) {
        return new RotatingDecorator(cutStrategy, simple());
    }
}
