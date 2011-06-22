/*
 * HelpPlusPlus - Help pages for smarter people
 * Copyright (C) 2011 lycano <https://github.com/lycano/HelpPlusPlus>
 * Original Credit & Copyright (C) 2011 tkelly910 <https://github.com/tkelly910/Help>
 * 
 * This file is part of Help (as of May 27, 2011).
 * Modified and forked from tkelly910 by jascotty2.
 * Copyright(C) 2011 jascotty2 <https://github.com/jascotty2/Help>
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

package de.luricos.bukkit.HelpPlusPlus.list;

import de.luricos.bukkit.HelpPlusPlus.bukkit.HelpPlusPlus;
import de.luricos.bukkit.HelpPlusPlus.config.BetterConfig;
import de.luricos.bukkit.HelpPlusPlus.utils.MCFontUtils;
import de.luricos.bukkit.HelpPlusPlus.utils.HelpColors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * HelpEntry class
 * 
 * @authors tkelly910, jascotty2, lycano
 */
public class HelpEntry {

    public String command;
    public String description;
    public String plugin;
    public String[] permissions;
    
    public boolean visible;
    public boolean main;
    
    private String helpFolder = "PluginHelp";

    public HelpEntry(String command, String description, String plugin, boolean main, String[] permissions, boolean visible) {
        this.command = command;
        this.description = description;
        this.plugin = plugin;
        this.main = main;
        this.permissions = permissions;
        this.visible = visible;
    }

    public HelpEntry(String command, String description, String plugin) {
        this(command, description, plugin, false, new String[]{}, true);
    }

    public HelpEntry(String command, String description, String plugin, boolean main) {
        this(command, description, plugin, main, new String[]{}, true);
    }

    public HelpEntry(String command, String description, String plugin, String[] permissions) {
        this(command, description, plugin, false, permissions, true);
    }

    public boolean playerCanUse(CommandSender sender) {
        if (this.permissions == null || this.permissions.length == 0 || !(sender instanceof Player)) {
            return true;
        }
        
        for (String permission : this.permissions) {
            if (permission.equalsIgnoreCase("OP") && sender.isOp()) {
                return true;
            } else if (HelpPlusPlus.getPermission().permission((Player) sender, permission)) {
                return true;
            }
        }
        return false;
    }

    public String message() {
        ChatColor commandColor = ChatColor.RED;
        ChatColor pluginColor = ChatColor.GREEN;
        ChatColor descriptionColor = ChatColor.WHITE;
        
        return String.format("%s/%s%s : (via %s%s%s) %s%s",
                commandColor.toString(), this.command, ChatColor.WHITE.toString(),
                pluginColor.toString(), this.plugin, ChatColor.WHITE.toString(),
                descriptionColor.toString(), this.description);
    }

    @Override
    public String toString() {
        return String.format("%s/%s%s : %s%s",
                HelpColors.commandColor.toString(),
                this.command, ChatColor.WHITE.toString(),
                HelpColors.descriptionColor.toString(), 
                this.description
        ).replace("[", HelpColors.optCommandBracketColor.toString() + "[").replace("]", "]" + HelpColors.commandColor.toString()).replace(
            "<", HelpColors.reqCommandBracketColor.toString() + "<").replace(">", ">" + HelpColors.commandColor.toString()
        );
    }

    public String chatString() {
        String line = String.format("%s/%s%s : %s",
                HelpColors.commandColor.toString(),
                this.command,
                ChatColor.WHITE.toString(),
                HelpColors.descriptionColor.toString()
        );

        int descriptionSize = MCFontUtils.getStringWidth(description);
        int sizeRemaining = MCFontUtils.getRemainingChatWidth(line);
        
        //TODO: remove debug
//        System.out.println("lineWidth: "+MCFontUtils.getStringWidth(line));
//        System.out.println("descriptionSize: "+descriptionSize);
//        System.out.println("RemainingSize: "+sizeRemaining);
        
        line += MCFontUtils.strPadLeft(description.replace(
                "[", HelpColors.optCommandBracketColor.toString() + "["
        ).replace(
                "]", "]" + HelpColors.descriptionColor.toString()
        ).replace(
                "<", HelpColors.reqCommandBracketColor.toString() + "<"
        ).replace(
                ">", ">" + HelpColors.descriptionColor.toString()
        ), " ", sizeRemaining, false);

        // @TODO: remove debug
        if (HelpPlusPlus.getSettingsBoolean("shortenEntries")) {
//            System.out.println("shortenEntries true: '" + line + "'");
            return MCFontUtils.strCompressChat(line);
        } else if (sizeRemaining > descriptionSize || !HelpPlusPlus.getSettingsBoolean("useWordWrap")) {
//            System.out.println("sizeRemaining > descriptionSize || useWordWrap=false: '" + line + "'");
            return line;
        } else if (HelpPlusPlus.getSettingsBoolean("wordWrapRight")) {
//            System.out.println("wordWrapRight = true: '" + line + "'");
            return MCFontUtils.strWordWrapRightChat(line, 10, " ", ":");
        } else {
//            System.out.println("default called: '" + line + "'");
            return MCFontUtils.strWordWrapChat(line, 10);
        }
    }

    public String consoleString(int width) {
        String line = String.format("%s/%s%s : %s",
                HelpColors.commandColor.toString(), this.command, ChatColor.WHITE.toString(),
                HelpColors.descriptionColor.toString());

        int descriptionSize = MCFontUtils.strLen(this.description);
        int sizeRemaining = width - MCFontUtils.strLen(line);

        line += MCFontUtils.unformattedPadLeft(description.replace(
            "[", HelpColors.optCommandBracketColor.toString() + "["
        ).replace(
            "]", "]" + HelpColors.descriptionColor.toString()
        ).replace(
            "<", HelpColors.reqCommandBracketColor.toString() + "<"
        ).replace(
            ">", ">" + HelpColors.descriptionColor.toString()
        ), " ", sizeRemaining);

        if (HelpPlusPlus.getSettingsBoolean("shortenEntries")) {
            return MCFontUtils.strCompress(line, width);
        } else if (sizeRemaining > descriptionSize || !HelpPlusPlus.getSettingsBoolean("useWordWrap")) {
            return line;
        } else if (HelpPlusPlus.getSettingsBoolean("wordWrapRight")) {
            return MCFontUtils.strWordWrapRight(line, 10, " ", ":", width);
        } else {
            return MCFontUtils.strWordWrap(line, 10, width);
        }
    }

    public void setEntry(HelpEntry newEntry) {
        this.command = newEntry.command;
        this.description = newEntry.description;
        this.plugin = newEntry.plugin;
        this.main = newEntry.main;
        this.permissions = newEntry.permissions;
        this.visible = newEntry.visible;
    }

    public void save(File dataFolder) {
        File folder = new File(dataFolder, this.helpFolder);
        File file = new File(folder, this.plugin + ".yml");//_orig
        
        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                
            }
        }
        
        BetterConfig config = new BetterConfig(file);
        config.load();

        String node = this.command.replace(" ", "");
        
        config.setProperty(node + ".command", this.command);
        config.setProperty(node + ".description", this.description);
        
        //config.setProperty(node + ".plugin", plugin);
        config.setProperty(node + ".main", this.main);
        
        if (this.permissions.length != 0) {
            config.setProperty(node + ".permissions", this.permissions);
        }
        
        config.setProperty(node + ".visible", this.visible);
        config.save();
    }
}
