package com.synopia.td;

import javax.vecmath.Vector2f;

/**
 * Created by synopia on 30.12.2014.
 */
public class Rocket extends Projectile {
    private float speed;


    @Override
    public boolean reachedTarget() {
        Vector2f dist = new Vector2f();
        dist.sub(getPosition(), getTarget().getPosition());
        return dist.length() < 0.1f;
    }

    protected void move(float speed, float dt) {
        Vector2f dist = new Vector2f();
        dist.sub(getTarget().getPosition(), getPosition());
        float length = dist.length();
        float maxDist = speed * dt;
        float moveLength = Math.min(length, maxDist);
        dist.scale(moveLength / length);
        dist.add(getPosition());
        setPosition(dist);
    }

    @Override
    public void tick(float dt) {
        if (!reachedTarget()) {
            move(speed, dt);
        }
        if (reachedTarget()) {
            hit();
        }
    }
}
