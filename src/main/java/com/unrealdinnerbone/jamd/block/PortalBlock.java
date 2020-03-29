package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.util.TelerportUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PortalBlock extends Block {

    public PortalBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0f));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof PortalTitleEnity) {
                PortalTitleEnity portalTitleEnity = (PortalTitleEnity) tileEntity;
                if (playerEntity.getHeldItem(hand).getItem() == Items.ENDER_PEARL) {
                    if (portalTitleEnity.getLinked() != playerEntity.getUniqueID()) {
                        portalTitleEnity.setLinked(playerEntity.getUniqueID());
                        playerEntity.sendStatusMessage(new TranslationTextComponent("block.portal.linked", playerEntity.getDisplayName()), true);
                        return ActionResultType.CONSUME;
                    }
                } else {
                    if (portalTitleEnity.getLinked().equals(PortalTitleEnity.FAKE_ID)) {
                        playerEntity.sendStatusMessage(new TranslationTextComponent("block.portal.noLink").appendSibling(new TranslationTextComponent(Items.ENDER_PEARL.getTranslationKey())), true);
                    } else {
                        TelerportUtils.toVoid(playerEntity, portalTitleEnity.getLinked());
                    }
                }
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
