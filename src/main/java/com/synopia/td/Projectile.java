package com.synopia.td;

import com.synopia.engine.HasSprite;
import com.synopia.engine.Sprite;
import com.synopia.engine.Tickable;

import javax.vecmath.Vector2f;
import java.util.List;

/**
 * Created by synopia on 30.12.2014.
 */
public abstract class Projectile implements HasSprite, Tickable {
    private Vector2f position;

    private Unit target;
    private List<Effect> effects;
    private Sprite sprite;

    public void hit() {
        effects.forEach(target::addEffect);
    }

    public abstract boolean reachedTarget();

    public void setTarget(Unit target) {
        this.target = target;
    }

    public Unit getTarget() {
        return target;
    }

    public void setPosition(Vector2f position) {
        sprite.setPosition(position);
        this.position = position;
    }

    public Vector2f getPosition() {
        return position;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

}
