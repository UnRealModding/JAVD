package com.unrealdinnerbone.javd.block;

import com.unrealdinnerbone.javd.JAVD;
import com.unrealdinnerbone.javd.util.TelerportUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
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
import net.minecraftforge.common.ToolType;

import java.util.Optional;

public class PortalBlock extends Block {

    private static final ResourceLocation ID = new ResourceLocation(JAVD.MOD_ID, "void");

    public PortalBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE));
    }


    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isClientSide()) {
            try {
                TelerportUtils.teleport(this, playerEntity, getWorldFromTileEntity(world, blockPos).orElseThrow(() -> new RuntimeException("Invalid world ID set")), blockPos, true);
            } catch (Exception e) {
                playerEntity.displayClientMessage(new StringTextComponent(e.getMessage()), false);
            }
            return ActionResultType.SUCCESS;
        }else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if(!worldIn.isClientSide()) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(tileEntity instanceof PortalTileEntity) {
                PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
                if(portalTileEntity.getWorldId() != null) {
                    if(!worldIn.dimension().equals(World.OVERWORLD)) {
                        portalTileEntity.setWorldId(World.OVERWORLD.location());
                    }else {
                        portalTileEntity.setWorldId(ID);
                    }
                    portalTileEntity.setChanged();
                }
            }
        }
        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    public static void placeBlock(Block block, World world, BlockPos blockPos, RegistryKey<World> portalTo) {
        world.setBlockAndUpdate(blockPos, block.defaultBlockState());
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if(tileEntity instanceof PortalTileEntity) {
            ((PortalTileEntity) tileEntity).setWorldId(portalTo.location());
            tileEntity.setChanged();
        }
    }

    public static Optional<World> getWorldFromTileEntity(World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity instanceof PortalTileEntity) {
            PortalTileEntity portalTileEntity = (PortalTileEntity) tileEntity;
            if (portalTileEntity.getWorldId() != null) {
                return Optional.ofNullable(world.getServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, portalTileEntity.getWorldId())));
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
