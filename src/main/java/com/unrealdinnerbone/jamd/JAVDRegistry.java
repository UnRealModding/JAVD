package com.unrealdinnerbone.jamd;

import com.unrealdinnerbone.jamd.JAVD;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(JAVD.MOD_ID)
public class JAVDRegistry {

    public static final Block PORTAL_BLOCK = Blocks.AIR;
    public static final Block VOID_PORTAL_BLOCK = Blocks.AIR;

    public static final ModDimension VOID = null;

    public static final ChunkGeneratorType<GenerationSettings, ?> VOID_CHUNK = null;

    public static final TileEntityType<? extends TileEntity> PORTAL = TileEntityType.CHEST;

    public static final Biome THE_VOID = Biomes.OCEAN;


}
