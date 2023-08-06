package com.gmail.sneakdevs.phantomconfig;

import com.gmail.sneakdevs.phantomconfig.config.PhantomConfigConfig;
import com.gmail.sneakdevs.phantomconfig.interfaces.ServerPlayerInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import net.minecraft.util.RandomSource;
import java.util.Iterator;
import java.util.Random;

public class InsanityPhantomSpawner implements CustomSpawner {
    private int insanityTick;

    public int tick(ServerLevel serverLevel, boolean bl, boolean bl2) {
        if (!bl) {
            return 0;
        }
        if (PhantomConfigConfig.getInstance().doInsanity) {
            --this.insanityTick;
            if (this.insanityTick <= 0) {
                this.insanityTick += (PhantomConfigConfig.getInstance().insanityMinCycleTime + serverLevel.random.nextInt(PhantomConfigConfig.getInstance().insanityRandomizationTime)) * 20;
                return insanitySpawn(serverLevel);
            }
        }
        return 0;
    }

    private int insanitySpawn(ServerLevel serverLevel) {
        RandomSource random = serverLevel.random;
        int phantomsSpawned = 0;
        Iterator playerList = serverLevel.players().iterator();
        DifficultyInstance difficultyInstance;
        BlockPos blockPos2;
        BlockState blockState;
        FluidState fluidState;
        Player player;
        while (playerList.hasNext()) {
            player = (Player)playerList.next();
            if (!player.isSpectator() && Math.abs(player.getX()) + Math.abs(player.getZ()) > 1000) {
                int j = Mth.clamp(((ServerPlayerInterface) player).phantomconfig_getInsanity(), 1, 2147483647);
                if (random.nextInt(j) > PhantomConfigConfig.getInstance().insanitySpawnStartTimer * 20) {
                    BlockPos blockPos = player.blockPosition();
                    if (PhantomConfigConfig.getInstance().insanityLightStopsPhantoms >= serverLevel.getLightEmission(blockPos)) {
                        do {
                            blockPos2 = blockPos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
                            blockState = serverLevel.getBlockState(blockPos2);
                            fluidState = serverLevel.getFluidState(blockPos2);
                        } while (!NaturalSpawner.isValidEmptySpawnBlock(serverLevel, blockPos2, blockState, fluidState, EntityType.PHANTOM));
                        SpawnGroupData spawnGroupData = null;
                        difficultyInstance = serverLevel.getCurrentDifficultyAt(blockPos);
                        int l = 1 + random.nextInt(((((ServerPlayerInterface) player).phantomconfig_getInsanity() - (PhantomConfigConfig.getInstance().insomniaSpawnStartTimer * 20)) / 24000) + 1);

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
