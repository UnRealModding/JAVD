package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAVDRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.print.attribute.standard.Sides;

public class PortalTileEntity extends TileEntity {

    private ResourceLocation worldId;

    public PortalTileEntity() {
        super(JAVDRegistry.PORTAL.get());
    }

    public void setWorldId(ResourceLocation worldId) {
        this.worldId = worldId;
    }

    public ResourceLocation getWorldId() {
        return worldId;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("world_id", worldId.toString());
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        if(nbt.contains("world_id")) {
            worldId = tryCreate(nbt.getString("world_id"));
        }
        super.read(state, nbt);
    }


    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString("world_id", worldId.toString());
        return new SUpdateTileEntityPacket(getPos(), 0, compoundNBT);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getNbtCompound();
        if(nbt.contains("world_id")) {
            worldId = tryCreate(nbt.getString("world_id"));
        }

    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString("world_id", worldId.toString());
        return compoundNBT;
    }

    public static ResourceLocation tryCreate(String name) {
        try {
            ResourceLocation resourceLocation = new ResourceLocation(name);
            return resourceLocation;
        }catch (Exception e) {
            return null;
        }
    }
}
