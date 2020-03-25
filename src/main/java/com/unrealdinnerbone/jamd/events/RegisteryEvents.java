package com.unrealdinnerbone.jamd.events;

import com.unrealdinnerbone.jamd.JAVD;
import com.unrealdinnerbone.jamd.JAVDRegistry;
import com.unrealdinnerbone.jamd.block.PortalBlock;
import com.unrealdinnerbone.jamd.block.PortalTitleEnity;
import com.unrealdinnerbone.jamd.block.VoidPortalBlock;
import com.unrealdinnerbone.jamd.dim.ChunkGen;
import com.unrealdinnerbone.jamd.dim.VoidBiome;
import com.unrealdinnerbone.jamd.dim.VoidDim;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisteryEvents
{

    @SubscribeEvent
    public void registerTiles(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(PortalTitleEnity::new, JAVDRegistry.PORTAL_BLOCK).build(null).setRegistryName(idOf("portal")));
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new PortalBlock().setRegistryName(idOf("portal_block")));
        event.getRegistry().register(new VoidPortalBlock().setRegistryName(idOf("void_portal_block")));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new BlockItem(JAVDRegistry.PORTAL_BLOCK, new Item.Properties().group(ItemGroup.TRANSPORTATION)).setRegistryName(idOf("portal_block")));
    }

    @SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().register(new VoidBiome().setRegistryName(idOf("the_void")));
    }

    @SubscribeEvent
    public void registerDim(RegistryEvent.Register<ModDimension> typeRegister) {
        typeRegister.getRegistry().register(ModDimension.withFactory(VoidDim::new).setRegistryName(idOf( "void")));
    }

    @SubscribeEvent
    public void registerChunk(RegistryEvent.Register<ChunkGeneratorType<?, ?>> event) {
        event.getRegistry().register(new ChunkGeneratorType<>(ChunkGen::new, false, GenerationSettings::new).setRegistryName(idOf("void_chunk")));
    }


    private static ResourceLocation idOf(String name) {
        return new ResourceLocation(JAVD.MOD_ID, name);
    }
}
