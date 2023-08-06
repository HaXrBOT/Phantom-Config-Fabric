package com.gmail.sneakdevs.phantomconfig.config;

import com.gmail.sneakdevs.phantomconfig.PhantomConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = PhantomConfig.MODID)
public class PhantomConfigConfig implements ConfigData {
    @Comment("Whether phantoms spawn after no sleep (note: gamerule does nothing)")
    public boolean doInsomnia = true;

    @Comment("Whether phantoms spawn after being in the outer end for too long")
    public boolean doInsanity = true;

    @Comment("How many seconds until phantoms should start spawning based on insomnia (Vanilla: 3600)")
    public int insomniaSpawnStartTimer = 3600;

    @Comment("How many seconds until phantoms should start spawning based on insanity")
    public int insanitySpawnStartTimer = 2700;

    @Comment("Min seconds after phantoms have spawned to try to spawn more (Vanilla: 60)")
    public int insomniaMinCycleTime = 90;
    public int insanityMinCycleTime = 90;

    @Comment("Max seconds to randomly add from the min spawn cycle time (Vanilla: 60)")
    public int insomniaRandomizationTime = 80;
    public int insanityRandomizationTime = 80;

    @Comment("Light level where the player is standing that phantom spawning (Vanilla: 999 (disabled))")
    public int insomniaLightStopsPhantoms = 0;
    public int insanityLightStopsPhantoms = 999;

    public static PhantomConfigConfig getInstance() {
        return AutoConfig.getConfigHolder(PhantomConfigConfig.class).getConfig();
    }
}