package com.synopia.td;

import com.google.common.collect.Lists;
import com.synopia.engine.HasSprite;
import com.synopia.engine.Initializable;
import com.synopia.engine.Sprite;
import com.synopia.engine.Tickable;
import com.synopia.engine.sprites.CompositeSprite;
import com.synopia.engine.sprites.ProgressBar;

import javax.vecmath.Vector2f;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by synopia on 30.12.2014.
 */
public class Unit implements Initializable, HasSprite, Tickable {
    private transient World world;
    private Vector2f position;

    private float radius;
    private float direction;

    private Armor armor;

    private float hitPoints;
    private float maxHitPoints;

    private float regenerationRate;
    private float regenerationHitPoints;

    private float movementSpeed;
    private Sprite sprite;
    private CompositeSprite unit;
    private ProgressBar healthBar = new ProgressBar();

    private List<Effect> activeEffects = Lists.newArrayList();
    private transient List<Effect> newEffects = Lists.newArrayList();
    private transient Path activePath;
    private transient Future<Path> requestedPath;

    @Override
    public void initialize() {
        unit = new CompositeSprite();
        unit.addChild(sprite);
        unit.addChild(healthBar);
        healthBar.setProgressValue(hitPoints / maxHitPoints);
    }

    @Override
    public Sprite getSprite() {
        return unit;
    }

    public void applyDamage(DamageType damageType, float damage) {
        hitPoints -= armor.applyDamage(damageType, damage);
        healthBar.setProgressValue(hitPoints / maxHitPoints);
    }

    public void addEffect(Effect effect) {
        newEffects.add(effect);
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public float getHitPoints() {
        return hitPoints;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        unit.setPosition(position);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void tick(float dt) {
        updateEffects(dt);

        moveToGoal(dt);
    }

    private void moveToGoal(float dt) {
        Position currentPosition = new Position(position);
        if (activePath == null) {
            if (requestedPath == null) {
                requestedPath = world.getGrid().findPathToGoal(currentPosition);
            }
            if (requestedPath.isDone()) {
                try {
                    activePath = requestedPath.get();
                } catch (InterruptedException | ExecutionException e) {
                    //ignore
                }
                requestedPath = null;
            }
        }
        if (activePath != null) {
            movePath(dt);
            if (activePath.finished()) {
                activePath = null;
            }
        }
    }

    private void movePath(float dt) {
        float remainingDist = movementSpeed * dt;
        while (remainingDist > 0 && !activePath.finished()) {
            Vector2f diff = new Vector2f();
            diff.sub(activePath.get(0).toVector2f(), position);
            float dist = diff.length();
            if (dist < 0.01f) {
                activePath.step();
            } else {
                float moveDist = Math.min(dist, remainingDist);
                remainingDist -= moveDist;
                diff.scale(moveDist / dist);
                diff.add(position);
                position = diff;
            }
        }
        setPosition(position);
    }

    private void updateEffects(float dt) {
        activeEffects.addAll(newEffects);

        Iterator<Effect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            boolean newEffect = newEffects.remove(effect);
            if (newEffect) {
                effect.bind(this);
            }
            boolean keepAlive = effect.tick(this, dt);
            System.out.println(hitPoints);
            if (!keepAlive) {
                effect.unbind(this);
                iterator.remove();
            }
        }
    }

    public boolean isAlive() {
        return hitPoints > 0;
    }
}
