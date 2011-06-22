/*
 * HelpPlusPlus - Help pages for smarter people
 * Copyright (C) 2011 lycano <https://github.com/lycano/HelpPlusPlus>
 * Original Credit & Copyright (C) 2011 tkelly910 <https://github.com/tkelly910/Help>
 * 
 * This file is part of Help (as of May 27, 2011).
 * Modified and forked from tkelly910 by jascotty2.
 * Copyright(C) 2011 jascotty2 <https://github.com/jascotty2/Help>
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

package de.luricos.bukkit.HelpPlusPlus.settings;

import de.luricos.bukkit.HelpPlusPlus.bukkit.HelpPlusPlus;
import de.luricos.bukkit.HelpPlusPlus.config.BetterConfig;
import de.luricos.bukkit.HelpPlusPlus.utils.HelpLogger;

import java.io.File;

/**
 * SettingsManager class
 * 
 * @authors tkelly910
 */
public final class SettingsManager {
    private final String settingsFile = "config.yml";
    private File dataFolder;
    
    public boolean allowPluginOverride = false,
                   allowPluginHelp = true,      // if plugins can pass Help custom entries
                   savePluginHelp = false,      // if the help entries registered should be saved
                   sortPluginHelp = true,       // if added entries should also be sorted (by command string)
                   shortenEntries = false,      // entries shown on only one line
                   useWordWrap = true,          // smart(er) word wrapping
                   wordWrapRight = true;        // wrap to the right
    
    public int entriesPerPage = 9;
    
    public SettingsManager() {
        
    }

    public SettingsManager(HelpPlusPlus plugin) {
        this.dataFolder = plugin.getDataFolder();
        
        this.initialize();
    }

    public void initialize() {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            File configFile = new File(dataFolder, settingsFile);
            BetterConfig config = new BetterConfig(configFile);
            
            config.load();

            entriesPerPage = config.getInt("help.list.entries-per-page", entriesPerPage);
            sortPluginHelp = config.getBoolean("help.list.sort-plugin-help", sortPluginHelp);
            shortenEntries = config.getBoolean("help.list.shorten-entries", shortenEntries);
            useWordWrap = config.getBoolean("help.list.use-word-wrap", useWordWrap);
            wordWrapRight = config.getBoolean("help.list.word-wrap-right", wordWrapRight);
            
            allowPluginOverride = config.getBoolean("help.system.allow-plugin-override", allowPluginOverride);
            allowPluginHelp = config.getBoolean("help.system.allow-plugin-help", allowPluginHelp);
            savePluginHelp = config.getBoolean("help.system.save-plugin-help", savePluginHelp);

            config.save();
        } catch (Exception ex) {
            HelpLogger.severe("Error loading configuration", ex);
        }
    }
    
    public boolean getSettingsBoolean(String key) {
        return getSettingsBoolean(key, false);
    }

    public boolean getSettingsBoolean(String key, boolean defaultVal) {
        try {
            boolean aliasValue = this.getClass().getField(key).getBoolean(this);
            return aliasValue;
        } catch (Exception ex) {
        }
        
        return defaultVal;
    }

    public int getSettingsInt(String key){
        return getSettingsInt(key, 0);
    }
    
    public int getSettingsInt(String key, int defaultVal){
        try {
            int aliasValue = this.getClass().getField(key).getInt(this);
            return aliasValue;
        } catch (Exception ex) {
        }
        
        return defaultVal;
    }    
}
