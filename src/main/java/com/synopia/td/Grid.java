package com.synopia.td;

import com.google.common.collect.Lists;
import com.synopia.core.AStar;
import com.synopia.core.BitMap;
import com.synopia.engine.EntityFactory;
import com.synopia.engine.Initializable;
import com.synopia.td.events.BlockEvent;
import javafx.scene.Group;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by synopia on 01.01.2015.
 */
public class Grid implements Initializable {
    private List<String> data;
    private Map<String, EntityFactory<Block>> blockDefinitions;

    private transient int width;
    private transient int height;
    private transient List<Block> passableBlocks = Lists.newArrayList();
    private transient List<Block> towerBlocks = Lists.newArrayList();
    private transient List<Block> spawnBlocks = Lists.newArrayList();
    private transient List<Block> goalBlocks = Lists.newArrayList();
    private transient Block[] map;
    private transient BitMap pathfinderMap;
    private transient AStar aStar;
    private transient Executor executor = Executors.newSingleThreadExecutor();
    private transient Tower activeTower;
    private transient BlockSelection hoverArea;
    private transient BlockSelection hoveredBlocks;

    @Override
    public void initialize() {
        height = data.size();
        width = data.get(0).length();

        pathfinderMap = new BitMap(width, height) {
            @Override
            public boolean isPassable(int offset) {
                return map[offset].isPassable();
            }
        };
        map = new Block[pathfinderMap.getNumberOfNodes()];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char ch = data.get(y).charAt(x);
                EntityFactory<Block> blockEntityFactory = blockDefinitions.get(Character.toString(ch));
                if (blockEntityFactory == null) {
                    throw new RuntimeException("Unknown map character " + ch);
                }

                Block block = blockEntityFactory.create();
                block.setPosition(new Position(x, y));
                if (block.isSpawn()) {
                    spawnBlocks.add(block);
                }
                if (block.isGoal()) {
                    goalBlocks.add(block);
                }
                if (block.isTowerPlaceable()) {
                    towerBlocks.add(block);
                }
                if (block.isPassable()) {
                    passableBlocks.add(block);
                }
                map[pathfinderMap.offset(x, y)] = block;
            }
        }
        aStar = new AStar(pathfinderMap);
    }

    public void registerListeners(Group node) {
        node.addEventHandler(BlockEvent.BLOCK_ENTERED, event -> {
            if (activeTower != null) {
                hoveredBlocks = activeTower.getGroundBlocks(Grid.this, event.getBlock().getPosition());
                if (hoveredBlocks.allTowerPlaceable()) {
                    for (Block block : hoveredBlocks) {
                        block.setHovered(true);
                    }
                }
            }
        });
        node.addEventHandler(BlockEvent.BLOCK_EXITED, event -> unhover());
        node.addEventHandler(BlockEvent.BLOCK_CLICKED, event -> {
            if (activeTower != null && hoveredBlocks != null && hoveredBlocks.allTowerPlaceable()) {
                for (Block block : hoveredBlocks) {
                    block.setTower(activeTower);
                }
                unhover();

                activeTower.setPosition(event.getBlock().getPosition());
//                activeTower = null;
            }
        });
    }

    private void unhover() {
        if (hoveredBlocks != null) {
            for (Block block : hoveredBlocks) {
                block.setHovered(false);
            }
            hoveredBlocks = null;
        }
    }

    protected Path findPath(Position start, Position goal) {
        aStar.reset();
        boolean pathFound = aStar.run(pathfinderMap.offset(start.x(), start.y()), pathfinderMap.offset(goal.x(), goal.y()));
        if (pathFound) {
            List<Position> path = Lists.newArrayList();
            for (Integer offset : aStar.getPath()) {
                path.add(new Position(pathfinderMap.getX(offset), pathfinderMap.getY(offset)));
            }
            return new Path(path);
        }
        return null;
    }

    public Future<Path> findPathToGoal(Position position) {
        Block block = goalBlocks.get(0);
        FutureTask<Path> task = new FutureTask<>(() -> findPath(block.getPosition(), position));
        executor.execute(task);
        return task;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Block getBlock(float x, float y) {
        return getBlock((int) (x), (int) (y));
    }

    public Block getBlock(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            return map[pathfinderMap.offset(x, y)];
        }
        return null;
    }

    public void setActiveTower(Tower activeTower) {
        this.activeTower = activeTower;
        unhover();
    }

    public List<Block> getSpawnBlocks() {
        return spawnBlocks;
    }

    public List<Block> getGoalBlocks() {
        return goalBlocks;
    }
}
