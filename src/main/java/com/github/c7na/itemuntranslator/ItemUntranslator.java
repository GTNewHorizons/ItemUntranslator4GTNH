package com.github.c7na.itemuntranslator;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Tags.MODID)
public class ItemUntranslator {

    public static final Object getTooltipLock = new Object();
    public static Thread getTooltipThread;

    public static final String EN_SUFFIX = "[EN] ";
    public static volatile boolean SHOW_TOOLTIP = true;
    public static volatile boolean SHOW_WAILA = true;

    private static Configuration config;

    private static final Set<String> BLACKLIST_UNLOCALIZED = new HashSet<>(
        Arrays.asList(
            "tile.blockHellFire",
            "tile.blockPineLogLog",
            "tile.blockRainforestOakLeaves",
            "tile.blockRainforestOakSapling",
            "tile.blockRainforestOakLog",
            "tile.blockPineLogLog",
            "tile.blockPineLeaves",
            "tile.blockPineSapling",
            "tile.blockDarkWorldGround",
            "tile.blockDarkWorldGround2",
            "item.MiscUtils.bucket.bucketPyrotheum",
            "item.MiscUtils.bucket.bucketCryotheum",
            "item.MiscUtils.bucket.bucketEnder",
            "item.MiscUtils.material.rodBlizz",
            "tile.fluidBlockSludge",
            "tile.blockInfiniteFluidTank",
            "tile.blockCircuitProgrammer",
            "tile.blockDarkWorldPortalFrame"));

    private static final Set<String> BLACKLIST_REGISTRY = new HashSet<>(Arrays.asList("miscutils:blockBlockSelenium"));

    private static final Set<String> BLACKLIST_CLASSES = new HashSet<>(
        Arrays.asList("gtPlusPlus.core.item.", "gtPlusPlus.xmod.forestry.bees.items.MBItemFrame"));

    public static boolean shouldUntranslate() {
        Thread current = Thread.currentThread();
        return current.equals(getTooltipThread);
    }

    public static boolean isBlacklisted(ItemStack stack) {
        if (stack == null) return false;
        try {
            // unlocalized
            String unloc = stack.getUnlocalizedName();
            if (unloc != null) {
                for (String key : BLACKLIST_UNLOCALIZED) {
                    if (unloc.equals(key) || unloc.startsWith(key)) return true;
                }
            }

            // registry name
            try {
                String reg = stack.getItem().delegate.name();
                if (reg != null) {
                    for (String key : BLACKLIST_REGISTRY) {
                        if (reg.equals(key) || reg.startsWith(key)) return true;
                    }
                }
            } catch (Throwable ignored) {}

            // class name
            String cls = stack.getItem()
                .getClass()
                .getName();
            if (cls != null) {
                for (String key : BLACKLIST_CLASSES) {
                    if (cls.equals(key) || cls.startsWith(key)) return true;
                }
            }

        } catch (Throwable ignored) {}
        return false;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // загружаем конфиг
        File cfgFile = new File("config", "itemuntranslator.cfg");
        config = new Configuration(cfgFile);
        try {
            config.load();
            SHOW_TOOLTIP = config
                .getBoolean("showTooltip", Configuration.CATEGORY_GENERAL, true, "Show original names in item tooltip");
            SHOW_WAILA = config
                .getBoolean("showWaila", Configuration.CATEGORY_GENERAL, true, "Show original names in Waila tooltip");
        } catch (Exception e) {
            System.err.println("Could not load ItemUntranslator config!");
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }

        if (event.getSide()
            .isClient()) {
            FMLInterModComms
                .sendMessage("Waila", "register", "com.github.c7na.itemuntranslator.waila.WailaRegister.register");
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new com.github.c7na.itemuntranslator.command.CommandGtip());
        event.registerServerCommand(new com.github.c7na.itemuntranslator.command.CommandWtip());
        event.registerServerCommand(new com.github.c7na.itemuntranslator.command.CommandTip());
        event.registerServerCommand(new com.github.c7na.itemuntranslator.command.CommandWatip());
    }

    public static void saveConfig() {
        if (config == null) return;
        config.get(Configuration.CATEGORY_GENERAL, "showTooltip", SHOW_TOOLTIP)
            .set(SHOW_TOOLTIP);
        config.get(Configuration.CATEGORY_GENERAL, "showWaila", SHOW_WAILA)
            .set(SHOW_WAILA);
        if (config.hasChanged()) {
            config.save();
        }
    }
}
