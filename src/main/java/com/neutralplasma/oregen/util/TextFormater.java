package com.neutralplasma.oregen.util;

import org.bukkit.ChatColor;

public class TextFormater {

    public static String colorCode(String message){
        return ChatColor.translateAlternateColorCodes('&',  message);
    }
}
