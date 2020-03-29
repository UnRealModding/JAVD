package com.unrealdinnerbone.jamd.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class EasterEggs
{
    private static final HashMap<UUID, Block> EASTER_EGGS = new HashMap<>();

    static {
        EASTER_EGGS.put(UUID.fromString("ae9c317a-cf2e-43c5-9b32-37a6ae83879f"), Blocks.DIAMOND_BLOCK);
        EASTER_EGGS.put(UUID.fromString("d2839efc-727a-4263-97ce-3c73cdee5013"), Blocks.CLAY);
    }


    public static Optional<Block> getPlayersBlock(UUID uuid) {
        return Optional.ofNullable(EASTER_EGGS.getOrDefault(uuid, null));
    }
}
