package com.unrealdinnerbone.jamd;

import com.unrealdinnerbone.jamd.block.PortalBlock;
import com.unrealdinnerbone.jamd.block.PortalTileEntity;
import com.unrealdinnerbone.jamd.block.VoidPortalBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
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


    public static final RegistryObject<PortalBlock> PORTAL_BLOCK = BLOCKS.register("portal_block", () -> new PortalBlock(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2), JAVD.VOID_DIMENSION.func_240901_a_(), true));

    public static final RegistryObject<Item> PORTAL_BLOCK_ITEM = ITEMS.register("portal_block", () -> new BlockItem(PORTAL_BLOCK.get(), new Item.Properties().group(ItemGroup.TRANSPORTATION)));

    public static final RegistryObject<PortalBlock> VOID_PORTAL_BLOCK = BLOCKS.register("void_portal_block", () -> {
        return new PortalBlock(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2), World.field_234918_g_.getRegistryName(), false);
    });

    public static final RegistryObject<TileEntityType<PortalTileEntity>> PORTAL = TILES.register("portal", () -> TileEntityType.Builder.create(PortalTileEntity::new, PORTAL_BLOCK.get(), VOID_PORTAL_BLOCK.get()).build(null));


}
