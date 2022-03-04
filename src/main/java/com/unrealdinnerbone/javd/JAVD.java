package com.unrealdinnerbone.javd;

import com.unrealdinnerbone.javd.data.DataEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Optional;

@Mod(JAVD.MOD_ID)
public class JAVD
{
    public static final String MOD_ID = "javd";

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue PLATFORM_RANGE = builder.comment("The range of how many blocks out to build the platform").defineInRange("platformRange", 3, 1, 10);
    private static final ForgeConfigSpec.ConfigValue<String> MAIN_WORLD = builder.comment("Main world id").define("main_world", "minecraft:overworld");


    public static final ResourceLocation DIM_ID = new ResourceLocation(MOD_ID, "void");

    public static final TagKey<Block> GENERATOR_BLOCKS = BlockTags.create(new ResourceLocation(JAVD.MOD_ID, "generator"));

    public JAVD() {
        JAVDRegistry.REGISTRIES.forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataEvent::onData);
    }

    public static String getMainWorld() {
        return MAIN_WORLD.get();
    }
    public static Optional<Level> getMainWorld(MinecraftServer server) {
        return Optional.ofNullable(server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MAIN_WORLD.get()))));
    }

    public static Optional<Level> getVoidWorld(MinecraftServer server) {
        return Optional.ofNullable(server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, DIM_ID)));
    }
}
