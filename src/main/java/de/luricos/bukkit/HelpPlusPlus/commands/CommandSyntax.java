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

package de.luricos.bukkit.HelpPlusPlus.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandSyntax {
    protected String originalSyntax;
    protected String regexp;
    protected List<String> arguments = new LinkedList<String>();

    public CommandSyntax(String syntax) {
        this.originalSyntax = syntax;

        this.regexp = this.prepareSyntaxRegexp(syntax);
    }

    public String getRegexp() {
        return regexp;
    }

    private String prepareSyntaxRegexp(String syntax) {
        String expression = syntax;

        Matcher argMatcher = Pattern.compile("(?:[\\s]+)((\\<|\\[|\\-)([^\\>\\]]+)(?:\\>|\\]))").matcher(expression);
        //Matcher argMatcher = Pattern.compile("(?:[\\s]+)((\\<|\\[)([^\\>\\]]+)(?:\\>|\\]))").matcher(expression);
        //Matcher argMatcher = Pattern.compile("(\\<|\\[)([^\\>\\]]+)(?:\\>|\\])").matcher(expression);
        
        int index = 0;
        while (argMatcher.find()) {
            if (argMatcher.group(2).equals("[")) {
                expression = expression.replace(argMatcher.group(0), "(?:(?:[\\s]+)(\"[^\"]+\"|[^\\s]+))?");
            } else {
                expression = expression.replace(argMatcher.group(1), "(\"[^\"]+\"|[\\S]+)");
            }

            arguments.add(index++, argMatcher.group(3));
        }

        return expression;
    }

    public boolean isMatch(String str) {
        return str.matches(this.regexp);
    }

    public Map<String, String> getMatchedArguments(String str) {
        Map<String, String> matchedArguments = new HashMap<String, String>(this.arguments.size());

        if (this.arguments.size() > 0) {
            Matcher argMatcher = Pattern.compile(this.regexp).matcher(str);

            if (argMatcher.find()) {
                for (int index = 1; index <= argMatcher.groupCount(); index++) {
                    String argumentValue = argMatcher.group(index);
                    if (argumentValue == null || argumentValue.isEmpty()) {
                        continue;
                    }

                    if (argumentValue.startsWith("\"") && argumentValue.endsWith("\"")) { // Trim boundary colons
                        argumentValue = argumentValue.substring(1, argumentValue.length() - 1);
                    }

                    matchedArguments.put(this.arguments.get(index - 1), argumentValue);
                }
            }
        }
        return matchedArguments;
    }
}