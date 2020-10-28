package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAVD;
import com.unrealdinnerbone.jamd.util.TelerportUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Optional;

public class PortalBlock extends Block {

    private final ResourceLocation portalTo;
    private final boolean spawnPlatform;

    public PortalBlock(AbstractBlock.Properties properties, ResourceLocation portalTo, boolean spawnPlatform) {
        super(properties);
        this.portalTo = portalTo;
        this.spawnPlatform = spawnPlatform;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            try {
                if (playerEntity.isSneaking()) {
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if(tileEntity instanceof PortalTileEntity) {
                        PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
                        ItemStack itemStack = playerEntity.getHeldItem(hand);
                        if (itemStack.getItem() == Items.NAME_TAG) {
                            ITextComponent displayName = itemStack.getItem().getDisplayName(itemStack);
                            String name = displayName.getString();
                            World theyWorld = getWorldFromName(world, name).orElseThrow(() -> new RuntimeException("Name not vaild world"));
                            portalTileEntity.setWorldId(new ResourceLocation(name));
                        }
                    }else {
                        throw new RuntimeException("Error tile not there?");
                    }
                } else {
                    TelerportUtils.toVoid(playerEntity, getWorldFromTileEntity(world, blockPos).orElseThrow(() -> new RuntimeException("Invalid world ID set")), blockPos, spawnPlatform);
                }
            } catch (Exception e) {
                playerEntity.sendStatusMessage(new StringTextComponent(e.getMessage()), false);
            }
        }
        return ActionResultType.SUCCESS;
    }

    public static Optional<World> getWorldFromTileEntity(World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof PortalTileEntity) {
            PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
            if (portalTileEntity.getWorldId() != null) {
                return Optional.ofNullable(world.getServer().getWorld(RegistryKey.func_240903_a_(Registry.WORLD_KEY, portalTileEntity.getWorldId())));
            }
        }
        return Optional.empty();
    }

    public static Optional<World> getWorldFromName(World world, String name) {
        return Optional.ofNullable(world.getServer().getWorld(RegistryKey.func_240903_a_(Registry.WORLD_KEY, new ResourceLocation(JAVD.MOD_ID, name))));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        PortalTileEntity portalTileEntity = new PortalTileEntity();
        portalTileEntity.setWorldId(portalTo);
        return portalTileEntity;
    }
}
