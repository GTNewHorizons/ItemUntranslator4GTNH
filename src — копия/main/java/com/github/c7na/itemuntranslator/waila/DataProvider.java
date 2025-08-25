package com.github.c7na.itemuntranslator.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.github.c7na.itemuntranslator.ItemUntranslator;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class DataProvider implements IWailaDataProvider {

    private static int recursion = 0;

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {

        // --- наши новые условия ---
        if (!ItemUntranslator.SHOW_WAILA || itemStack == null) {
            return currenttip;
        }
        if (ItemUntranslator.isBlacklisted(itemStack)) {
            return currenttip;
        }
        // --------------------------

        String englishName;
        synchronized (ItemUntranslator.getTooltipLock) {
            try {
                if (ItemUntranslator.getTooltipThread == null) {
                    ItemUntranslator.getTooltipThread = Thread.currentThread();
                } else {
                    recursion++;
                }
                englishName = itemStack.getDisplayName();
            } finally {
                if (recursion == 0) {
                    ItemUntranslator.getTooltipThread = null;
                } else {
                    recursion--;
                }

            }
        }

        if (englishName != null && !currenttip.get(0)
            .contains(englishName)) {
            // добавляем с префиксом [EN] для единообразия
            currenttip.add(1, "[EN] " + englishName);
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x,
        int y, int z) {
        return tag;
    }
}
