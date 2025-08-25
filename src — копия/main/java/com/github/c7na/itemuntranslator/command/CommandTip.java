package com.github.c7na.itemuntranslator.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Общая команда /tip on|off
 * Делает то же, что /gtip, и дополнительно вызывает /tipp из второго мода.
 */
public class CommandTip extends CommandBase {

    @Override
    public String getCommandName() {
        return "tip";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tip on|off";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        // Сначала выполняем нашу команду
        new CommandGtip().processCommand(sender, args);

        // Потом пробуем вызвать вторую
        try {
            net.minecraft.server.MinecraftServer server = net.minecraft.server.MinecraftServer.getServer();
            server.getCommandManager()
                .executeCommand(sender, "tipp " + args[0]);
        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText("§cОшибка при вызове команды /tipp: " + e.getMessage()));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
