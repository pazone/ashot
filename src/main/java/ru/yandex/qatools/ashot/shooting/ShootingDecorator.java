package ru.yandex.qatools.ashot.shooting;

abstract class ShootingDecorator implements ShootingStrategy {

    private ShootingStrategy shootingStrategy;

    public ShootingDecorator(ShootingStrategy shootingStrategy) {
        this.shootingStrategy = shootingStrategy;
    }

    public ShootingStrategy getShootingStrategy() {
        return shootingStrategy;
    }
}
