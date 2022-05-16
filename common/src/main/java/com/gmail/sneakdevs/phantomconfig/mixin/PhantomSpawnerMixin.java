package com.gmail.sneakdevs.phantomconfig.mixin;

import com.gmail.sneakdevs.phantomconfig.config.PhantomConfigConfig;
import com.gmail.sneakdevs.phantomconfig.interfaces.ServerPlayerInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Random;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    private int insomniaTick;

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void phantomconfig_tickMixin(ServerLevel serverLevel, boolean bl, boolean bl2, CallbackInfoReturnable<Integer> cir) {
        if (!bl) {
            cir.setReturnValue(0);
        }
        if (PhantomConfigConfig.getInstance().doInsomnia) {
            --this.insomniaTick;
            if (this.insomniaTick <= 0) {
                this.insomniaTick += (PhantomConfigConfig.getInstance().insomniaMinCycleTime + serverLevel.random.nextInt(PhantomConfigConfig.getInstance().insomniaRandomizationTime)) * 20;
                if (!serverLevel.isDay()) {
                    cir.setReturnValue(insomniaSpawn(serverLevel));
                }
            }
        }
        cir.setReturnValue(0);
    }

    private int insomniaSpawn(ServerLevel serverLevel) {
        Random random = serverLevel.random;
        int phantomsSpawned = 0;
        Iterator playerList = serverLevel.players().iterator();
        DifficultyInstance difficultyInstance;
        BlockPos blockPos2;
        BlockState blockState;
        FluidState fluidState;
        Player player;
        while (playerList.hasNext()) {
            player = (Player)playerList.next();
            if (!player.isSpectator()) {
                ServerStatsCounter serverStatsCounter = ((ServerPlayer) player).getStats();
                int j = Mth.clamp(serverStatsCounter.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, 2147483647);
                if (random.nextInt(j) > PhantomConfigConfig.getInstance().insomniaSpawnStartTimer*20) {
                    BlockPos blockPos = player.blockPosition();
                    if (blockPos.getY() >= serverLevel.getSeaLevel() && serverLevel.canSeeSky(blockPos) && (PhantomConfigConfig.getInstance().insomniaLightStopsPhantoms >= serverLevel.getLightEmission(blockPos))) {
                        do {
                            blockPos2 = blockPos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
                            blockState = serverLevel.getBlockState(blockPos2);
                            fluidState = serverLevel.getFluidState(blockPos2);
                        } while (!NaturalSpawner.isValidEmptySpawnBlock(serverLevel, blockPos2, blockState, fluidState, EntityType.PHANTOM));
                        SpawnGroupData spawnGroupData = null;
                        difficultyInstance = serverLevel.getCurrentDifficultyAt(blockPos);
                        int l = 1 + random.nextInt(((Mth.clamp(serverStatsCounter.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, 2147483647) - (PhantomConfigConfig.getInstance().insomniaSpawnStartTimer * 20)) / 24000)+ 1);

                        for (int m = 0; m < l; ++m) {
                            Phantom phantom = EntityType.PHANTOM.create(serverLevel);
                            phantom.moveTo(blockPos2, 0.0F, 0.0F);
                            spawnGroupData = phantom.finalizeSpawn(serverLevel, difficultyInstance, MobSpawnType.NATURAL, spawnGroupData, null);
                            serverLevel.addFreshEntityWithPassengers(phantom);
                        }
                        phantomsSpawned += l;
                    }
                }
            }
        }
        return phantomsSpawned;
    }
}