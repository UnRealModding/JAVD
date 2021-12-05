package com.unrealdinnerbone.javd;

import com.unrealdinnerbone.javd.block.PortalBlock;
import com.unrealdinnerbone.javd.block.PortalTileEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;

public class JAVDRegistry {


    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JAVD.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JAVD.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, JAVD.MOD_ID);

    public static final List<DeferredRegister<?>> REGISTRIES = Arrays.asList(BLOCKS, ITEMS, TILES);

    public static final RegistryObject<PortalBlock> PORTAL_BLOCK = BLOCKS.register("portal_block", PortalBlock::new);

    public static final RegistryObject<Item> PORTAL_BLOCK_ITEM = ITEMS.register("portal_block", () -> new BlockItem(PORTAL_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

    public static final RegistryObject<BlockEntityType<PortalTileEntity>> PORTAL = TILES.register("portal", () -> BlockEntityType.Builder.of(PortalTileEntity::new, PORTAL_BLOCK.get()).build(null));


}
