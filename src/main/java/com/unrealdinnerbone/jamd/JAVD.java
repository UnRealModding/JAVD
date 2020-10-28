package com.unrealdinnerbone.jamd;

import com.unrealdinnerbone.jamd.block.PortalBlock;
import com.unrealdinnerbone.jamd.block.PortalTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
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

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DISABLE_EASTER_EGGS = builder.comment("Disable easter eggs").define("disableEasterEggs", false);
    public static final ForgeConfigSpec.IntValue PLATFORM_RANGE = builder.comment("The range of how many blocks out to build the platform").defineInRange("platformRange", 3, 1, 10);

    public static final RegistryKey<World> VOID_DIMENSION = RegistryKey.func_240903_a_(Registry.WORLD_KEY, new ResourceLocation(JAVD.MOD_ID, "void"));
    public static final Tags.IOptionalNamedTag<Block> GENERATOR_BLOCKS = BlockTags.createOptional(new ResourceLocation(JAVD.MOD_ID, "generator"));

    public static final String MOD_ID = "javd";

    public JAVD() {
        JAVDRegistry.REGISTRIES.forEach(deferredRegister -> deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        MinecraftForge.EVENT_BUS.addListener(JAVD::onWrenched);
    }

    public static void onWrenched(PlayerInteractEvent.RightClickBlock event) {
        try {
            if (!event.getWorld().isRemote()) {
                if (!event.getItemStack().isEmpty()) {
                    if (event.getItemStack().getItem() == Items.NAME_TAG) {
                        if (event.getPlayer().isSneaking()) {
                            BlockPos blockPos = event.getPos();
                            World world = event.getWorld();
                            TileEntity tileEntity = world.getTileEntity(blockPos);
                            if(tileEntity instanceof PortalTileEntity) {
                                PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
                                ItemStack itemStack = event.getPlayer().getHeldItem(event.getHand());
                                if (itemStack.getItem() == Items.NAME_TAG) {
                                    ITextComponent displayName = itemStack.getDisplayName();
                                    String name = displayName.getString();
                                    System.out.println(name);
                                    World theyWorld = PortalBlock.getWorldFromName(world, name).orElseThrow(() -> new RuntimeException("Name not vaild world"));
                                    ResourceLocation resourceLocation = theyWorld.func_234923_W_().func_240901_a_();
                                    portalTileEntity.setWorldId(resourceLocation);
                                }
                            }else {
                                throw new RuntimeException("Error tile not there?");
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            event.getPlayer().sendStatusMessage(new StringTextComponent(e.getMessage()), false);
        }
    }

}
