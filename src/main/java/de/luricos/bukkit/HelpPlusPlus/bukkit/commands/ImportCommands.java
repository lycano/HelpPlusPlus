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

package de.luricos.bukkit.HelpPlusPlus.bukkit.commands;

import de.luricos.bukkit.HelpPlusPlus.bukkit.HelpPlusPlus;
import de.luricos.bukkit.HelpPlusPlus.commands.Command;
import de.luricos.bukkit.HelpPlusPlus.permissions.PermissionsStrings;

import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * ImportCommands annotation
 * 
 * @author lycano
 */
public class ImportCommands extends HelpCommands {       
    @Command(name = "help",
    syntax = "plugins list",
    permission = PermissionsStrings.menu,
    description = "List registered plugins",
    isPrimary = false)
    public void listPlugins(HelpPlusPlus plugin, Command commandAnnotation, CommandSender sender, Map<String, String> args) {
        HelpPlusPlus.getCoreManager().listPlugins(sender);
    }
    
    @Command(name = "help",
    syntax = "plugins import <plugins>",
    permission = PermissionsStrings.menu,
    description = "Import plugin/s (comma-separated list)",
    isPrimary = false)
    public void importPlugins(HelpPlusPlus plugin, Command commandAnnotation, CommandSender sender, Map<String, String> args) {
        HelpPlusPlus.getCoreManager().importPlugins(sender);
    }    
}
