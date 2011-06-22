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

package de.luricos.bukkit.HelpPlusPlus.utils;

import de.luricos.bukkit.HelpPlusPlus.list.HelpEntry;

import java.util.Comparator;

/**
 * EntryComparator - used in HelpCoreManager for sorting entries
 * 
 * @author tkelly910, lycano
 */
public class HelpEntryComparator implements Comparator<HelpEntry> {
    boolean descending = true;

    public HelpEntryComparator() {

    }

    @Override
    public int compare(HelpEntry o1, HelpEntry o2) {
        return o1.command.compareTo(o2.command) * (descending ? 1 : -1);
    }
}
