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

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List getWailaHead(ItemStack itemStack, List currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        if (!ItemUntranslator.SHOW_WAILA) return currenttip;
        ItemStack stack = accessor.getStack();
        if (stack == null) return currenttip;
        if (ItemUntranslator.isBlacklisted(stack)) return currenttip;

        String englishName = null;
        try {
            ItemUntranslator.getTooltipThread = Thread.currentThread();
            englishName = stack.getDisplayName();
        } finally {
            ItemUntranslator.getTooltipThread = null;
        }

        if (englishName != null && !currenttip.isEmpty()) {
            Object first = currenttip.get(0);
            if (first == null || !englishName.equals(first.toString())) {
                currenttip.add(englishName);
            }
        }
        return currenttip;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List getWailaBody(ItemStack itemStack, List currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List getWailaTail(ItemStack itemStack, List currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x,
        int y, int z) {
        return tag;
    }
}
