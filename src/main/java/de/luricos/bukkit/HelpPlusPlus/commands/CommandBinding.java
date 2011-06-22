/*
 * HelpPlusPlus - Help pages for smarter people
 * Copyright (C) 2011 lycano <https://github.com/lycano/HelpPlusPlus>
 * Original Credit & Copyright (C) 2011 tkelly910 <https://github.com/tkelly910/Help>
 * 
 * This file is part of PermissionsEx. This is a modified version.
 * 
 * PermissionsEx - Permissions plugin for Bukkit
 * Copyright (C) 2011 t3hk0d3 http://www.tehkode.ru
 *
 * PermissionsEx is free software; you can redistribute it and/or
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

package de.luricos.bukkit.HelpPlusPlus.commands;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class CommandBinding {
    protected Object object;
    protected Method method;
    protected Map<String, String> params = new HashMap<String, String>();

    public CommandBinding(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public Command getMethodAnnotation() {
        return this.method.getAnnotation(Command.class);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void call(Object... args) {
        try {
            this.method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
