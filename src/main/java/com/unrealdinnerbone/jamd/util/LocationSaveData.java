package com.unrealdinnerbone.jamd.util;

import com.unrealdinnerbone.jamd.JAVD;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LocationSaveData extends WorldSavedData {

    private static final LocationSaveData clientStorageCopy = new LocationSaveData();


    private static final BlockPos ZERO = new BlockPos(0, 64, 0);
    private final HashMap<String, BlockPos> spawnLocations;
    private int xLocation = 0;
    private int zLocation = 0;

    public LocationSaveData() {
        super(JAVD.MOD_ID);
        this.spawnLocations = new HashMap<>();
    }

    @Override
    public void read(CompoundNBT nbt) {
        spawnLocations.clear();
        ListNBT listNBT = (ListNBT) nbt.get("locations");
        listNBT.forEach(inbt -> {
            StringNBT stringNBT = (StringNBT) inbt;
            String[] values = stringNBT.getString().split("@");
            BlockPos blockPos = BlockPos.fromLong(Long.parseLong(values[1]));
            spawnLocations.put(values[0], blockPos);
        });
        this.xLocation = nbt.getInt("x");
        this.zLocation = nbt.getInt("z");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        List<String> storedValues = new ArrayList<>();
        spawnLocations.forEach((uuid, blockPos) -> storedValues.add(uuid + "@" + blockPos.toLong()));
        ListNBT listNBT = new ListNBT();
        storedValues.forEach(s -> listNBT.add(StringNBT.valueOf(s)));
        compound.put("locations", listNBT);
        compound.putInt("x", xLocation);
        compound.putInt("z", zLocation);
        return compound;
    }

    public BlockPos findPortalLocationForPlayer(UUID uuid) {
        if(JAVD.PLAYER_VOIDS.get()) {
            return ZERO;
        }else {
            if(spawnLocations.containsKey(uuid.toString())) {
                return spawnLocations.get(uuid.toString());
            }else {
                this.xLocation += 10000;
                this.zLocation += 10000;
                BlockPos blockPos =  new BlockPos(xLocation, 64, zLocation);
                spawnLocations.put(uuid.toString(), blockPos);
                markDirty();
                return blockPos;
            }
        }
    }



    public static LocationSaveData get(World world)
    {
        if (!(world instanceof ServerWorld))
        {
            return clientStorageCopy;
        }

        ServerWorld overworld = world.getServer().getWorld(DimensionType.OVERWORLD);
        DimensionSavedDataManager storage = overworld.getSavedData();
        return storage.getOrCreate(LocationSaveData::new, JAVD.MOD_ID);
    }

    public HashMap<String, BlockPos> getSpawnLocations() {
        return spawnLocations;
    }
}
