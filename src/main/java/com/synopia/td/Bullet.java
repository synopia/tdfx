package com.synopia.td;

/**
 * Created by synopia on 30.12.2014.
 */
public class Bullet extends Projectile {
    private int bulletCount;
    private float hitChance;

    @Override
    public void tick(float dt) {
        int hits = World.rollBullets(bulletCount, hitChance);
        for (int i = 0; i < hits; i++) {
            hit();
        }
    }

    @Override
    public boolean reachedTarget() {
        return true;
    }
}
