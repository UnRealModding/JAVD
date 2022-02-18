package com.unrealdinnerbone.javd.block;

import com.unrealdinnerbone.javd.JAVD;
import com.unrealdinnerbone.javd.JAVDRegistry;
import com.unrealdinnerbone.javd.util.TelerportUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Optional;

public class PortalBlock extends Block implements EntityBlock {

    public PortalBlock() {
        super(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            try {
                if(level.dimension().location().equals(JAVD.DIM_ID)) {
                    TelerportUtils.teleport(this, player, JAVD.getMainWorld(level.getServer()).orElseThrow(() -> new RuntimeException("Can't find world with id " + JAVD.getMainWorld())), pos, false);
                }else {
                    TelerportUtils.teleport(this, player, JAVD.getVoidWorld(level.getServer()).orElseThrow(() -> new RuntimeException("Error getting mining dimension")), pos, true);
                }
            } catch (Exception e) {
                player.displayClientMessage(new TextComponent(e.getMessage()), false);
            }
            return InteractionResult.PASS;
        }else {
            return InteractionResult.FAIL;
        }
    }



    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return JAVDRegistry.PORTAL.get().create(pos, state);
    }
}
