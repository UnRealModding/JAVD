package com.unrealdinnerbone.jamd.util;

import com.unrealdinnerbone.jamd.JAVD;
import jdk.nashorn.internal.ir.Block;
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

public class PlayerSaveData extends WorldSavedData {

    private static final PlayerSaveData clientStorageCopy = new PlayerSaveData();

    private final HashMap<String, BlockPos> spawnLocations;

    public PlayerSaveData() {
        super(JAVD.MOD_ID + "_players");
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
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        List<String> storedValues = new ArrayList<>();
        spawnLocations.forEach((uuid, blockPos) -> storedValues.add(uuid + "@" + blockPos.toLong()));
        ListNBT listNBT = new ListNBT();
        storedValues.forEach(s -> listNBT.add(StringNBT.valueOf(s)));
        compound.put("locations", listNBT);
        return compound;
    }

    public BlockPos getPlayersSpawnLocation(UUID uuid) {
        if(spawnLocations.containsKey(uuid.toString())) {
            BlockPos blockPos = spawnLocations.get(uuid.toString());
            return blockPos;
        }else {
            return null;
        }
    }

    public void setPlayersSpawnLocation(UUID uuid, BlockPos blockPos) {
//        if(this.spawnLocations.containsKey(uuid) && this.spawnLocations.get(uuid).toLong() == blockPos.toLong()) {
//            return;
//        }
        this.spawnLocations.remove(uuid.toString());
        this.spawnLocations.put(uuid.toString(), blockPos);
        markDirty();
    }


    public static PlayerSaveData get(World world)
    {
        if (!(world instanceof ServerWorld))
        {
            return clientStorageCopy;
        }

        ServerWorld overworld = world.getServer().getWorld(DimensionType.OVERWORLD);
        DimensionSavedDataManager storage = overworld.getSavedData();
        return storage.getOrCreate(PlayerSaveData::new, JAVD.MOD_ID + "_players");
    }

    public HashMap<String, BlockPos> getSpawnLocations() {
        return spawnLocations;
    }
}
