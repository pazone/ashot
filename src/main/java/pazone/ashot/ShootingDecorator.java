package pazone.ashot;

import pazone.ashot.coordinates.Coords;

import java.util.Set;

public abstract class ShootingDecorator implements ShootingStrategy {

    private final ShootingStrategy shootingStrategy;

    protected ShootingDecorator(ShootingStrategy shootingStrategy) {
        this.shootingStrategy = shootingStrategy;
    }

    public ShootingStrategy getShootingStrategy() {
        return shootingStrategy;
    }

    /**
     * Default behavior is not to change coords, because by default coordinates are not changed
     */
    @Override
    public Set<Coords> prepareCoords(Set<Coords> coordsSet) {
        return coordsSet;
    }
}
