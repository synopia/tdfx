package com.synopia.td;

import com.google.common.collect.Lists;
import com.synopia.engine.EntityFactory;
import com.synopia.engine.Tickable;

import javax.vecmath.Vector2f;
import java.util.Iterator;
import java.util.List;

/**
 * Created by synopia on 30.12.2014.
 */
public class Weapon implements Tickable {
    private Tower owner;

    private float range;
    private float cooldown;
    private EntityFactory<Projectile> projectile;
    private List<Projectile> firedProjectiles = Lists.newArrayList();

    private float timeToWait;

    public Projectile fire(Unit target) {
        if (target != null && timeToWait <= 0) {
            if (inRange(target)) {
                Projectile p = projectile.create();
                p.setTarget(target);
                p.setPosition(owner.getPosition().toVector2f());
                firedProjectiles.add(p);
                timeToWait = cooldown;
                return p;
            }
        }
        return null;
    }

    @Override
    public void tick(float dt) {
        if (timeToWait > 0) {
            timeToWait -= dt;
        }
        Iterator<Projectile> iterator = firedProjectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.tick(dt);
            if (projectile.reachedTarget()) {
                this.projectile.remove(projectile);
                iterator.remove();
            }
        }
    }

    public boolean inRange(Unit target) {
        Vector2f dist = new Vector2f();
        dist.sub(owner.getPosition().toVector2f(), target.getPosition());
        return dist.length() < range;
    }

    public void setOwner(Tower owner) {
        this.owner = owner;
    }

    public float getRange() {
        return range;
    }
}
