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

import java.util.ArrayList;

/*
 * MatchList class
 * 
 * @authors tkelly910, jascotty2, lycano
 */
public class MatchList {

    public ArrayList<HelpEntry> commandMatches;
    public ArrayList<HelpEntry> pluginExactMatches;
    public ArrayList<HelpEntry> pluginPartialMatches;
    public ArrayList<HelpEntry> descriptionMatches;

    public MatchList(ArrayList<HelpEntry> commandMatches, ArrayList<HelpEntry> pluginExactMatches, ArrayList<HelpEntry> pluginPartialMatches, ArrayList<HelpEntry> descriptionMatches) {
        this.commandMatches = commandMatches;
        this.pluginExactMatches = pluginExactMatches;
        this.pluginPartialMatches = pluginPartialMatches;
        this.descriptionMatches = descriptionMatches;
    }

    public int size() {
        return commandMatches.size() + pluginExactMatches.size() + pluginPartialMatches.size() + descriptionMatches.size();
    }
}
