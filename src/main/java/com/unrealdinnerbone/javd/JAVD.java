package com.unrealdinnerbone.javd;

import com.unrealdinnerbone.javd.block.PortalTileEntity;
import com.unrealdinnerbone.javd.util.ListUtil;
import com.unrealdinnerbone.javd.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
        MinecraftForge.EVENT_BUS.addListener(JAVD::onWrenched);
    }

    public static void onWrenched(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getWorld().isRemote()) {
            if (event.getPlayer().isSneaking()) {
                BlockPos blockPos = event.getPos();
                World world = event.getWorld();
                TileEntity tileEntity = world.getTileEntity(blockPos);
                if (tileEntity instanceof PortalTileEntity) {
                    PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
                    ListUtil.next(WorldUtils.WORLDS.get(), portalTileEntity.getWorldId().toString()).ifPresent(s -> {
                        portalTileEntity.setWorldId(new ResourceLocation(s));
                        portalTileEntity.markDirty();
                        event.getPlayer().sendStatusMessage(new StringTextComponent("Set portal id " + s), true);
                        event.setCanceled(true);
                    });
                } else {
                    throw new RuntimeException("Error tile not there?");
                }
            }
        }
    }

}
