/*
 * HelpPlusPlus - Help pages for smarter people
 * Copyright (C) 2011 lycano <https://github.com/lycano/HelpPlusPlus>
 * Original Credit & Copyright (C) 2011 tkelly910 <https://github.com/tkelly910/Help>
 * 
 * HelpPlusPlus is free software; you can redistribute it and/or
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

package de.luricos.bukkit.HelpPlusPlus;

import de.luricos.bukkit.HelpPlusPlus.bukkit.HelpPlusPlus;
import de.luricos.bukkit.HelpPlusPlus.list.HelpEntry;
import de.luricos.bukkit.HelpPlusPlus.list.Lister;
import de.luricos.bukkit.HelpPlusPlus.list.MatchList;
import de.luricos.bukkit.HelpPlusPlus.list.Searcher;
import de.luricos.bukkit.HelpPlusPlus.settings.HelpLoader;
import de.luricos.bukkit.HelpPlusPlus.settings.SettingsManager;
import de.luricos.bukkit.HelpPlusPlus.utils.HelpEntryComparator;
import de.luricos.bukkit.HelpPlusPlus.utils.HelpLogger;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * HelpPlusPlus Core methods
 * 
 * @author lycano
 */
public class HelpCoreManager {
    private HashMap<String, HelpEntry> helpEntries = new HashMap<String, HelpEntry>();
    private HashMap<String, ArrayList<HelpEntry>> pluginHelpEntries = new HashMap<String, ArrayList<HelpEntry>>();
    private LinkedList<HelpEntry> savedList = new LinkedList<HelpEntry>();    
    
    private HelpPlusPlus plugin;
    private SettingsManager settings;
    private File dataFolder;
    
    public HelpCoreManager(HelpPlusPlus plugin, SettingsManager settings) {
        this.plugin = plugin;
        this.settings = settings;
        this.dataFolder = plugin.getDataFolder();
        
        HelpLogger.Log(HelpPlusPlus.getInternalName() + " manager loaded. Awaiting orders.");
    }
    
    public boolean isHelpActive() {
        return this.plugin.isEnabled();
    }
    
    //@TODO: remove in released version (debug stuff)
//    public void charWidthTest() {
//        for (int i = 0; i < MCFontUtils.charWidthIndex.length(); i++) {
//            this.registerCommand("help version" + MCFontUtils.charWidthIndex.substring(i, i+1), "l "+MCFontUtils.charWidthIndex.substring(i, i+1), this.plugin, true);
//        }
//    }    
    
    public boolean listHelpPage(CommandSender sender, String pageNumber) {
        return this.listPluginHelp(sender, null, pageNumber);
    }
    
    public boolean listPluginHelp(CommandSender sender, String pluginName, String pageNumber) {
        Lister lister;
        if (pageNumber == null) {
            if (pluginName == null) {
                lister = new Lister(sender);
            } else {
                if (pluginName.equals("?")) {
                    this.listPlugins(sender);
                    return true;
                }
                
                lister = new Lister(pluginName, sender, 1, false);
            }
            
            lister.setPage(1);
        } else {
            lister = new Lister(pluginName, sender, Integer.parseInt(pageNumber));
            int page = Integer.parseInt(pageNumber);
            if (page < 1) {
                sender.sendMessage(ChatColor.RED + "Page number can't be below 1.");
                return true;
            } else if (page > lister.getMaxPages(sender)) {
                sender.sendMessage(ChatColor.RED + "There are only " + lister.getMaxPages(sender) + " pages of help");
                return true;
            }
            lister.setPage(page);
        }

        lister.list();        
        return true;
    }
    
    
    public void listPlugins(CommandSender sender) {
        StringBuilder list = new StringBuilder();
        for (String pluginName : pluginHelpEntries.keySet()) {
            list.append(ChatColor.GREEN.toString());
            list.append(pluginName);
            
            list.append(ChatColor.WHITE.toString());
            list.append(", ");
        }
        
        list.delete(list.length() - 2, list.length());
        sender.sendMessage(ChatColor.AQUA + "Plugins with Help entries:");
        sender.sendMessage(list.toString());
    }
    
    public void importPlugins(CommandSender sender) {
        
    }
    
    public void reloadSettings(CommandSender sender) {
        if (!this.plugin.reloadSettings(sender)) {
            sender.sendMessage(ChatColor.RED + " You don't have permission to do this");
        }
    }
    
    public void searchCommand(CommandSender sender, String cmdLabel) {
        Searcher searcher = new Searcher(HelpPlusPlus.getCoreManager());
        searcher.addPlayer(sender);
        searcher.setQuery(cmdLabel);
        searcher.search();
    }
    
    public int getHelpEntrySize() {
        return helpEntries.size();
    }

    public int getPluginHelpEntrySize(String plugin) {
        if (pluginHelpEntries.containsKey(plugin)) {
            return pluginHelpEntries.get(plugin).size();
        } else {
            return 0;
        }
    }
    
