package com.github.c7na.itemuntranslator.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.github.c7na.itemuntranslator.ItemUntranslator;

public class CommandWtip extends CommandBase {

    @Override
    public String getCommandName() {
        return "wtip";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/wtip on|off";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }
        if ("on".equalsIgnoreCase(args[0])) {
            ItemUntranslator.SHOW_WAILA = true;
            sender.addChatMessage(new ChatComponentText("Waila original names: ON"));
            ItemUntranslator.saveConfig();
        } else if ("off".equalsIgnoreCase(args[0])) {
            ItemUntranslator.SHOW_WAILA = false;
            sender.addChatMessage(new ChatComponentText("Waila original names: OFF"));
            ItemUntranslator.saveConfig();
        } else {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
