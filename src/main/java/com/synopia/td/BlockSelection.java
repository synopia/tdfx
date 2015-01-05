package com.synopia.td;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by synopia on 03.01.2015.
 */
public class BlockSelection implements Iterable<Block> {
    public static BlockSelection INVALID = new BlockSelection();

    private List<Block> blocks = Lists.newArrayList();


    public void add(Block block) {
        blocks.add(block);
    }

    public void remove(Block block) {
        blocks.remove(block);
    }

    @Override
    public Iterator<Block> iterator() {
        return blocks.iterator();
    }

    @Override
    public void forEach(Consumer<? super Block> action) {
        blocks.forEach(action);
    }

    @Override
    public Spliterator<Block> spliterator() {
        return blocks.spliterator();
    }

    public static BlockSelection fromRectangle(Grid grid, int sx, int sy, int width, int height) {
        BlockSelection selection = new BlockSelection();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Block b = grid.getBlock(x + sx, y + sy);
                if (b == null) {
                    return INVALID;
                }
                selection.add(b);
            }
        }
        return selection;
    }

    public boolean allTowerPlaceable() {
        if (blocks.size() == 0) {
            return false;
        }
        for (Block block : blocks) {
            if (!block.isTowerPlaceable()) {
                return false;
            }
        }
        return true;
    }
}
