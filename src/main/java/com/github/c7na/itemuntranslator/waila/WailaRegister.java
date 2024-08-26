package com.github.c7na.itemuntranslator.waila;

import net.minecraft.block.Block;

import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegister {

    public static void register(IWailaRegistrar register) {
        register.registerHeadProvider(new DataProvider(), Block.class);
    }
}
