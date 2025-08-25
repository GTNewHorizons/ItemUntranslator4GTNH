package com.github.c7na.itemuntranslator.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Общая команда /watip on|off
 * Делает то же, что /wtip, и дополнительно вызывает /wtipp из второго мода.
 */
public class CommandWatip extends CommandBase {

    @Override
    public String getCommandName() {
        return "watip";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/watip on|off";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        // Сначала выполняем нашу команду
        new CommandWtip().processCommand(sender, args);

        // Потом пробуем вызвать вторую
        try {
            net.minecraft.server.MinecraftServer server = net.minecraft.server.MinecraftServer.getServer();
            server.getCommandManager()
                .executeCommand(sender, "wtipp " + args[0]);
        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText("§cОшибка при вызове команды /wtipp: " + e.getMessage()));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
