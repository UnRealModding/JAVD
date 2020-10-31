package com.unrealdinnerbone.javd.mixin;

import com.unrealdinnerbone.javd.util.WorldUtils;
import net.minecraft.server.management.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Inject(at = @At("RETURN"), method = "reloadResources()V")
    public void onReloaded(CallbackInfo callbackInfo) {
        WorldUtils.WORLDS.reset();
    }
}
