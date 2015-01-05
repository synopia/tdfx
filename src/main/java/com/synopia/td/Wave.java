package com.synopia.td;

import com.synopia.engine.Tickable;

import java.util.List;

/**
 * Created by synopia on 05.01.2015.
 */
public class Wave implements Tickable {
    private List<Spawn> spawns;

    @Override
    public void tick(float dt) {
        for (Spawn spawn : spawns) {
            spawn.tick(dt);
        }
    }

    public void setSpawnPosition(Position spawnPosition) {
        for (Spawn spawn : spawns) {
            spawn.setSpawnPosition(spawnPosition);
        }
    }

    public void setWorld(World world) {
        for (Spawn spawn : spawns) {
            spawn.setWorld(world);
        }
    }
}
