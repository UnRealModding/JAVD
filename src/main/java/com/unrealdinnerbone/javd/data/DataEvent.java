package com.unrealdinnerbone.javd.data;

import com.mojang.datafixers.util.Pair;
import com.unrealdinnerbone.javd.JAVD;
import com.unrealdinnerbone.javd.JAVDRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.*;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeLootTableProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class DataEvent {

    public static void onData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new Recipe(event.getGenerator()));
        event.getGenerator().addProvider(true, new BlockState(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new Item(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new LootTable(event.getGenerator()));
        event.getGenerator().addProvider(true, new BlockTag(event.getGenerator(),event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new CodecTypedGenerator<>(event.getGenerator(), JAVD.MOD_ID, ForgeRegistries.Keys.BIOMES, Biome.DIRECT_CODEC));
        event.getGenerator().addProvider(true, new CodecTypedGenerator<>(event.getGenerator(), JAVD.MOD_ID, Registry.DIMENSION_TYPE_REGISTRY, DimensionType.DIRECT_CODEC));
    }

    public static class BlockTag extends BlockTagsProvider {

        public BlockTag(DataGenerator generatorIn, ExistingFileHelper fileHelper) {
            super(generatorIn, JAVD.MOD_ID, fileHelper);
        }

        @Override
        protected void addTags() {
            tag(JAVD.GENERATOR_BLOCKS)
                    .add(Blocks.WHITE_CONCRETE)
                    .add(Blocks.ORANGE_CONCRETE)
                    .add(Blocks.MAGENTA_CONCRETE)
                    .add(Blocks.LIGHT_BLUE_CONCRETE)
                    .add(Blocks.YELLOW_CONCRETE)
                    .add(Blocks.LIME_CONCRETE)
                    .add(Blocks.PINK_CONCRETE)
                    .add(Blocks.GRAY_CONCRETE)
                    .add(Blocks.LIGHT_GRAY_CONCRETE)
                    .add(Blocks.CYAN_CONCRETE)
                    .add(Blocks.PURPLE_CONCRETE)
                    .add(Blocks.BLUE_CONCRETE)
                    .add(Blocks.BROWN_CONCRETE)
                    .add(Blocks.GREEN_CONCRETE)
                    .add(Blocks.RED_CONCRETE)
                    .add(Blocks.BLACK_CONCRETE);

            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(JAVDRegistry.PORTAL_BLOCK.get());
            tag(BlockTags.NEEDS_DIAMOND_TOOL).add(JAVDRegistry.PORTAL_BLOCK.get());
        }

    }

    public static class LootTable extends LootTableProvider {

        public LootTable(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, net.minecraft.world.level.storage.loot.LootTable.Builder>>>, LootContextParamSet>> getTables() {
            return Collections.singletonList(Pair.of(BlockLootTable::new, LootContextParamSets.BLOCK));
        }

        public static class BlockLootTable extends BlockLoot {

            @Override
            public void addTables() {
                dropSelf(JAVDRegistry.PORTAL_BLOCK.get());
            }

            protected Iterable<Block> getKnownBlocks() {
                return Collections.singleton(JAVDRegistry.PORTAL_BLOCK.get());
            }


        }

        @Override
        protected void validate(Map<ResourceLocation, net.minecraft.world.level.storage.loot.LootTable> map, ValidationContext validationtracker) {

        }
    }

    public static class Item extends net.minecraftforge.client.model.generators.ItemModelProvider {

        public Item(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, JAVD.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            ResourceLocation location = ForgeRegistries.ITEMS.getKey(JAVDRegistry.PORTAL_BLOCK_ITEM.get());
            cubeAll(location.getPath(),new ResourceLocation(JAVD.MOD_ID, "block/portal_block"));
        }
    }

    public static class BlockState extends BlockStateProvider {

        public BlockState(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, JAVD.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlock(JAVDRegistry.PORTAL_BLOCK.get());
        }
    }

    public static class Recipe extends RecipeProvider {

        public Recipe(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
            ShapedRecipeBuilder.shaped(JAVDRegistry.PORTAL_BLOCK_ITEM::get)
                    .pattern("OOO")
                    .pattern("OEO")
                    .pattern("OOO")
                    .define('O', Tags.Items.OBSIDIAN)
                    .define('E', Items.ENDER_PEARL)
                    .unlockedBy("has_eye", has(Items.ENDER_PEARL))
                    .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
                    .save(consumer);
        }
    }
}
