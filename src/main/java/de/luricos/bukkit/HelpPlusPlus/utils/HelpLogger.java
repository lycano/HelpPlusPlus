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

package de.luricos.bukkit.HelpPlusPlus.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import de.luricos.bukkit.HelpPlusPlus.bukkit.HelpPlusPlus;

/**
 * HelpLogger class - used for logging
 * 
 * @author tkelly910, jascotty2, lycano
 */
public class HelpLogger {
    public static final Logger logger = Logger.getLogger("Minecraft.HelpPlusPlus");

    public static void severe(String string, Exception ex) {
        logger.log(Level.SEVERE, "[" + HelpPlusPlus.getInternalName() + "] " + string, ex);
    }

    public static void severe(String string) {
        logger.log(Level.SEVERE, "[" + HelpPlusPlus.getInternalName() + "] ".concat(string));
    }

    public static void info(String string) {
        logger.log(Level.INFO, "[" + HelpPlusPlus.getInternalName() + "] ".concat(string));
    }

    public static void warning(String string) {
        logger.log(Level.WARNING, "[" + HelpPlusPlus.getInternalName() + "] ".concat(string));
    }
    
    public static void Log(String txt) {
        logger.log(Level.INFO, String.format("[" + HelpPlusPlus.getInternalName() + "] %s", txt));
    }

    public static void Log(Level loglevel, String txt) {
        Log(loglevel, txt, true);
    }

    public static void Log(Level loglevel, String txt, boolean sendReport) {
        logger.log(loglevel, String.format("[" + HelpPlusPlus.getInternalName() + "] %s", txt == null ? "" : txt));
    }

    public static void Log(Level loglevel, String txt, Exception params) {
        if (txt == null) {
            Log(loglevel, params);
        } else {
            logger.log(loglevel, String.format("[" + HelpPlusPlus.getInternalName() + "] %s", txt == null ? "" : txt), (Exception) params);
        }
    }

    public static void Log(Level loglevel, Exception err) {
        logger.log(loglevel, String.format("[" + HelpPlusPlus.getInternalName() + "] %s", err == null ? "? unknown exception ?" : err.getMessage()), err);
    }
}
