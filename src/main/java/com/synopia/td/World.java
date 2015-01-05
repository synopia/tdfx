package com.synopia.td;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.synopia.engine.EntityManager;
import com.synopia.engine.InheritanceAdapter;
import com.synopia.engine.Sprite;
import com.synopia.engine.Tickable;
import com.synopia.engine.UniqueAdapter;
import com.synopia.engine.sprites.Circle;
import com.synopia.engine.sprites.Rect;
import javafx.scene.Group;

import javax.vecmath.Vector2f;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by synopia on 30.12.2014.
 */
public class World implements Tickable {
    private static Random random = new Random();
    private Group gridNodes;

    private List<ArmorType> armorTypes;
    private List<DamageType> damageTypes;
    private Grid grid;
    protected EntityManager entityManager;
    private final Group spriteGroup;
    private Wave wave;

    public World(Group sceneNodes) {
        super();
        gridNodes = new Group();
        sceneNodes.getChildren().add(gridNodes);
        spriteGroup = new Group();
        entityManager = new EntityManager(spriteGroup);
    }

    @Override
    public void tick(float dt) {
        for (Tower tower : entityManager.getEntities(Tower.class)) {
            tower.tick(dt);
        }
        List<Unit> removal = Lists.newArrayList();
        for (Unit unit : entityManager.getEntities(Unit.class)) {
            unit.tick(dt);
            if (!unit.isAlive()) {
                removal.add(unit);
            }
        }
        for (Unit unit : removal) {
            entityManager.remove(Unit.class, unit);
        }

        wave.tick(dt);
    }

    public Grid loadGrid(String name) {
        grid = entityManager.create(Grid.class, name);
        grid.initialize();
        grid.registerListeners(gridNodes);

        double width = gridNodes.getScene().getWidth() - gridNodes.getScene().getX();
        double height = gridNodes.getScene().getHeight() - gridNodes.getScene().getY();

        gridNodes.setScaleX(width / grid.getWidth());
        gridNodes.setScaleY(height / grid.getHeight());
        gridNodes.setTranslateX(width / 2);
        gridNodes.setTranslateY(height / 2);

        gridNodes.getChildren().add(spriteGroup);

        return grid;
    }

    public Wave loadWave(String name) {
        wave = entityManager.create(Wave.class, name);
        wave.setWorld(this);
        wave.setSpawnPosition(grid.getSpawnBlocks().get(0).getPosition());
        return wave;
    }

    public Tower createTower(String name) {
        Tower tower = entityManager.create(Tower.class, name);
        tower.setWorld(this);
        return tower;
    }

    public Unit createUnit(String name) {
        Unit unit = entityManager.create(Unit.class, name);
        unit.setWorld(this);
        return unit;
    }

    public void remove(Unit unit) {
        entityManager.remove(Unit.class, unit);
    }

    public Unit findTargetNearest(Tower tower) {
        Vector2f dist = new Vector2f();
        float nearestDist = tower.getWeapon().getRange();
        Unit nearest = null;
        for (Unit unit : entityManager.getEntities(Unit.class)) {
            if (!unit.isAlive()) {
                continue;
            }
            dist.sub(tower.getPosition().toVector2f(), unit.getPosition());
            float length = dist.length();
            if (length < nearestDist) {
                nearestDist = length;
                nearest = unit;
            }
        }
        return nearest;
    }

    public Unit findTargetSticky(Tower tower) {
        Unit currentTarget = tower.getTarget();
        if (currentTarget != null && currentTarget.isAlive()) {
            return currentTarget;
        }
        return findTargetNearest(tower);
    }


    public void findDamageTypes() {
        GsonBuilder builder = entityManager.getGsonBuilder();
        builder.registerTypeAdapter(DamageType.class, new UniqueAdapter<>((id, json, context) -> new DamageType(id)));
        Gson gson = builder.create();
        builder.registerTypeAdapter(ArmorType.class, new UniqueAdapter<>((id, json, context) -> gson.fromJson(json, new TypeToken<ArmorType>() {
        }.getType())));

        damageTypes = entityManager.create(new TypeToken<List<DamageType>>() {
        }.getType(), "damageTypes");
        armorTypes = entityManager.create(new TypeToken<List<ArmorType>>() {
        }.getType(), "armorTypes");

        Map<String, Class> types = Maps.newHashMap();
        types.put("damage", DirectDamage.class);
        types.put("buff", Buff.class);
        types.put("slowdown", Slowdown.class);
        builder.registerTypeAdapter(Effect.class, new InheritanceAdapter<>(types));

        types = Maps.newHashMap();
        types.put("bullet", Bullet.class);
        types.put("rocket", Rocket.class);
        types.put("laser", Laser.class);
        builder.registerTypeAdapter(Projectile.class, new InheritanceAdapter<>(types));

        types = Maps.newHashMap();
        types.put("circle", Circle.class);
        types.put("rect", Rect.class);
        builder.registerTypeAdapter(Sprite.class, new InheritanceAdapter<>(types));
    }

    public Grid getGrid() {
        return grid;
    }

    public static int rollDice(String dice) {
        String[] parts = dice.toLowerCase().split("d");
        int numberOfDice = Integer.parseInt(parts[0]);
        int diceSides = Integer.parseInt(parts[1]);
        int result = 0;
        for (int i = 0; i < numberOfDice; i++) {
            result += random.nextInt(diceSides) + 1;
        }
        return result;
    }

    public static int rollBullets(int bulletCount, float hitChance) {
        int result = 0;
        for (int i = 0; i < bulletCount; i++) {
            result += random.nextFloat() < hitChance ? 1 : 0;
        }
        return result;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
