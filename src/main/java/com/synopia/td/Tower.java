package com.synopia.td;

import com.synopia.engine.HasSprite;
import com.synopia.engine.Initializable;
import com.synopia.engine.Sprite;
import com.synopia.engine.Tickable;

/**
 * Created by synopia on 30.12.2014.
 */
public class Tower implements Initializable, HasSprite, Tickable {
    private transient World world;

    private boolean passable;
    private Position size;
    private Position position;

    private Unit target;
    private Weapon weapon;
    private Sprite sprite;

    public Tower() {
    }

    @Override
    public void initialize() {
        weapon.setOwner(this);
        sprite.setSize(size.toVector2f());
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        sprite.setPosition(position.toVector2f());

    }

    @Override
    public void tick(float dt) {
        if (position == null) {
            return;
        }
        target = world.findTargetSticky(this);
        getWeapon().fire(target);
        getWeapon().tick(dt);
    }


    public Unit getTarget() {
        return target;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    public boolean isPassable() {
        return passable;
    }

    public Position getSize() {
        return size;
    }

    public BlockSelection getGroundBlocks(Grid grid, Position position) {
        return BlockSelection.fromRectangle(grid, position.x(), position.y(), getSize().x(), getSize().y());
    }


}