    public double getMaxEntries(CommandSender sender) {
        if (sender instanceof Player) {
            int count = 0;
            
            for (HelpEntry entry : helpEntries.values()) {
                if (entry.playerCanUse((Player) sender) && entry.visible) {
                    ++count;
                }
            }
            
            return count;
        }
        
        return helpEntries.size();
    }

    public ArrayList<HelpEntry> getHelpEntries(CommandSender player, int start, int size) {
        ArrayList<HelpEntry> ret = new ArrayList<HelpEntry>();
        List<String> names = new ArrayList<String>(helpEntries.keySet());
        Collator collator = Collator.getInstance();
        
        collator.setStrength(Collator.SECONDARY);
        Collections.sort(names, collator);

        int currentCount = 0;
        for (String entryName : names) {
            HelpEntry entry = helpEntries.get(entryName);
            if (entry != null && entry.playerCanUse(player) && entry.visible) {
                if (currentCount >= start) {
                    ret.add(entry);
                    if (ret.size() >= size) {
                        break;
                    }
                } else {
                    currentCount++;
                }
            }
        }
        
        return ret;
    }

    public ArrayList<HelpEntry> getHelpEntries(CommandSender player, int start, int size, String plugin) {
        ArrayList<HelpEntry> ret = new ArrayList<HelpEntry>();
        if (pluginHelpEntries.containsKey(plugin)) {
            int currentCount = 0;
            
            for (HelpEntry entry : pluginHelpEntries.get(plugin)) {
                if (entry != null && entry.playerCanUse(player) && entry.visible) {
                    if (currentCount >= start) {
                        ret.add(entry);
                        if (ret.size() >= size) {
                            break;
                        }
                    } else {
                        currentCount++;
                    }
                }
            }
        }
        
        return ret;
    }    
    
    public double getMaxEntries(CommandSender player, String plugin) {
        if (pluginHelpEntries.containsKey(plugin)) {
            if (player instanceof Player) {
                int count = 0;
                for (HelpEntry entry : pluginHelpEntries.get(plugin)) {
                    if (entry.playerCanUse((Player) player) && entry.visible) {
                        ++count;
                    }
                }
                return count;
            }
            
            return pluginHelpEntries.get(plugin).size();
        }
        
        return 0;
    }    
    
    /**
     * gets the correctly-cased name of the given plugin title
     * @param plugin plugin to match
     * @return case matched plugin, or the input string if no match
     */
    public String matchPlugin(String plugin) {
        for (String pluginKey : pluginHelpEntries.keySet()) {
            if (pluginKey.equals(plugin)) {
                return pluginKey;
            }
        }
        
        return plugin;
    }
    
    public String matchUniquePlugin(String plugin) {
        for (String pluginKey : pluginHelpEntries.keySet()) {
            String pluginMatch = pluginKey.toLowerCase();
            if (pluginMatch.contains(plugin.toLowerCase())) {
                return pluginKey;
            }
        }
        
        return plugin;
    }
    
    public MatchList getMatches(String query, CommandSender player) {
        ArrayList<HelpEntry> commandMatches = new ArrayList<HelpEntry>();
        ArrayList<HelpEntry> pluginExactMatches = new ArrayList<HelpEntry>();
        ArrayList<HelpEntry> pluginPartialMatches = new ArrayList<HelpEntry>();
        ArrayList<HelpEntry> descriptionMatches = new ArrayList<HelpEntry>();

        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.SECONDARY);

        List<String> plugins = new ArrayList<String>(pluginHelpEntries.keySet());
        Collections.sort(plugins, collator);

        for (String pluginName : plugins) {
            for (HelpEntry entry : pluginHelpEntries.get(pluginName)) {
                if (entry.playerCanUse(player) && entry.visible) {
                    //TODO Separate word matching
                    if (pluginName.equalsIgnoreCase(query)) {
                        pluginExactMatches.add(entry);
                    } else if (pluginName.toLowerCase().contains(query.toLowerCase())) {
                        pluginPartialMatches.add(entry);
                    }
                    
                    if (entry.description.toLowerCase().contains(query.toLowerCase())) {
                        descriptionMatches.add(entry);
                    }
                    
                    if (entry.command.toLowerCase().contains(query.toLowerCase())) {
                        commandMatches.add(entry);
                    }
                }
            }
        }

