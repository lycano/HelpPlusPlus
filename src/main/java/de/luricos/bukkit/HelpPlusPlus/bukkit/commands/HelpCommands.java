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
 * PermissionsEx is free software; you can redistribute it and/or
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

import de.luricos.bukkit.HelpPlusPlus.commands.Command;
import de.luricos.bukkit.HelpPlusPlus.commands.CommandListener;
import de.luricos.bukkit.HelpPlusPlus.bukkit.HelpPlusPlus;
import de.luricos.bukkit.HelpPlusPlus.permissions.PermissionsStrings;
import de.luricos.bukkit.HelpPlusPlus.utils.MCFontUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * HelpCommands annotation
 * 
 * @author lycano
 */
public class HelpCommands implements CommandListener {   
    
    @Command(name = "help",
    syntax = "version",
    permission = PermissionsStrings.version,
    description = "Display version",
    isPrimary = true)
    public void showVersion(HelpPlusPlus plugin, Command commandAnnotation, CommandSender sender, Map<String, String> args) {
        sender.sendMessage(ChatColor.GOLD + HelpPlusPlus.getInternalName() + " running: v" + HelpPlusPlus.getVersion());
    }

    @Command(name = "help",
    syntax = "list [pagenumber]",
    permission = PermissionsStrings.menu,
    description = "List commands. Optional at [pagenumber]",
    isPrimary = false)
    public void listHelpPage(HelpPlusPlus plugin, Command commandAnnotation, CommandSender sender, Map<String, String> args) {
        HelpPlusPlus.getCoreManager().listHelpPage(sender, args.get("pagenumber"));
    }
    
    @Command(name = "help",
    syntax = "reload",
    permission = PermissionsStrings.menu,
    description = "Reload help pages",
    isPrimary = false)
    public void reloadHelp(HelpPlusPlus plugin, Command commandAnnotation, CommandSender sender, Map<String, String> args) {
        HelpPlusPlus.getCoreManager().reloadSettings(sender);
    }        
    
    @Command(name = "help",
    syntax = "search <command>",
    permission = PermissionsStrings.menu,
    description = "Search for registered command",
    isPrimary = false)
    public void searchCommand(HelpPlusPlus plugin, Command commandAnnotation, CommandSender sender, Map<String, String> args) {
        HelpPlusPlus.getCoreManager().searchCommand(sender, args.get("command"));
    }
    
    @Command(name = "help",
    syntax = "show <plugin> [pagenumber]",
    permission = PermissionsStrings.menu,
    description = "Show plugin commands. Optional with [pagenumber]",
    isPrimary = true)
    public void listPluginCommands(HelpPlusPlus plugin, Command commandAnnotation, CommandSender sender, Map<String, String> args) {
        HelpPlusPlus.getCoreManager().listPluginHelp(sender, args.get("plugin"), args.get("pagenumber"));
    }       
    
    @Command(name = "help",
    syntax = "more [pagenumber]",
    permission = PermissionsStrings.menu,
    description = "Displays more /help options. Optional at [pagenumber]",
    isPrimary = true)
    public void listInternalCommands(HelpPlusPlus plugin, Command commandAnnotation, CommandSender sender, Map<String, String> args) {
        HelpPlusPlus.getCoreManager().listPluginHelp(sender, HelpPlusPlus.getPluginName(), args.get("pagenumber"));
    }
}
    