package com.unrealdinnerbone.jamd.block;

import com.unrealdinnerbone.jamd.JAVDRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

import java.util.UUID;

public class PortalTitleEnity extends TileEntity {

    public static final UUID FAKE_ID = UUID.fromString("59b66d3c-a220-4827-a73b-eb0ade057c51");
    private UUID uuid;

    public PortalTitleEnity() {
        super(JAVDRegistry.PORTAL);
        this.uuid = FAKE_ID;
    }


    public void setLinked(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getLinked() {
        return uuid;
    }


    @Override
    public void read(CompoundNBT tag) {
        this.uuid = tag.getUniqueId("linked");
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT nbt = super.write(tag);
        nbt.putUniqueId("linked", uuid);
        return nbt;
    }
}
