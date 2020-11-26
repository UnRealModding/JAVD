package com.unrealdinnerbone.javd.data;

import com.mojang.datafixers.util.Pair;
import com.unrealdinnerbone.javd.JAVD;
import com.unrealdinnerbone.javd.JAVDRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeLootTableProvider;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class DataEvent {

    public static void onData(GatherDataEvent event) {
        event.getGenerator().addProvider(new Recipe(event.getGenerator()));
        event.getGenerator().addProvider(new BlockState(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(new Item(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(new LootTable(event.getGenerator()));
        event.getGenerator().addProvider(new BlockTag(event.getGenerator(),event.getExistingFileHelper()));
    }

    public static class BlockTag extends BlockTagsProvider {

        public BlockTag(DataGenerator generatorIn, ExistingFileHelper fileHelper) {
            super(generatorIn, JAVD.MOD_ID, fileHelper);
        }

        @Override
        protected void registerTags() {
            getOrCreateBuilder(JAVD.GENERATOR_BLOCKS)
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

        }
    }

    public static class LootTable extends ForgeLootTableProvider {

        public LootTable(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, net.minecraft.loot.LootTable.Builder>>>, LootParameterSet>> getTables() {
            return Collections.singletonList(Pair.of(BlockLootTable::new, LootParameterSets.BLOCK));
        }

        public static class BlockLootTable extends BlockLootTables {

            @Override
            public void addTables() {
                registerDropSelfLootTable(JAVDRegistry.PORTAL_BLOCK.get());
            }

            protected Iterable<Block> getKnownBlocks() {
                List<Block> blocks = new ArrayList<>();
                blocks.add(JAVDRegistry.PORTAL_BLOCK.get());
                return blocks;
            }


        }
    }

    public static class Item extends net.minecraftforge.client.model.generators.ItemModelProvider {

        public Item(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, JAVD.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            itemGenerated(JAVDRegistry.PORTAL_BLOCK_ITEM.get(), new ResourceLocation(JAVD.MOD_ID, "block/portal_block"));
        }

        public void itemGenerated(net.minecraft.item.Item item, ResourceLocation texture) {
            getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", texture);
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
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            ShapedRecipeBuilder.shapedRecipe(JAVDRegistry.PORTAL_BLOCK_ITEM::get)
                    .patternLine("OOO")
                    .patternLine("OEO")
                    .patternLine("OOO")
                    .key('O', Tags.Items.OBSIDIAN)
                    .key('E', Items.ENDER_PEARL)
                    .addCriterion("has_eye", hasItem(Items.ENDER_PEARL))
                    .addCriterion("has_obsidian", hasItem(Items.OBSIDIAN))
                    .build(consumer);
        }
    }
}
