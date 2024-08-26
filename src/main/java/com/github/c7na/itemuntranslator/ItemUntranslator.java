package com.github.c7na.itemuntranslator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;

@Mod(modid = Tags.MODID)
public class ItemUntranslator {

    public static final Object getTooltipLock = new Object();
    public static Thread getTooltipThread;

    public static boolean shouldUntranslate() {
        Thread current = Thread.currentThread();
        return current.equals(getTooltipThread);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide()
            .isClient()) {
            FMLInterModComms
                .sendMessage("Waila", "register", "com.github.c7na.itemuntranslator.waila.WailaRegister.register");
        }
    }
}
