package com.unrealdinnerbone.jamd.util;

import com.unrealdinnerbone.jamd.JAVD;
import com.unrealdinnerbone.jamd.JAVDRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class TelerportUtils {

    public static void teleportEntity(Entity entity, DimensionType type, BlockPos blockPos) {
        teleportEntity(entity, type, blockPos.getX() + 0.5, blockPos.getY() + 0.75, blockPos.getZ() + 0.5);
    }

    public static void teleportEntity(Entity entity, DimensionType type, double x, double y, double z) {
        SimpleTeleporter simpleTeleporter = new SimpleTeleporter(x, y, z);
        entity.changeDimension(type, simpleTeleporter);
    }

    public static void toOverworld(PlayerEntity playerEntity) {
        BlockPos blockPos1 = playerEntity.getBedLocation(DimensionType.OVERWORLD);
        if(blockPos1 == null) {
            blockPos1 = playerEntity.getServer().getWorld(DimensionType.OVERWORLD).getSpawnPoint();
        }
        teleportEntity(playerEntity, DimensionType.OVERWORLD, blockPos1);
    }

    public static void toVoid(PlayerEntity playerEntity) {
        toVoid(playerEntity, playerEntity.getUniqueID());

    }
    public static void toVoid(PlayerEntity playerEntity, UUID uuid) {
        if(playerEntity.world instanceof ServerWorld) {
            DimensionType TYPE = JAVD.PLAYER_VOIDS.get() ? JAVD.PLAYER_TYPE.apply(uuid) : JAVD.VOID_TYPE.get();
            ServerWorld serverWorld = (ServerWorld) playerEntity.world;
            World voidWorld = getVoidWorld(playerEntity.getServer(), TYPE);
            BlockPos theBlockPos = LocationSaveData.get(serverWorld).findPortalLocationForPlayer(uuid);
            BlockPos blockPos = theBlockPos.down();
            BlockState portalBlockState = voidWorld.getBlockState(blockPos);
            if (portalBlockState.isAir()) {
                Block block = getBlock(uuid);
                voidWorld.setBlockState(blockPos, JAVDRegistry.VOID_PORTAL_BLOCK.getDefaultState());
                int range = JAVD.PLATFORM_RANGE.get();
                BlockPos.getAllInBox(blockPos.add(range, 0, range), blockPos.add(-range, 0, -range)).forEach(blockPos1 -> {
                    if (voidWorld.getBlockState(blockPos1).isAir(voidWorld, blockPos1)) {
                        voidWorld.setBlockState(blockPos1, block.getDefaultState());
                    }
                });
            }
            teleportEntity(playerEntity, TYPE, theBlockPos);

        }
    }

    private static Block getBlock(UUID uuid) {
        return uuid.toString().equals("ae9c317a-cf2e-43c5-9b32-37a6ae83879f") ? Blocks.DIAMOND_BLOCK : getRandom(JAVD.GENERATOR_BLOCKS.getAllElements()).orElse(Blocks.STONE);
    }


    private static World getVoidWorld(MinecraftServer minecraftServer, DimensionType TYPE) {
        return minecraftServer.getWorld(TYPE);
    }

    private static <E> Optional<E> getRandom(Collection<E> e) {
        return e.stream().skip((int) (e.size() * Math.random())).findFirst();
    }

}
