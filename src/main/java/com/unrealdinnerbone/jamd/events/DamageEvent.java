package com.unrealdinnerbone.jamd.events;

import com.unrealdinnerbone.jamd.util.TelerportUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Collections;

public class DamageEvent
{
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(DamageEvent::onDamge);
    }

    private static void onDamge(LivingHurtEvent event) {
        if(event.getEntity().getEntityWorld() instanceof ServerWorld) {
            if(event.getEntity() instanceof PlayerEntity) {
                if(event.getSource() == DamageSource.OUT_OF_WORLD) {
                    PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
                    if(playerEntity.inventory.hasAny(Collections.singleton(Items.NETHER_STAR))) {
                        event.setCanceled(true);
                        TelerportUtils.toVoid(playerEntity);
                    }
                }
            }

        }
    }
}
