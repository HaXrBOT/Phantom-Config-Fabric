package com.gmail.sneakdevs.phantomconfigforge;

import com.gmail.sneakdevs.phantomconfig.config.PhantomConfigConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import com.gmail.sneakdevs.phantomconfig.PhantomConfig;
import net.minecraftforge.fml.common.Mod;

@Mod(PhantomConfig.MODID)
public class PhantomConfigForge {
    public PhantomConfigForge() {
        AutoConfig.register(PhantomConfigConfig.class, JanksonConfigSerializer::new);
    }
}
