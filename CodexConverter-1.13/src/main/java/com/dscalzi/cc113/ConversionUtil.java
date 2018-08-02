/*
 * CodexConverter113
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc113;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.dscalzi.cc113.adapter.LegacyEntryTypeAdapter;
import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.ItemList;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.Spigot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class ConversionUtil {
    
    public static void convert(CommandSender sender) throws FileNotFoundException {
        
        final ItemList legacyList = loadLegacy();
        
        sender.sendMessage("items : " + legacyList.getItems().size());
        
        int i = 0;
        
        for(final ItemEntry ie : legacyList.getItems()) {
            
            final Spigot s = ie.getSpigot();
            final Legacy l = ie.getLegacy();
            
            sender.sendMessage("Loaded: " + l.getId() + ":" + l.getData() + " " + s.getMaterial() + (s.hasBlockData() ? " CLASS: " + s.getData().getClass().getSimpleName() : ""));
            ++i;
        }
        
        sender.sendMessage("looped x" + i);
        
    }
    
    public static ItemList loadLegacy() throws FileNotFoundException {
        
        final Gson g = new GsonBuilder().registerTypeAdapter(ItemEntry.class, new LegacyEntryTypeAdapter()).create();
        final File jsonFile = new File("plugins/CodexConverter113/items.json");
        final JsonReader r = new JsonReader(new FileReader(jsonFile));
        
        ItemList il = g.fromJson(r, ItemList.class);
        
        il.getItems().removeIf(i -> i == null);
        
        return il;
    }
    
    /**
     * Check if the Material should has potion meta.
     * 
     * @param m The Material to test.
     * @return True if the Material should have potion meta, otherwise false.
     */
    public static boolean isPotionable(Material m) {
        return m == Material.POTION || m == Material.SPLASH_POTION || m == Material.LINGERING_POTION || m == Material.TIPPED_ARROW;
    }
    
}
