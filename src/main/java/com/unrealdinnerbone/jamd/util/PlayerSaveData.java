package com.unrealdinnerbone.jamd.util;

import com.unrealdinnerbone.jamd.JAVD;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerSaveData extends WorldSavedData {

    private final HashMap<UUID, BlockPos> spawnLocations;

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
            UUID uuid = UUID.fromString(values[0]);
            BlockPos blockPos = BlockPos.fromLong(Long.parseLong(values[1]));
            spawnLocations.put(uuid, blockPos);
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        List<String> storedValues = new ArrayList<>();
        spawnLocations.forEach((uuid, blockPos) -> storedValues.add(uuid.toString() + "@" + blockPos.toLong()));
        ListNBT listNBT = new ListNBT();
        storedValues.forEach(s -> listNBT.add(StringNBT.valueOf(s)));
        compound.put("locations", listNBT);
        return compound;
    }

    public BlockPos getPlayersSpawnLocation(UUID uuid) {
        return spawnLocations.getOrDefault(uuid, null);
    }

    public void setPlayersSpawnLocation(UUID uuid, BlockPos blockPos) {
        if(this.spawnLocations.containsKey(uuid) && this.spawnLocations.get(uuid).equals(blockPos)) {
            return;
        }
        this.spawnLocations.put(uuid, blockPos);
        markDirty();
    }


    public static PlayerSaveData get(ServerWorld world) {
        return world.getServer().getWorld(DimensionType.OVERWORLD).getSavedData().getOrCreate(PlayerSaveData::new, JAVD.MOD_ID + "_players");
    }

    public HashMap<UUID, BlockPos> getSpawnLocations() {
        return spawnLocations;
    }
}
