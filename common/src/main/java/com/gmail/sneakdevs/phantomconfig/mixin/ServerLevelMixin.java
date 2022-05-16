package com.gmail.sneakdevs.phantomconfig.mixin;

import com.gmail.sneakdevs.phantomconfig.InsanityPhantomSpawner;
import com.gmail.sneakdevs.phantomconfig.interfaces.ServerPlayerInterface;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements ServerPlayerInterface {
    private final InsanityPhantomSpawner spawner = new InsanityPhantomSpawner();

    @Inject(at = @At("HEAD"), method = "tickCustomSpawners")
    private void phantomconfig_tickCustomSpawnersMixin(boolean bl, boolean bl2, CallbackInfo ci) {
        if (((Level)(Object)this).dimension().equals(Level.END)) {
            ((CustomSpawner)spawner).tick(((ServerLevel) (Object) this), bl, bl2);
        }
    }
}