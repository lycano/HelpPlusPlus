/*
 * HelpPlusPlus - Help pages for smarter people
 * Copyright (C) 2011 lycano <https://github.com/lycano/HelpPlusPlus>
 * Original Credit & Copyright (C) 2011 tkelly910 <https://github.com/tkelly910/Help>
 * 
 * This file is part of PermissionsEx. This is a modified version.
 * 
 * PermissionsEx - Permissions plugin for Bukkit
 * Copyright (C) 2011 t3hk0d3 http://www.tehkode.ru
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.luricos.bukkit.HelpPlusPlus.commands;

import de.luricos.bukkit.HelpPlusPlus.bukkit.HelpPlusPlus;
import de.luricos.bukkit.HelpPlusPlus.utils.HelpLogger;
import de.luricos.bukkit.HelpPlusPlus.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CommandsManager {
    protected Map<String, Map<CommandSyntax, CommandBinding>> listeners = new HashMap<String, Map<CommandSyntax, CommandBinding>>();
    protected HelpPlusPlus plugin;
    
    public CommandsManager(HelpPlusPlus plugin) {
        this.plugin = plugin;
    }    
    
    public void register(CommandListener listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            Command cmdAnnotation = method.getAnnotation(Command.class);

            Map<CommandSyntax, CommandBinding> commandListeners = listeners.get(cmdAnnotation.name());
            if (commandListeners == null) {
                commandListeners = new HashMap<CommandSyntax, CommandBinding>();
                listeners.put(cmdAnnotation.name(), commandListeners);
            }

            if (HelpPlusPlus.getCoreManager().isHelpActive() && !cmdAnnotation.description().isEmpty()) {
                HelpPlusPlus.getCoreManager().registerCommand(cmdAnnotation.name() + " " + cmdAnnotation.syntax(), cmdAnnotation.description(), plugin, cmdAnnotation.isPrimary(), cmdAnnotation.permission());
            }

            commandListeners.put(new CommandSyntax(cmdAnnotation.syntax()), new CommandBinding(listener, method));
        }
    }

    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String[] args) {
        Map<CommandSyntax, CommandBinding> callMap = this.listeners.get(command.getName());

        // No commands registered
        if (callMap == null) {
            return false;
        }

        CommandBinding selectedBinding = null;
        int argumentsLength = 0;
        String arguments = StringUtils.implode(args, " ");

        for (Entry<CommandSyntax, CommandBinding> entry : callMap.entrySet()) {
            CommandSyntax syntax = entry.getKey();
            if (!syntax.isMatch(arguments)) {
                continue;
            }
            
            // match, but there are already other variants present
            if (selectedBinding != null && syntax.getRegexp().length() < argumentsLength) {
                continue;
            }

            CommandBinding binding = entry.getValue();
            binding.setParams(syntax.getMatchedArguments(arguments));
            selectedBinding = binding;
        }

        // error in command syntax @TODO: redirect to internalHelp
        if (selectedBinding == null) {
            sender.sendMessage(ChatColor.RED + "Error in command syntax. Check command help.");
            return true;
        }

        // Check permission
        Command commandAnnotation = selectedBinding.getMethodAnnotation();
        if (!commandAnnotation.permission().isEmpty() && sender instanceof Player) { // this method are not public and required permission
            if (!HelpPlusPlus.getPermission().has((Player) sender, commandAnnotation.permission())) {
                HelpLogger.warning("Player '" + ((Player) sender).getName() + "' has tried to access chat command \"" + command.getName() + " " + arguments + "\","
                        + " but don't have permissions (" + commandAnnotation.permission() + ") to do that.");
                sender.sendMessage(ChatColor.RED + "Sorry, you don't have permissions to do that.");
                return true;
            }
        }


        try {
            selectedBinding.call(this.plugin, commandAnnotation, sender, selectedBinding.getParams());
        } catch (RuntimeException e) {
            HelpLogger.severe("Ambiguous command handler found for " + command.getName() + " command. (Is the calling plugin up to date?)");
            e.printStackTrace();
        }

        return true;
    }
}
