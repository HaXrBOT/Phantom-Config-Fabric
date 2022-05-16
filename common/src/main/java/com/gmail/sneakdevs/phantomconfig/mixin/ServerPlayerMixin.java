package com.gmail.sneakdevs.phantomconfig.mixin;

import com.gmail.sneakdevs.phantomconfig.config.PhantomConfigConfig;
import com.gmail.sneakdevs.phantomconfig.interfaces.ServerPlayerInterface;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements ServerPlayerInterface {
    private int insanity;

    @Inject(at = @At("HEAD"), method = "tick")
    private void phantomconfig_tickMixin(CallbackInfo ci) {
        if (PhantomConfigConfig.getInstance().doInsanity && Math.abs(((ServerPlayer)(Object)this).getX()) + Math.abs(((ServerPlayer)(Object)this).getZ()) > 1000 && !((ServerPlayer)(Object)this).getLevel().dimensionType().hasSkyLight() && !((ServerPlayer)(Object)this).getLevel().dimensionType().bedWorks()) {
            insanity++;
        }
    }

    public int phantomconfig_getInsanity() {
        return insanity;
    }
}