/*
 * HelpPlusPlus - Help pages for smarter people
 * Copyright (C) 2011 lycano <https://github.com/lycano/HelpPlusPlus>
 * Original Credit & Copyright (C) 2011 tkelly910 <https://github.com/tkelly910/Help>
 * 
 * This file is part of Help (as of May 27, 2011).
 * Modified and forked from tkelly910 by jascotty2.
 * Copyright(C) 2011 jascotty2 <https://github.com/jascotty2/Help>* 
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

package de.luricos.bukkit.HelpPlusPlus.bukkit;

import de.luricos.bukkit.HelpPlusPlus.HelpCoreManager;
import de.luricos.bukkit.HelpPlusPlus.bukkit.commands.HelpCommands;
import de.luricos.bukkit.HelpPlusPlus.bukkit.commands.ImportCommands;
import de.luricos.bukkit.HelpPlusPlus.commands.CommandsManager;
import de.luricos.bukkit.HelpPlusPlus.permissions.PermissionsManager;
import de.luricos.bukkit.HelpPlusPlus.settings.SettingsManager;
import de.luricos.bukkit.HelpPlusPlus.utils.HelpLogger;
import de.luricos.bukkit.HelpPlusPlus.settings.HelpLoader;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * HelpPlusPlus plugin initialization
 * 
 * @authors tkelly910, jsacotty2, lycano
 */
public class HelpPlusPlus extends JavaPlugin {
    protected PluginDescriptionFile pdFile;
    
    protected static String name = "Help++";
    protected static String version = "?";
    protected static String pluginName;
    
    protected SettingsManager settingsManager;
    protected PermissionsManager permissionsManager;
    protected CommandsManager commandsManager;
    protected HelpCoreManager Core;
    
    @Override
    public void onLoad() {
        this.pdFile = this.getDescription();
        
        HelpPlusPlus.version = this.pdFile.getVersion();
        HelpPlusPlus.pluginName = this.pdFile.getName();        
        
        this.permissionsManager = new PermissionsManager(this.getServer());
        this.settingsManager = new SettingsManager(this);
        
        this.commandsManager = new CommandsManager(this);
        this.Core = new HelpCoreManager(this, this.settingsManager);
    }
    
    @Override
    public void onEnable() {       
        this.commandsManager.register(new HelpCommands());
        this.commandsManager.register(new ImportCommands());
        
        HelpLoader.load(this.getDataFolder(), this.Core);
        HelpLogger.Log("v" + version + " enabled");
    }

    @Override
    public void onDisable() {
        HelpLogger.Log("shutting down v"+this.getVersion());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length > 0) {
            return this.commandsManager.execute(sender, command, args);
        }
        
        return HelpPlusPlus.getCoreManager().listHelpPage(sender, null);
    }
    
    public static HelpCoreManager getCoreManager() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(getPluginName());
        if (plugin == null || !(plugin instanceof HelpPlusPlus)) {
            throw new RuntimeException("'" + getInternalName() + "' not found. '" + getInternalName() + "' plugin disabled?");
        }

        return ((HelpPlusPlus) plugin).Core;
    }
    
    public static String getPluginName() {
        return HelpPlusPlus.pluginName;
    }
    
    public static String getVersion() {
        return HelpPlusPlus.version;
    }
    
    public static String getInternalName() {
        return HelpPlusPlus.name;
    }
    
    public boolean reloadSettings(CommandSender sender) {
        if (sender.isOp()) {
            this.settingsManager.initialize();
            this.Core.reload(sender);
            
            return true;
        }
       
        return false;
    }
    
    public static boolean getSettingsBoolean(String key) {
        if ((key == null) || (key.isEmpty())) {
            return false;
        }
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(getPluginName());
        if (plugin == null || !(plugin instanceof HelpPlusPlus)) {
            throw new RuntimeException("'" + getInternalName() + "' not found. '" + getInternalName() + "' plugin disabled?");
        }

        return ((HelpPlusPlus) plugin).settingsManager.getSettingsBoolean(key);        
    }
    
    public static int getSettingsInt(String key) {
        if ((key == null) || (key.isEmpty())) {
            return 0;
        }
        
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(getPluginName());
        if (plugin == null || !(plugin instanceof HelpPlusPlus)) {
            throw new RuntimeException("'" + getInternalName() + "' not found. '" + getInternalName() + "' plugin disabled?");
        }

        return ((HelpPlusPlus) plugin).settingsManager.getSettingsInt(key);        
    }    
    
    public static PermissionsManager getPermission() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(getPluginName());
        if (plugin == null || !(plugin instanceof HelpPlusPlus)) {
            throw new RuntimeException("'" + getInternalName() + "' not found. '" + getInternalName() + "' plugin disabled?");
        }

        return ((HelpPlusPlus) plugin).permissionsManager;        
    }
}
