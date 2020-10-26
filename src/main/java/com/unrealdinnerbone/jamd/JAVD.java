package com.unrealdinnerbone.jamd;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(JAVD.MOD_ID)
public class JAVD
{

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DISABLE_EASTER_EGGS = builder.comment("Disable easter eggs").define("disableEasterEggs", false);
    public static final ForgeConfigSpec.IntValue PLATFORM_RANGE = builder.comment("The range of how many blocks out to build the platform").defineInRange("platformRange", 3, 1, 10);

    public static final RegistryKey<World> VOID_DIMENSION = RegistryKey.func_240903_a_(Registry.WORLD_KEY, new ResourceLocation(JAVD.MOD_ID, "void"));
    public static final Tags.IOptionalNamedTag<Block> GENERATOR_BLOCKS = BlockTags.createOptional(new ResourceLocation(JAVD.MOD_ID, "generator"));

    public static final String MOD_ID = "javd";

    public JAVD() {
        JAVDRegistry.REGISTRIES.forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }

}
