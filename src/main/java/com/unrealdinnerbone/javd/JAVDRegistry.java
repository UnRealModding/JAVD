package com.unrealdinnerbone.javd;

import com.unrealdinnerbone.javd.block.PortalBlock;
import com.unrealdinnerbone.javd.block.PortalTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class JAVDRegistry {


    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JAVD.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JAVD.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, JAVD.MOD_ID);

    public static final List<DeferredRegister<?>> REGISTRIES = Arrays.asList(BLOCKS, ITEMS, TILES);


    public static final RegistryObject<PortalBlock> PORTAL_BLOCK = BLOCKS.register("portal_block", () -> new PortalBlock("void"));

    public static final RegistryObject<PortalBlock> MINE_PORTAL_BLOCK = BLOCKS.register("mine_portal_block", () -> new PortalBlock("mining"));

    public static final RegistryObject<Item> PORTAL_BLOCK_ITEM = ITEMS.register("portal_block", () -> new BlockItem(PORTAL_BLOCK.get(), new Item.Properties().group(ItemGroup.TRANSPORTATION)));
    public static final RegistryObject<Item> MINE_PORTAL_BLOCK_ITEM = ITEMS.register("mine_portal_block", () -> new BlockItem(MINE_PORTAL_BLOCK.get(), new Item.Properties().group(ItemGroup.TRANSPORTATION)));


    public static final RegistryObject<TileEntityType<PortalTileEntity>> PORTAL = TILES.register("portal", () -> TileEntityType.Builder.create(PortalTileEntity::new, PORTAL_BLOCK.get()).build(null));


}
