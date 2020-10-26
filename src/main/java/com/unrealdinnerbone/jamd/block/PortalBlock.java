package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAVD;
import com.unrealdinnerbone.jamd.util.TelerportUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class PortalBlock extends Block {

    public PortalBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0f));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            try {
                TelerportUtils.toVoid(playerEntity, world.getServer().getWorld(JAVD.VOID_DIMENSION), blockPos, true);
            }catch (RuntimeException e) {
                playerEntity.sendStatusMessage(new StringTextComponent("Cant find spot to place portal, move portal block and try again"), false);
            }
        }
        return ActionResultType.SUCCESS;
    }

}
