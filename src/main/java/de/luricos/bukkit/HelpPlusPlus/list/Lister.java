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

import de.luricos.bukkit.HelpPlusPlus.HelpCoreManager;
import de.luricos.bukkit.HelpPlusPlus.bukkit.HelpPlusPlus;
import de.luricos.bukkit.HelpPlusPlus.utils.MCFontUtils;
import de.luricos.bukkit.HelpPlusPlus.utils.HelpColors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * HelpLister class
 * 
 * @authors tkelly910, jascotty2, lycano
 */
public final class Lister {
    private HelpCoreManager helpCore = HelpPlusPlus.getCoreManager();
    private CommandSender sender;
    private String plugin;
    private int maxPages;
    private int page;
    private ArrayList<HelpEntry> sortedEntries;

    public Lister(String plugin, CommandSender sender, int page, boolean greedy) {
        this.sender = sender;
        this.plugin = (greedy) ? helpCore.matchPlugin(plugin) : helpCore.matchUniquePlugin(plugin);
        setPage(page);        
    }    
    
    public Lister(String plugin, CommandSender sender, int page) {
        this(plugin, sender, page, true);
    }
    
    public Lister(String plugin, CommandSender sender) {
        this(plugin, sender, 1, true);
    }

    public Lister(CommandSender sender) {
        this(null, sender, 1, true);
    }

    public Lister() {
        this(null, null, 1, true);
    }

    public Lister(CommandSender sender, int page) {
        this(null, sender, page, true);
    }

    public Lister(int page) {
        this(null, null, page, true);
    }

    public void setPage(int page) {
        this.page = page;
        int start = (page - 1) * HelpPlusPlus.getSettingsInt("entriesPerPage");
        if (plugin == null) {
            sortedEntries = this.helpCore.getHelpEntries(sender, start, HelpPlusPlus.getSettingsInt("entriesPerPage"));
            maxPages = (int) Math.ceil(this.helpCore.getMaxEntries(sender) / (double) HelpPlusPlus.getSettingsInt("entriesPerPage"));
        } else {
            sortedEntries = this.helpCore.getHelpEntries(sender, start, HelpPlusPlus.getSettingsInt("entriesPerPage"), plugin);
            maxPages = (int) Math.ceil(this.helpCore.getMaxEntries(sender, plugin) / (double) HelpPlusPlus.getSettingsInt("entriesPerPage"));
        }
    }

    public void list() {
        list(sender);
    }

    public void list(CommandSender sender) {
        if (sender != null) {
            if (sender instanceof Player) {
                if (plugin == null) {
                    sender.sendMessage(HelpColors.introDashColor.toString()
                            + MCFontUtils.strPadCenterChat(HelpColors.introTextColor.toString()
                            + " HELP (" + page + "/" + maxPages + ") " + HelpColors.introDashColor.toString(), "-"));
                } else {
                    if (sortedEntries.isEmpty()) {
                        sender.sendMessage(ChatColor.RED.toString() + plugin + " has no Help entries");
                    } else {
                        sender.sendMessage(HelpColors.introDashColor.toString()
                                + MCFontUtils.strPadCenterChat(HelpColors.introTextColor.toString()
                                + " " + plugin.toUpperCase() + " HELP (" + page + "/" + maxPages + ") " + HelpColors.introDashColor.toString(), "-"));
                    }
                }

                for (HelpEntry entry : sortedEntries) {
                    for (String line : entry.chatString().split("\\r?\\n")) {
                        sender.sendMessage(line);
                    }
                }
            } else {
                int width = System.getProperty("os.name").startsWith("Windows") ? 80 - 17 : 90;
                if (plugin == null) {
                    sender.sendMessage(HelpColors.introDashColor.toString() + MCFontUtils.unformattedPadCenter(
                            HelpColors.introTextColor.toString() + " HELP (" + page + "/" + maxPages + ") " + HelpColors.introDashColor.toString(), "-", width));
                } else {
                    if (sortedEntries.isEmpty()) {
                        sender.sendMessage(ChatColor.RED.toString() + plugin + " has no Help entries");
                    } else {
                        sender.sendMessage(HelpColors.introDashColor.toString() + MCFontUtils.unformattedPadCenter(
                                HelpColors.introTextColor.toString() + " " + plugin.toUpperCase() + " HELP (" + page + "/" + maxPages + ") " + HelpColors.introDashColor.toString(), "-", width));
                    }
                }

                for (HelpEntry entry : sortedEntries) {
                    for (String l : entry.consoleString(width).split("\\r?\\n")) {
                        sender.sendMessage(l);
                    }
                }
            }
        }
    }

    public int getMaxPages() {
        return getMaxPages(this.sender);
    }

    public int getMaxPages(CommandSender sender) {
        if (this.plugin == null) {
            return (int) Math.ceil(this.helpCore.getMaxEntries(sender) / (double) HelpPlusPlus.getSettingsInt("entriesPerPage"));
        } else {
            return (int) Math.ceil(this.helpCore.getMaxEntries(sender, plugin) / (double) HelpPlusPlus.getSettingsInt("entriesPerPage"));
        }
    }
}
