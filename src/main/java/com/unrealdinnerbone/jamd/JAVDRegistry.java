package com.unrealdinnerbone.jamd;

import com.unrealdinnerbone.jamd.block.PortalBlock;
import com.unrealdinnerbone.jamd.block.PortalTitleEnity;
import com.unrealdinnerbone.jamd.block.VoidPortalBlock;
import com.unrealdinnerbone.jamd.dim.ChunkGen;
import com.unrealdinnerbone.jamd.dim.VoidBiome;
import com.unrealdinnerbone.jamd.dim.VoidDim;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class JAVDRegistry {


    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, JAVD.MOD_ID);
    private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, JAVD.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, JAVD.MOD_ID);
    private static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, JAVD.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, JAVD.MOD_ID);
    private static final DeferredRegister<ChunkGeneratorType<?, ?>> CHUNKS_GEN = new DeferredRegister<>(ForgeRegistries.CHUNK_GENERATOR_TYPES, JAVD.MOD_ID);


    public static final List<DeferredRegister<?>> REGISTRIES = Arrays.asList(BLOCKS, DIMENSIONS, TILE_ENTITY, BIOMES, ITEMS, CHUNKS_GEN);

    public static final RegistryObject<PortalBlock> PORTAL_BLOCK = BLOCKS.register("portal_block", PortalBlock::new);

    public static final RegistryObject<Item> PORTAL_BLOCK_ITEM = ITEMS.register("portal_block", () -> new BlockItem(PORTAL_BLOCK.get(), new Item.Properties().group(ItemGroup.TRANSPORTATION)));

    public static final RegistryObject<VoidPortalBlock> VOID_PORTAL_BLOCK = BLOCKS.register("void_portal_block", VoidPortalBlock::new);

    public static final RegistryObject<ModDimension> VOID = DIMENSIONS.register("void", () -> ModDimension.withFactory(VoidDim::new));

    public static final RegistryObject<ChunkGeneratorType<GenerationSettings, ChunkGen>> VOID_CHUNK = CHUNKS_GEN.register("void_chunk", () -> new ChunkGeneratorType<>(ChunkGen::new, false, GenerationSettings::new));

    public static final RegistryObject<TileEntityType<? extends PortalTitleEnity>> PORTAL = TILE_ENTITY.register("portal", () -> TileEntityType.Builder.create(PortalTitleEnity::new, JAVDRegistry.PORTAL_BLOCK.get()).build(null));

    public static final RegistryObject<VoidBiome> THE_VOID = BIOMES.register("the_void", VoidBiome::new);


}
