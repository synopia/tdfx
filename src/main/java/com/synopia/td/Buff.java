package com.synopia.td;

/**
 * Created by synopia on 30.12.2014.
 */
public class Buff extends Effect {
    private float duration;
    private int ticks;
    private boolean stackable;
    private Effect effect;

    private float timeToTick;
    private int currentTick;

    @Override
    public boolean tick(Unit unit, float dt) {
        timeToTick -= dt;
        if (timeToTick < 0) {
            if (currentTick < ticks) {
                effect.tick(unit, dt);
                currentTick++;
                timeToTick = duration / ticks;
            } else {
                return false;
            }
        }
        return true;
    }
}
