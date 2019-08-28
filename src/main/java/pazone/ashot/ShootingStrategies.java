package pazone.ashot;

import pazone.ashot.cutter.CutStrategy;
import pazone.ashot.cutter.FixedCutStrategy;
import pazone.ashot.cutter.VariableCutStrategy;

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

    private static final CutStrategy CUT_STRATEGY_IOS_7 = new FixedCutStrategy(HEADER_IOS_7, 0);
    private static final CutStrategy CUT_STRATEGY_IOS_8 = iOS8CutStrategy(VIEWPORT_MIN_IOS_8);
    private static final CutStrategy CUT_STRATEGY_IOS_8_SIM = iOS8CutStrategy(VIEWPORT_MIN_IOS_8_SIM);

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
     * @param shootingStrategy Shooting strategy used to take screenshots before scaling
     * @param dpr device pixel ratio
     * @return {@code ShootingStrategy} that will scale image according to {@code dpr}
     */
    public static ShootingStrategy scaling(ShootingStrategy shootingStrategy, float dpr) {
        return new ScalingDecorator(shootingStrategy).withDpr(dpr);
    }

    /**
     * Will scale down image according to dpr specified.
     *
     * @param dpr device pixel ratio
     * @return {@code ShootingStrategy} that will scale image according to {@code dpr}
     */
    public static ShootingStrategy scaling(float dpr) {
        return scaling(simple(), dpr);
    }

    /**
     * Will cut header and footer off from screen shot.
     *
     * @param shootingStrategy Shooting strategy used to take screenshots before cutting
     * @param cutStrategy {@link CutStrategy} to use. See {@link FixedCutStrategy} or {@link VariableCutStrategy}
     * @return {@code ShootingStrategy} with custom cutting strategy
     */
    public static ShootingStrategy cutting(ShootingStrategy shootingStrategy, CutStrategy cutStrategy) {
        return new CuttingDecorator(shootingStrategy).withCutStrategy(cutStrategy);
    }

    /**
     * Will cut header and footer off from screen shot.
     *
     * @param cutStrategy {@link CutStrategy} to use. See {@link FixedCutStrategy} or {@link VariableCutStrategy}
     * @return {@code ShootingStrategy} with custom cutting strategy
     */
    public static ShootingStrategy cutting(CutStrategy cutStrategy) {
        return cutting(simple(), cutStrategy);
    }

    /**
     * Will cut header and footer off from screen shot.
     *
     * @param headerToCut header to cut in pixels
     * @param footerToCut footer to cut in pixels
     * @return {@code ShootingStrategy} with {@link FixedCutStrategy} cutting strategy
     */
    public static ShootingStrategy cutting(int headerToCut, int footerToCut) {
        return cutting(new FixedCutStrategy(headerToCut, footerToCut));
    }

    /**
     * Will scroll viewport while shooting.
     *
     * @param shootingStrategy Shooting strategy used to take screenshots between scrolls
     * @param scrollTimeout time between viewportPasting scrolls in milliseconds
     * @return {@code ShootingStrategy} with custom timeout between scrolls
     */
    public static ShootingStrategy viewportPasting(ShootingStrategy shootingStrategy, int scrollTimeout) {
        return new ViewportPastingDecorator(shootingStrategy).withScrollTimeout(scrollTimeout);
    }

    /**
     * Will scroll viewport while shooting.
     *
     * @param scrollTimeout time between viewportPasting scrolls in milliseconds
     * @return {@code ShootingStrategy} with custom timeout between scrolls
     */
    public static ShootingStrategy viewportPasting(int scrollTimeout) {
        return viewportPasting(simple(), scrollTimeout);
    }

    /**
     * Will scroll viewport while shooting and cut off browser's header and footer
     *
     * @param shootingStrategy Underneath shooting strategy
     * @param scrollTimeout time between scrolls in milliseconds
     * @param cutStrategy strategy to cut header and footer from image
     *
     * @return {@code ShootingStrategy} with custom scroll timeout and cutting strategy
     */
    public static ShootingStrategy viewportNonRetina(ShootingStrategy shootingStrategy, int scrollTimeout,
            CutStrategy cutStrategy) {
        return viewportPasting(cutting(shootingStrategy, cutStrategy), scrollTimeout);
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
        return viewportPasting(cutting(cutStrategy), scrollTimeout);
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
     * @param shootingStrategy Underneath shooting strategy
     * @param scrollTimeout time between scrolls in milliseconds
     * @param cutStrategy strategy to cut header and footer from image
     * @param dpr device pixel ratio
     *
     * @return {@code ShootingStrategy} with custom DPR scaling and scroll timeout and cutting strategy
     */
    public static ShootingStrategy viewportRetina(ShootingStrategy shootingStrategy, int scrollTimeout,
            CutStrategy cutStrategy, float dpr) {
        ShootingStrategy scalingDecorator = scaling(shootingStrategy, dpr);
        return viewportNonRetina(scalingDecorator, scrollTimeout, cutStrategy);
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
        return viewportRetina(simple(), scrollTimeout, cutStrategy, dpr);
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

    /**
     * Will scale screenshots and scroll viewportPasting while shooting and cut off browser's header/footer
     *
     * @param shootingStrategy Underneath shooting strategy
     * @param scrollTimeout time between scrolls in milliseconds
     * @param headerToCut height of header to cut from image
     * @param footerToCut height of footer to cut from image
     * @param dpr device pixel ratio
     *
     * @return {@code ShootingStrategy} with custom DPR scaling and scroll timeout and cutting strategy
     */
    public static ShootingStrategy viewportRetina(ShootingStrategy shootingStrategy, int scrollTimeout, int headerToCut,
            int footerToCut, float dpr) {
        return viewportRetina(shootingStrategy, scrollTimeout, new FixedCutStrategy(headerToCut, footerToCut), dpr);
    }

    public static ShootingStrategy iPad2WithIOS7(ShootingStrategy shootingStrategy) {
        return viewportIOSNonRetina(shootingStrategy, CUT_STRATEGY_IOS_7);
    }

    public static ShootingStrategy iPad2WithIOS7() {
        return iPad2WithIOS7(simple());
    }

    public static ShootingStrategy iPad2WithIOS8(ShootingStrategy shootingStrategy) {
        return viewportIOSNonRetina(shootingStrategy, CUT_STRATEGY_IOS_8);
    }

    public static ShootingStrategy iPad2WithIOS8() {
        return iPad2WithIOS8(simple());
    }

    public static ShootingStrategy iPad2WithIOS8Simulator(ShootingStrategy shootingStrategy) {
        return viewportIOSNonRetina(shootingStrategy, CUT_STRATEGY_IOS_8_SIM);
    }

    public static ShootingStrategy iPad2WithIOS8Simulator() {
        return iPad2WithIOS8Simulator(simple());
    }

    public static ShootingStrategy iPad2WithIOS8Retina(ShootingStrategy shootingStrategy) {
        return viewportIOSRetina(shootingStrategy, CUT_STRATEGY_IOS_8);
    }

    public static ShootingStrategy iPad2WithIOS8Retina() {
        return iPad2WithIOS8Retina(simple());
    }

    public static ShootingStrategy iPad2WithIOS8RetinaSimulator(ShootingStrategy shootingStrategy) {
        return viewportIOSRetina(shootingStrategy, CUT_STRATEGY_IOS_8_SIM);
    }

    public static ShootingStrategy iPad2WithIOS8RetinaSimulator() {
        return iPad2WithIOS8RetinaSimulator(simple());
    }

    /**
     * Will create screenshot's of the whole page and rotate landscape images
     *
     * @param scrollTimeout time between scrolls in milliseconds
     * @param cutStrategy strategy to cut header and footer from image
     * @return {@code ShootingStrategy} which will shoot whole page and rotate landscape images
     */
    public static ShootingStrategy iPadLandscapeOrientation(int scrollTimeout, CutStrategy cutStrategy) {
        return viewportPasting(iPadLandscapeOrientationSimple(cutStrategy), scrollTimeout);
    }

    /**
     * Will rotate screenshot's made on iPad in landscape orientation
     *
     * @param cutStrategy strategy to cut header and footer from image
     * @return {@code ShootingStrategy} which will rotate image
     */
    public static ShootingStrategy iPadLandscapeOrientationSimple(CutStrategy cutStrategy) {
        return new RotatingDecorator(cutStrategy, simple());
    }

    private static ShootingStrategy viewportIOSNonRetina(ShootingStrategy shootingStrategy, CutStrategy cutStrategy) {
        return viewportNonRetina(shootingStrategy, SCROLL_TIMEOUT_IOS, cutStrategy);
    }

    private static ShootingStrategy viewportIOSRetina(ShootingStrategy shootingStrategy, CutStrategy cutStrategy) {
        return viewportRetina(shootingStrategy, SCROLL_TIMEOUT_IOS, cutStrategy, 2F);
    }

    private static final CutStrategy iOS8CutStrategy(int minViewport) {
        return new VariableCutStrategy(HEADER_IOS_8_MIN, HEADER_IOS_8_MAX, minViewport);
    }
}
