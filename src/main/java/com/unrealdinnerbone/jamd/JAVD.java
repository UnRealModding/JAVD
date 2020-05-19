package com.unrealdinnerbone.jamd;

import com.unrealdinnerbone.jamd.util.LocationSaveData;
import com.unrealdinnerbone.jamd.util.PlayerSaveData;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(JAVD.MOD_ID)
public class JAVD
{

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue ALLOW_RESPAWN = builder.comment("Allow respawning in the void dimension ").define("allowRespawn", false);

    public static final ForgeConfigSpec.BooleanValue PLAYER_VOIDS = builder.comment("Enabled per player void dimensions").define("perPlayerDim", false);
    public static final ForgeConfigSpec.BooleanValue DISABLE_EASTER_EGGS = builder.comment("Disable easter eggs").define("disableEasterEggs", false);
    public static final ForgeConfigSpec.IntValue PLATFORM_RANGE = builder.comment("The range of how many blocks out to build the platform").defineInRange("platformRange", 3, 1, 10);

    private static final Supplier<DimensionType> VOID_TYPE = () -> DimensionManager.registerOrGetDimension(new ResourceLocation(JAVD.MOD_ID, "void"), JAVDRegistry.VOID.get(), null, true);
    private static final Function<UUID, DimensionType> PLAYER_TYPE = uuid -> DimensionManager.registerOrGetDimension(new ResourceLocation(JAVD.MOD_ID, uuid.toString().replace("-", "")), JAVDRegistry.VOID.get(), null, true);
    public static final Function<UUID, DimensionType> TYPE = uuid -> PLAYER_VOIDS.get() ? PLAYER_TYPE.apply(uuid) : VOID_TYPE.get();

    public static final Tag<Block> GENERATOR_BLOCKS = new BlockTags.Wrapper(new ResourceLocation(JAVD.MOD_ID, "generator"));

    public static final String MOD_ID = "javd";
    private static final Logger LOGGER = LogManager.getLogger();

    public JAVD() {
        JAVDRegistry.REGISTRIES.forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }

}
