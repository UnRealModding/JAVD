package com.unrealdinnerbone.jamd.util;

import com.unrealdinnerbone.jamd.JAVD;
import com.unrealdinnerbone.jamd.JAVDRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class TelerportUtils {

    public static void teleportEntity(Entity entity, ServerWorld world, double x, double y, double z) {
        SimpleTeleporter simpleTeleporter = new SimpleTeleporter(x + 0.5, y, z + 0.5);
        entity.changeDimension(world, simpleTeleporter);
    }

    public static void toVoid(PlayerEntity playerEntity, World toWorld, BlockPos blockPos, boolean spawnPlatform) throws RuntimeException{
        if(!toWorld.isRemote() && playerEntity.world instanceof ServerWorld && toWorld instanceof ServerWorld) {
            BlockPos spawnPos = findPortalLocation(playerEntity.getEntityWorld(), blockPos).orElseThrow(() -> new RuntimeException("Cant find location to spawn portal"));
            if (playerEntity.world.getBlockState(spawnPos).isAir()) {
                Block block = getBlock(playerEntity.getUniqueID());
                toWorld.setBlockState(spawnPos, JAVDRegistry.VOID_PORTAL_BLOCK.get().getDefaultState());
                if(spawnPlatform) {
                    int range = JAVD.PLATFORM_RANGE.get();
                    BlockPos.getAllInBox(spawnPos.add(range, 0, range), spawnPos.add(-range, 0, -range)).forEach(blockPos1 -> {
                        if (toWorld.getBlockState(blockPos1).isAir()) {
                            toWorld.setBlockState(blockPos1, block.getDefaultState());
                        }
                    });
                }
            }
            teleportEntity(playerEntity, (ServerWorld) toWorld, spawnPos.getX(), spawnPos.up().getY(), spawnPos.getZ());
        }
    }


    private static Optional<BlockPos> findPortalLocation(World worldTo, BlockPos fromPos) {
        if(isSafeSpawnLocation(worldTo, fromPos)) {
            return Optional.of(fromPos);
        }
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(0, 0, 0);
        for (int y = 0; y < 256; y++) {
            for (int x = fromPos.getX() - 6; x < fromPos.getX() + 6; x++) {
                for (int z = fromPos.getZ() - 6; z < fromPos.getZ() + 6; z++) {
                    mutableBlockPos.setPos(x, y, z);
                    BlockState blockState = worldTo.getBlockState(mutableBlockPos);
                    if (blockState.isAir() && isSafeSpawnLocation(worldTo, mutableBlockPos.up())) {
                        return Optional.of(mutableBlockPos);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isSafeSpawnLocation(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.up()).isAir();
    }

    private static Block getBlock(UUID uuid) {
        return EasterEggs.getPlayersBlock(uuid).orElse(getRandom(JAVD.GENERATOR_BLOCKS.getAllElements()).orElse(Blocks.STONE));
    }


    private static <E> Optional<E> getRandom(Collection<E> e) {
        return e.stream().skip((int) (e.size() * Math.random())).findFirst();
    }

}
