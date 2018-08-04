/*
 * CodexConverter113
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc113;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Material;
import com.dscalzi.cc113.adapter.LegacyEntryTypeAdapter;
import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.ItemList;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.Spigot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ConversionUtil {
    
    public static void convert() throws IOException {
        
        final ItemList legacyList = loadLegacy();
        
        System.out.println("items : " + legacyList.getItems().size());
        
        for(final ItemEntry ie : legacyList.getItems()) {
            
            final Spigot s = ie.getSpigot();
            final Legacy l = ie.getLegacy();
            System.out.println("Loaded: " + l.getId() + ":" + l.getData() + " " + s.getMaterial() + (s.hasPotionData() ? " Potion: " + s.getPotionData().toString() : ""));
        }
        
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        
        try(final JsonWriter writer = g.newJsonWriter(new FileWriter(new File("plugins/CodexConverter113/itemsNew.json")))){
            legacyList.setVersion("1.13");
            g.toJson(g.toJsonTree(legacyList), writer);
        }
        
        
    }
    
    public static ItemList loadLegacy() throws IOException {
        
        final Gson g = new GsonBuilder().registerTypeAdapter(ItemEntry.class, new LegacyEntryTypeAdapter()).create();
        final File jsonFile = new File("plugins/CodexConverter113/itemsLegacy.json");
        try(final JsonReader r = new JsonReader(new FileReader(jsonFile))){
            ItemList il = g.fromJson(r, ItemList.class);
            
            il.getItems().removeIf(i -> i == null);
            
            return il;
        }
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
