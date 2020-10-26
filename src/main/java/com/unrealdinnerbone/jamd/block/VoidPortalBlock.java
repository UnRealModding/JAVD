package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.util.TelerportUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class VoidPortalBlock extends Block {

    public VoidPortalBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            TelerportUtils.toVoid(playerEntity, world.getServer().getWorld(World.field_234918_g_), blockPos, false);
        }
        return ActionResultType.SUCCESS;
    }

}
