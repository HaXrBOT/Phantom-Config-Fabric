package com.gmail.sneakdevs.phantomconfig;

import com.gmail.sneakdevs.phantomconfig.config.PhantomConfigConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class PhantomConfigFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(PhantomConfigConfig.class, JanksonConfigSerializer::new);
    }
}
