package com.unrealdinnerbone.javd.util;

import net.minecraft.util.RegistryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldUtils
{
    public static List<String> BLACKLIST = Arrays.asList("minecraft:the_end", "minecraft:the_nether");
    public static final CachedValue<List<String>> WORLDS = new CachedValue<>(WorldUtils::getWorlds);

    private static List<String> getWorlds() {
        List<String> locations = new ArrayList<>();
        for (String id : RegistryKey.UNIVERSAL_KEY_MAP.keySet()) {
            String[] keys = id.split(":");
            if(keys.length == 4) {
                String type = keys[0] + ":" + keys[1];
                if(type.equalsIgnoreCase("minecraft:dimension")) {
                    String worldID = keys[2] + ":" + keys[3];
                    if(!BLACKLIST.contains(worldID)) {
                        locations.add(worldID);
                    }
                }
            }
        }
        return locations;
    }
}
