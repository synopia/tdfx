package com.synopia.td.events;

import com.synopia.td.Block;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by synopia on 03.01.2015.
 */
public class BlockEvent extends Event {
    public static final EventType<BlockEvent> BLOCK_EVENT = new EventType<>(ANY, "BLOCK_EVENT");
    public static final EventType<BlockEvent> BLOCK_ENTERED = new EventType<>(BLOCK_EVENT, "ENTERED");
    public static final EventType<BlockEvent> BLOCK_EXITED = new EventType<>(BLOCK_EVENT, "EXITED");
    public static final EventType<BlockEvent> BLOCK_CLICKED = new EventType<>(BLOCK_EVENT, "CLICKED");
    private final Block block;

    public BlockEvent(EventType<? extends Event> eventType, Block block) {
        super(eventType);
        this.block = block;
    }

    public static BlockEvent entered(Block block) {
        return new BlockEvent(BLOCK_ENTERED, block);
    }

    public static BlockEvent exited(Block block) {
        return new BlockEvent(BLOCK_EXITED, block);
    }

    public static BlockEvent clicked(Block block) {
        return new BlockEvent(BLOCK_CLICKED, block);
    }

    public Block getBlock() {
        return block;
    }
}
