package com.synopia.td;

/**
 * Created by synopia on 30.12.2014.
 */
public class Laser extends Projectile {
    @Override
    public void tick(float dt) {
        hit();
    }

    @Override
    public boolean reachedTarget() {
        return true;
    }
}
