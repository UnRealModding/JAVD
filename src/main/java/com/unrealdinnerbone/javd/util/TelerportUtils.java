package com.unrealdinnerbone.javd.util;

import com.unrealdinnerbone.javd.JAVD;
import com.unrealdinnerbone.javd.JAVDRegistry;
import com.unrealdinnerbone.javd.block.PortalBlock;
import com.unrealdinnerbone.javd.block.PortalTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class TelerportUtils {


    public static void teleport(Block clickedBlock, Player playerEntity, Level toWorld, BlockPos blockPos, boolean spawnPlatform) throws RuntimeException {
        if(!toWorld.isClientSide() && playerEntity.level instanceof ServerLevel && toWorld instanceof ServerLevel) {
            BlockPos portalLocation = findPortalLocation(toWorld, blockPos).orElseThrow(() -> new RuntimeException("Cant find location to spawn portal"));
            if (toWorld.getBlockState(portalLocation).isAir()) {
                Block block = ListUtil.getRandom(JAVD.GENERATOR_BLOCKS.getValues()).orElse(Blocks.STONE);
                if(spawnPlatform && toWorld.getBlockState(portalLocation).isAir()) {
                    int range = JAVD.PLATFORM_RANGE.get();
                    BlockPos.betweenClosedStream(portalLocation.offset(range, 0, range), portalLocation.offset(-range, 0, -range)).forEach(blockPos1 -> {
                        if (toWorld.getBlockState(blockPos1).isAir()) {
                            toWorld.setBlockAndUpdate(blockPos1, block.defaultBlockState());
                        }
                    });
                }
                toWorld.setBlockAndUpdate(portalLocation, JAVDRegistry.PORTAL_BLOCK.get().defaultBlockState());
            }
            playerEntity.changeDimension((ServerLevel) toWorld, new SimpleTeleporter(portalLocation.getX(), portalLocation.above().getY(), portalLocation.getZ()));
        }
    }


    private static Optional<BlockPos> findPortalLocation(Level worldTo, BlockPos fromPos) {
        if(worldTo.getBlockState(fromPos).getBlock() == JAVDRegistry.PORTAL_BLOCK.get() && isSafeSpawnLocation(worldTo, fromPos)) {
            return Optional.of(fromPos.above());
        }

        int range = 5;
        return Optional.ofNullable(ChunkPos.rangeClosed(worldTo.getChunkAt(fromPos).getPos(), range)
                .map(chunkPos -> worldTo.getChunk(chunkPos.x, chunkPos.z).getBlockEntitiesPos())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()).stream()
                .filter(pos -> worldTo.getBlockEntity(pos) instanceof PortalTileEntity)
                .findFirst()
                .orElseGet(() -> {
                    BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(0, 0, 0);
                    for (int y = worldTo.getMinBuildHeight(); y < worldTo.getMaxBuildHeight(); y++) {
                        for (int x = fromPos.getX() - 6; x < fromPos.getX() + 6; x++) {
                            for (int z = fromPos.getZ() - 6; z < fromPos.getZ() + 6; z++) {
                                mutableBlockPos.set(x, y, z);
                                BlockState blockState = worldTo.getBlockState(mutableBlockPos);
                                if (blockState.isAir() && isSafeSpawnLocation(worldTo, mutableBlockPos.above())) {
                                    return mutableBlockPos;
                                }
                            }
                        }
                    }
                    return null;
                }));

    }


    private static boolean isSafeSpawnLocation(Level world, BlockPos blockPos) {
        return world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.above()).isAir();
    }


}
