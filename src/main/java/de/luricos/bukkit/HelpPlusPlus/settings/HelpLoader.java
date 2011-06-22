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

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import de.luricos.bukkit.HelpPlusPlus.HelpCoreManager;
import de.luricos.bukkit.HelpPlusPlus.utils.HelpLogger;
import de.luricos.bukkit.HelpPlusPlus.utils.YmlFilter;

/**
 * HelpLoader class
 * 
 * @authors tkelly910, jascotty2, lycano
 */
public class HelpLoader {
    @SuppressWarnings("unchecked")
    public static void load(File dataFolder, HelpCoreManager helpCore) {
        File helpFolder = new File(dataFolder, "PluginHelp");
        
        if (!helpFolder.exists()) {
            helpFolder.mkdirs();
        }
        
        File files[] = helpFolder.listFiles(new YmlFilter());
        if (files == null) {
            return;
        }
        
        int count = 0;
        String filesLoaded = "";
        for (File insideFile : files) {
            String fileName = insideFile.getName().replaceFirst(".yml$", "");
            final Yaml yaml = new Yaml(new SafeConstructor());
            Map<String, Object> root;
            FileInputStream input = null;
            
            try {
                input = new FileInputStream(insideFile);
                root = (Map<String, Object>) yaml.load(new UnicodeReader(input));
                if (root == null || root.isEmpty()) {
                    System.out.println("The file " + insideFile + " is empty");
                    continue;
                }
                int num = 0;
                for (String helpKey : root.keySet()) {
                    Map<String, Object> helpNode = (Map<String, Object>) root.get(helpKey);

                    if (!helpNode.containsKey("command")) {
                        HelpLogger.warning("Help entry node \"" + helpKey + "\" is missing a command name in " + insideFile);
                        continue;
                    }
                    
                    String command = helpNode.get("command").toString();
                    if (!helpNode.containsKey("description")) {
                        HelpLogger.warning(command + "'s Help entry is missing a description");
                        continue;
                    }

                    ArrayList<String> permissions = new ArrayList<String>();
                    String description = helpNode.get("description").toString();
                    boolean main = false;
                    boolean visible = true;

                    if (helpNode.containsKey("main")) {
                        if (helpNode.get("main") instanceof Boolean) {
                            main = (Boolean) helpNode.get("main");
                        } else {
                            HelpLogger.warning(command + "'s Help entry has 'main' as a non-boolean. Defaulting to false");
                        }
                    }

                    if (helpNode.containsKey("visible")) {
                        if (helpNode.get("visible") instanceof Boolean) {
                            visible = (Boolean) helpNode.get("visible");
                        } else {
                            HelpLogger.warning(command + "'s Help entry has 'visible' as a non-boolean. Defaulting to true");
                        }
                    }

                    if (helpNode.containsKey("permissions")) {
                        if (helpNode.get("permissions") instanceof List) {
                            for (Object permission : (List) helpNode.get("permissions")) {
                                permissions.add(permission.toString());
                            }
                        } else {
                            permissions.add(helpNode.get("permissions").toString());
                        }
                    }
                    
                    helpCore.customRegisterCommand(command, description, fileName, main, permissions.toArray(new String[0]), visible);
                    num++;
                    count++;
                }
                
                filesLoaded += fileName + String.format("(%d), ", num);
            } catch (Exception ex) {
                HelpLogger.severe("Error!", ex);
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ex) {
                    
                }
            }
        }
        
        //HelpLogger.info(count + " extra help entries loaded" + (filesLoaded.length()>2 ? " from files: " + filesLoaded.replaceFirst(", $", "") : ""));
        HelpLogger.info(count + " extra help entries loaded" + (filesLoaded.length() > 2 ? " from files: " + filesLoaded.substring(0, filesLoaded.length() - 2) : ""));
    }
}
