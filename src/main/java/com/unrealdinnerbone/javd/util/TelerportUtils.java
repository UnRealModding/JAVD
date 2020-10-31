package com.unrealdinnerbone.javd.util;

import com.unrealdinnerbone.javd.JAVD;
import com.unrealdinnerbone.javd.JAVDRegistry;
import com.unrealdinnerbone.javd.block.PortalBlock;
import com.unrealdinnerbone.javd.block.PortalTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.stream.Collectors;

public class TelerportUtils {

    public static void teleportEntity(Entity entity, ServerWorld world, double x, double y, double z) {
        SimpleTeleporter simpleTeleporter = new SimpleTeleporter(x + 0.5, y, z + 0.5);
        entity.changeDimension(world, simpleTeleporter);
    }

    public static void teleport(PlayerEntity playerEntity, World toWorld, BlockPos blockPos, boolean spawnPlatform) throws RuntimeException {
        if(!toWorld.isRemote() && playerEntity.world instanceof ServerWorld && toWorld instanceof ServerWorld) {
            BlockPos portalLocation = findPortalLocation(playerEntity.world.getDimensionKey(), playerEntity.getEntityWorld(), blockPos).orElseThrow(() -> new RuntimeException("Cant find location to spawn portal"));
            if (toWorld.getBlockState(portalLocation).isAir()) {
                Block block = getBlock(playerEntity.getUniqueID());
                if(spawnPlatform && toWorld.getBlockState(portalLocation).isAir()) {
                    int range = JAVD.PLATFORM_RANGE.get();
                    BlockPos.getAllInBox(portalLocation.add(range, 0, range), portalLocation.add(-range, 0, -range)).forEach(blockPos1 -> {
                        if (toWorld.getBlockState(blockPos1).isAir()) {
                            toWorld.setBlockState(blockPos1, block.getDefaultState());
                        }
                    });
                }
                PortalBlock.placeBlock(toWorld, portalLocation, playerEntity.world.getDimensionKey());
            }
            teleportEntity(playerEntity, (ServerWorld) toWorld, portalLocation.getX(), portalLocation.up().getY(), portalLocation.getZ());
        }
    }


    private static Optional<BlockPos> findPortalLocation(RegistryKey<World> worldFrom, World worldTo, BlockPos fromPos) {
        if(worldTo.getBlockState(fromPos).getBlock() == JAVDRegistry.PORTAL_BLOCK.get() && isSafeSpawnLocation(worldTo, fromPos)) {
            return Optional.of(fromPos.up());
        }

        int range = 5;
        Chunk centerChunk = worldTo.getChunkAt(fromPos);
        List<Set<BlockPos>> blockPos = ChunkPos.getAllInBox(centerChunk.getPos(), range).map(chunkPos -> worldTo.getChunk(chunkPos.x, chunkPos.z).getTileEntitiesPos()).collect(Collectors.toList());

        for (Set<BlockPos> blockPo : blockPos) {
            for (BlockPos pos : blockPo) {
                TileEntity tileEntity = worldTo.getTileEntity(pos);
                if(tileEntity instanceof PortalTileEntity) {
                    return Optional.of(pos);
                }
            }
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

    public static Optional<BlockPos> findPortalInChunk(RegistryKey<World> worldFrom, World worldTo, Chunk chunk) {
        for (BlockPos tileEntitiesPo : chunk.getTileEntitiesPos()) {
            TileEntity tileEntity = worldTo.getTileEntity(tileEntitiesPo);
            if(tileEntity instanceof PortalTileEntity) {
                PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
                if(portalTileEntity.getWorldId().equals(worldFrom.getLocation())) {
                    return Optional.of(tileEntitiesPo);
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isSafeSpawnLocation(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.up()).isAir();
    }

    private static Block getBlock(UUID uuid) {
        return getRandom(JAVD.GENERATOR_BLOCKS.getAllElements()).orElse(Blocks.STONE);
    }


    private static <E> Optional<E> getRandom(Collection<E> e) {
        return e.stream().skip((int) (e.size() * Math.random())).findFirst();
    }

}
