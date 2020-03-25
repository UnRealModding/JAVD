package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAVDRegistry;
import com.unrealdinnerbone.jamd.util.TelerportUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class VoidPortalBlock extends Block {

    public VoidPortalBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            if (playerEntity.dimension.getModType() == JAVDRegistry.VOID) {
                TelerportUtils.toOverworld(playerEntity);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean isAir(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PortalTitleEnity();
    }
}
