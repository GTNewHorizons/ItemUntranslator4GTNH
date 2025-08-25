package com.github.c7na.itemuntranslator.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.github.c7na.itemuntranslator.ItemUntranslator;

public class CommandGtip extends CommandBase {

    @Override
    public String getCommandName() {
        return "gtip";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gtip on|off";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }
        if ("on".equalsIgnoreCase(args[0])) {
            ItemUntranslator.SHOW_TOOLTIP = true;
            sender.addChatMessage(new ChatComponentText("Original tooltip names: ON"));
            ItemUntranslator.saveConfig();
        } else if ("off".equalsIgnoreCase(args[0])) {
            ItemUntranslator.SHOW_TOOLTIP = false;
            sender.addChatMessage(new ChatComponentText("Original tooltip names: OFF"));
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
