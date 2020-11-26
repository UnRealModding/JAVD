package com.unrealdinnerbone.javd;

import com.unrealdinnerbone.javd.data.DataEvent;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(JAVD.MOD_ID)
public class JAVD
{
    public static final String MOD_ID = "javd";

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue PLATFORM_RANGE = builder.comment("The range of how many blocks out to build the platform").defineInRange("platformRange", 3, 1, 10);

    public static final Tags.IOptionalNamedTag<Block> GENERATOR_BLOCKS = BlockTags.createOptional(new ResourceLocation(JAVD.MOD_ID, "generator"));

    public JAVD() {
        JAVDRegistry.REGISTRIES.forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataEvent::onData);
    }

}
