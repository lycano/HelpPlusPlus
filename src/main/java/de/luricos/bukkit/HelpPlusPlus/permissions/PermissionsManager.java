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

package de.luricos.bukkit.HelpPlusPlus.permissions;

import de.luricos.bukkit.HelpPlusPlus.utils.HelpLogger;

import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * PermissionsManager for Permissions plugin
 * 
 * @authors tkelly910, lycano
 */

public final class PermissionsManager {
    private enum PermissionHandler {
        PERMISSIONS, NONE
    }
    
    private PermissionHandler handler;
    private Plugin permissionPlugin;
    private Server server;
    private String version;
    
    public PermissionsManager(Server server) {
        this.server = server;
        
        this.initialize();
    }
    
    public void initialize() {
        Plugin permissions = this.server.getPluginManager().getPlugin("Permissions");
        
        if (permissions != null) {
            this.permissionPlugin = permissions;
            this.handler = PermissionHandler.PERMISSIONS;
            
            this.version = permissions.getDescription().getVersion();
            
            if (!this.server.getPluginManager().isPluginEnabled(this.permissionPlugin)) {
                HelpLogger.info("Help plugin detected but disabled. Enabling plugin 'Permissions' (v" + this.version + ").");
                this.server.getPluginManager().enablePlugin(this.permissionPlugin);
            }
            
            HelpLogger.info("Permissions enabled using: Permissions v" + version);
        } else {
            this.handler = PermissionHandler.NONE;
            HelpLogger.warning("No suitable permission plugin found.");
        }
    }

    public boolean has(Player player, String permission) {
        return this.permission(player, permission);
    }
    
    public boolean permission(Player player, String permission) {
        switch (this.handler) {
            case PERMISSIONS:
                return ((Permissions) permissionPlugin).getHandler().permission(player, permission);
            case NONE:
                return true;
            default:
                return true;
        }
    }
}