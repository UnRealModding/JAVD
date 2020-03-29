package com.unrealdinnerbone.jamd.dim;

import com.unrealdinnerbone.jamd.JAVD;
import com.unrealdinnerbone.jamd.JAVDRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;

import javax.annotation.Nullable;

public class VoidDim extends Dimension {

    public VoidDim(World world, DimensionType dimensionType) {
        super(world, dimensionType, 0);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        GenerationSettings genSettings = JAVDRegistry.VOID_CHUNK.createSettings();
        genSettings.setDefaultBlock(JAVDRegistry.PORTAL_BLOCK.getDefaultState());
        return JAVDRegistry.VOID_CHUNK.create(this.world, BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.func_226840_a_(this.world.getWorldInfo()).setBiome(JAVDRegistry.THE_VOID)), genSettings);
    }

    @Nullable
    @Override
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
        return null;
    }

    @Nullable
    @Override
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
        return null;
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0;
    }

    @Override
    public boolean isDaytime() {
        return true;
    }

    @Override
    public long getWorldTime() {
        return 6000;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }


    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return new Vec3d(0.75, 0.84, 1.0);
    }

    @Override
    public boolean canRespawnHere() {
        return JAVD.ALLOW_RESPAWN.get();
    }

    @Override
    public int getMoonPhase(long worldTime) {
        return 0;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    @Override
    public boolean hasSkyLight() {
        return JAVD.ALLOW_SKYLIGHT.get();
    }
}
