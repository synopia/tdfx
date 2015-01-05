package com.synopia.td;

import com.synopia.engine.EntityFactory;
import com.synopia.engine.Tickable;

/**
 * Created by synopia on 05.01.2015.
 */
public class Spawn implements Tickable {
    private EntityFactory<Unit> unit;
    private int count;
    private float delay;
    private float duration;
    private transient float spawnTime;
    private transient int spawned;
    private transient Position spawnPosition;
    private transient World world;

    @Override
    public void tick(float dt) {
        if (delay <= 0) {
            spawnTime += dt;
            if (spawnTime < duration && spawned < count) {
                int toBeSpawned;
                if (duration > 0) {
                    toBeSpawned = (int) (spawnTime / duration * count);
                } else {
                    toBeSpawned = count;
                }
                int toSpawn = toBeSpawned - spawned;
                for (int i = 0; i < toSpawn; i++) {
                    Unit spawn = this.unit.create();
                    spawn.setWorld(world);
                    spawn.setPosition(spawnPosition.toVector2f());
                }
                spawned += toSpawn;
            }
        } else {
            delay -= dt;
        }
    }

    public void setSpawnPosition(Position spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