        return new MatchList(commandMatches, pluginExactMatches, pluginPartialMatches, descriptionMatches);
    }

    public ArrayList<String> getPluginCommands(String plugin) {
        ArrayList<String> ret = new ArrayList<String>();
        if (pluginHelpEntries.containsKey(plugin)) {
            for (HelpEntry entry : pluginHelpEntries.get(plugin)) {
                if (entry != null) {
                    ret.add(entry.command);
                }
            }
        }
        return ret;
    }

    public void reload(CommandSender sender) {
        helpEntries = new HashMap<String, HelpEntry>();
        pluginHelpEntries = new HashMap<String, ArrayList<HelpEntry>>(); //HashMap<String, HashMap<String, HelpEntry>>();

        HelpLoader.load(this.dataFolder, this);

        for (HelpEntry entry : savedList) {
            if (entry.main && !helpEntries.containsKey(entry.command)) {
                helpEntries.put(entry.command, entry);
            }
            customSaveEntry(entry.plugin, entry, false);
        }

        if (sender != null) {
            sender.sendMessage(ChatColor.AQUA + "Successfully reloaded " + HelpPlusPlus.getInternalName() + " environment");
        }
    }    
    
    
    /**
     * Register a command with a plugin
     * 
     * Use this if you dont need permission check and hide your help entry in main help list
     * 
     * @param command the command string
     * @param description command description
     * @param plugin plugin that this command is for
     * @return if the command was registered in Help
     */
    public boolean registerCommand(String command, String description, Plugin plugin) {
        return this.registerCommand(command, description, plugin, false, new String[0]);
    }

    /**
     * Register a command with a plugin
     * 
     * Use this to if you dont need permission check. main-list help visibility has to be defined
     * 
     * @param command the command string
     * @param description command description
     * @param plugin plugin that this command is for
     * @param main if this command should be listed on the main pages
     * @return boolean
     */
    public boolean registerCommand(String command, String description, Plugin plugin, boolean main) {
        return this.registerCommand(command, description, plugin, main, new String[0]);
    }

    /**
     * Register a command with a plugin
     * 
     * Use this if you want to hide your help entry from main help-list
     * 
     * @param command the command string
     * @param description command description
     * @param plugin plugin that this command is for
     * @param permissions the permission(s) necessary to view this entry
     * @return boolean
     */
    public boolean registerCommand(String command, String description, Plugin plugin, String... permissions) {
        return this.registerCommand(command, description, plugin, false, permissions);
    }

    /**
     * Register a command with a plugin
     * 
     * Use this if you want to save help as custom help (without _orig suffix)
     * 
     * @param command the command string
     * @param description command description
     * @param plugin plugin that this command is for
     * @param main if this command should be listed on the main pages
     * @param permissions the permission(s) necessary to view this entry
     * @return boolean
     */
    public boolean registerCommand(String command, String description, Plugin plugin, boolean main, String... permissions) {
        if ((HelpPlusPlus.getSettingsBoolean("allowPluginHelp") || plugin == this) && plugin != null) {
            if (command != null && description != null) {
                if (!this.settings.allowPluginOverride) {
                    // TODO: check if the command (not plugin:command) is already registered

                }

                String pluginName = plugin.getDescription().getName();
                HelpEntry entry = new HelpEntry(command.trim(), description.trim(), pluginName, main, permissions, true);
                
                if (this.settings.savePluginHelp) {
                    entry.save(this.dataFolder);
                }

                if (main && !helpEntries.containsKey(command)) {
                    helpEntries.put(command, entry);
                }

                savePluginEntry(pluginName, entry);
                sortPluginHelp(pluginName);

                return true;
            }            
        }
        
        return false;
    }    
    
    private void savePluginEntry(String plugin, HelpEntry entry) {
        customSaveEntry(plugin, entry, false);
        int i = savedList.lastIndexOf(entry);
        if (i >= 0) {
            savedList.get(i).setEntry(entry);
        } else {
            savedList.add(entry);
        }
    }

    /**
     * saves a HelpEntry if it does not exist, or if given priority
     * 
     * @param plugin the plugin name
     * @param entry entry to save
     * @param priority if should overwrite the previous entry, if any
     */
    private void customSaveEntry(String plugin, HelpEntry entry, boolean priority) {
        if (plugin != null && entry != null) {
            if (pluginHelpEntries.containsKey(plugin)) {
                ArrayList<String> plgs = getPluginCommands(plugin);
                int i = plgs.indexOf(entry.command);
                
                if (i >= 0) {
                    if (priority) {
                        pluginHelpEntries.get(plugin).get(i).setEntry(entry);
                    }
                } else {
                    pluginHelpEntries.get(plugin).add(entry);
                }
            } else {
                pluginHelpEntries.put(plugin, new ArrayList<HelpEntry>(Arrays.asList(entry)));
            }
        }
    }

    public boolean customRegisterCommand(String command, String description, String plugin, boolean main, String[] permissions, boolean visible) {
        HelpEntry entry = new HelpEntry(command, description, plugin, main, permissions, visible);
        
        if (main) {
            helpEntries.put(command, entry);
        }
        
        customSaveEntry(plugin, entry, true);
        sortPluginHelp(plugin);
        
        return true;
    }

    public void sortPluginHelp(String plugin){
        if (this.settings.sortPluginHelp && plugin != null && pluginHelpEntries.containsKey(plugin)) {
            java.util.Collections.sort(pluginHelpEntries.get(plugin), new HelpEntryComparator());
        }
    } 
}
