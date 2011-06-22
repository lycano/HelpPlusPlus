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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * HelpSearcher class
 * @authors jascotty2, lycano
 */

public class Searcher {

    private HelpCoreManager helpCore;
    private CommandSender player;
    private MatchList matches;
    private String query;

    public Searcher(HelpCoreManager helpCore) {
        this.helpCore = helpCore;
    }

    public void addPlayer(CommandSender player) {
        this.player = player;
    }

    public void setQuery(String name) {
        this.query = name;
        this.matches = helpCore.getMatches(name, player);
    }

    public void search() {
        ChatColor searchColor = ChatColor.YELLOW;

        if (matches.size() == 0) {
            player.sendMessage(ChatColor.RED + "No Help matches for search: " + ChatColor.GRAY + query);
        } else {
            if (matches.commandMatches.size() > 0) {
                player.sendMessage(searchColor.toString() + "Entries with commands similar to: " + ChatColor.GRAY + query);
                for (HelpEntry entry : matches.commandMatches) {
                    player.sendMessage(entry.message());
                }
            }
            if (matches.descriptionMatches.size() > 0) {
                player.sendMessage(searchColor.toString() + "Entries with descriptions similar to: " + ChatColor.GRAY + query);
                for (HelpEntry entry : matches.descriptionMatches) {
                    player.sendMessage(entry.message());
                }
            }
            if (matches.pluginExactMatches.size() > 0) {
                player.sendMessage(searchColor.toString() + "Entries from the plugin: " + ChatColor.GRAY + query);
                for (HelpEntry entry : matches.pluginExactMatches) {
                    player.sendMessage(entry.message());
                }
            } else if (matches.pluginPartialMatches.size() > 0) {
                player.sendMessage(searchColor.toString() + "Entries from plugins similar to: " + ChatColor.GRAY + query);
                for (HelpEntry entry : matches.pluginPartialMatches) {
                    player.sendMessage(entry.message());
                }
            }
        }
    }

}
