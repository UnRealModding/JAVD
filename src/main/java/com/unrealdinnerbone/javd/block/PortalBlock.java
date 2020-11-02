package com.unrealdinnerbone.javd.block;

import com.unrealdinnerbone.javd.JAVDRegistry;
import com.unrealdinnerbone.javd.util.ListUtil;
import com.unrealdinnerbone.javd.util.TelerportUtils;
import com.unrealdinnerbone.javd.util.WorldUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Optional;

public class PortalBlock extends Block {

    public PortalBlock() {
        super(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            try {
                TelerportUtils.teleport(playerEntity, getWorldFromTileEntity(world, blockPos).orElseThrow(() -> new RuntimeException("Invalid world ID set")), blockPos, true);
            } catch (Exception e) {
                playerEntity.sendStatusMessage(new StringTextComponent(e.getMessage()), false);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if(!worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof PortalTileEntity) {
                PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
                if(portalTileEntity.getWorldId() != null) {
                    if(!worldIn.getDimensionKey().equals(World.OVERWORLD)) {
                        portalTileEntity.setWorldId(World.OVERWORLD.getLocation());
                    }else {
                        ListUtil.getFirstValue(WorldUtils.WORLDS.get()).ifPresent(worldId -> portalTileEntity.setWorldId(new ResourceLocation(worldId)));
                    }
                    portalTileEntity.markDirty();
                }
            }
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    public static void placeBlock(World world, BlockPos blockPos, RegistryKey<World> portalTo) {
        world.setBlockState(blockPos, JAVDRegistry.PORTAL_BLOCK.get().getDefaultState());
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if(tileEntity instanceof PortalTileEntity) {
            ((PortalTileEntity) tileEntity).setWorldId(portalTo.getLocation());
            tileEntity.markDirty();
        }
    }

    public static Optional<World> getWorldFromTileEntity(World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof PortalTileEntity) {
            PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
            if (portalTileEntity.getWorldId() != null) {
                return Optional.ofNullable(world.getServer().getWorld(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, portalTileEntity.getWorldId())));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PortalTileEntity();
    }
}
