package com.dscalzi.cc112;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.dscalzi.cc112.component.ItemList;
import com.dscalzi.cc112.component.Spigot;
import com.dscalzi.cc112.component.adapter.SpigotTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class TestJSON {
    
    public static void main(String[] args) throws FileNotFoundException {
        testJSON();
    }
    
    public static void testJSON() throws FileNotFoundException {
        
        Gson g = new GsonBuilder().registerTypeAdapter(Spigot.class, new SpigotTypeAdapter()).create();
        
        final File jsonFile = new File(TestJSON.class.getClassLoader().getResource("items.json").getFile());
        JsonReader r = new JsonReader(new FileReader(jsonFile));
        
        ItemList il = g.fromJson(r, ItemList.class);
        
        System.out.println(il.getVersion());
        
        System.out.println("----- NULL VALUES -----");
        
        il.getItems().stream().filter(i -> i.getSpigot() == null).forEach(i -> System.out.println(i.getLegacy().getId() + ":" + i.getLegacy().getData()));
        
        System.out.println("----- END NULL VALUES -----");
        
        il.getItems().removeIf(i -> i.getSpigot() == null);
        
        System.out.println("----- LOADED VALUES -----");
        
        il.getItems().forEach(s -> System.out.println(s.getSpigot().getMaterial()));
        
    }
    
}
