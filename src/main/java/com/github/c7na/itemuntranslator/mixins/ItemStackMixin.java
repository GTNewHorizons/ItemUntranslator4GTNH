package com.github.c7na.itemuntranslator.mixins;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.c7na.itemuntranslator.ItemUntranslator;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    private static int recursionDepth;

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void itemuntranslator$appendEnglish(EntityPlayer player, boolean advanced,
        CallbackInfoReturnable<List> cir) {
        if (!ItemUntranslator.SHOW_TOOLTIP) return;
        List arraylist = cir.getReturnValue();
        if (arraylist == null || arraylist.isEmpty()) return;

        ItemStack stack = (ItemStack) (Object) this;

        if (ItemUntranslator.isBlacklisted(stack)) return;

        String englishName = null;
        synchronized (ItemUntranslator.getTooltipLock) {
            try {
                recursionDepth++;
                if (ItemUntranslator.getTooltipThread == null) {
                    ItemUntranslator.getTooltipThread = Thread.currentThread();
                }
                englishName = stack.getDisplayName();
            } finally {
                recursionDepth--;
                if (recursionDepth <= 0) {
                    recursionDepth = 0;
                    ItemUntranslator.getTooltipThread = null;
                }
            }
        }

        if (englishName != null) {
            Object first = arraylist.get(0);
            if (first == null || !englishName.equals(first.toString())) {
                arraylist.add(ItemUntranslator.EN_SUFFIX + englishName);
                cir.setReturnValue(arraylist);
            }
        }
    }
}
