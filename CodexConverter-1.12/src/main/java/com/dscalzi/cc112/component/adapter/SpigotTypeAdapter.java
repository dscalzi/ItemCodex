/*
 * CodexConverter112
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc112.component.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import com.dscalzi.cc112.ConversionUtil;
import com.dscalzi.cc112.component.Spigot;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

@SuppressWarnings("deprecation")
public class SpigotTypeAdapter implements JsonDeserializer<Spigot> {

    private static MaterialData spigotDataTransform(Material m, JsonObject data) throws ClassNotFoundException {
        
        Class<?> clazz = Class.forName(data.get("class").getAsString());
        
        if(clazz == MaterialData.class) {
            return new MaterialData(m);
        } else if(clazz == org.bukkit.material.Wood.class) {
            return new org.bukkit.material.Wood(org.bukkit.TreeSpecies.valueOf(data.get("species").getAsString()));
        } else if(clazz == org.bukkit.material.Sapling.class) {
            return new org.bukkit.material.Sapling(org.bukkit.TreeSpecies.valueOf(data.get("species").getAsString()));
        } else if(clazz == org.bukkit.material.Tree.class) {
            return new org.bukkit.material.Tree(org.bukkit.TreeSpecies.valueOf(data.get("species").getAsString()));
        } else if(clazz == org.bukkit.material.Leaves.class) {
            return new org.bukkit.material.Leaves(org.bukkit.TreeSpecies.valueOf(data.get("species").getAsString()));
        } else if(clazz == org.bukkit.material.Sandstone.class) {
            return new org.bukkit.material.Sandstone(org.bukkit.SandstoneType.valueOf(data.get("type").getAsString()));
        } else if(clazz == org.bukkit.material.LongGrass.class) {
            return new org.bukkit.material.LongGrass(org.bukkit.GrassSpecies.valueOf(data.get("species").getAsString()));
        } else if(clazz == org.bukkit.material.Wool.class) {
            return new org.bukkit.material.Wool(org.bukkit.DyeColor.valueOf(data.get("color").getAsString()));
        } else if(clazz == org.bukkit.material.Step.class) {
            return new org.bukkit.material.Step(org.bukkit.Material.valueOf(data.get("material").getAsString()));
        } else if(clazz == org.bukkit.material.MonsterEggs.class) {
            return new org.bukkit.material.MonsterEggs(org.bukkit.Material.valueOf(data.get("material").getAsString()));
        } else if(clazz == org.bukkit.material.SmoothBrick.class) {
            return new org.bukkit.material.SmoothBrick(org.bukkit.Material.valueOf(data.get("material").getAsString()));
        } else if(clazz == org.bukkit.material.Mushroom.class) {
            return new org.bukkit.material.Mushroom(m, org.bukkit.material.types.MushroomBlockTexture.valueOf(data.get("texture").getAsString()));
        } else if(clazz == org.bukkit.material.WoodenStep.class) {
            return new org.bukkit.material.WoodenStep(org.bukkit.TreeSpecies.valueOf(data.get("species").getAsString()));
        } else if(clazz == org.bukkit.material.Coal.class) {
            return new org.bukkit.material.Coal(org.bukkit.CoalType.valueOf(data.get("type").getAsString()));
        } else if(clazz == org.bukkit.material.Dye.class) {
            return new org.bukkit.material.Dye(org.bukkit.DyeColor.valueOf(data.get("color").getAsString()));
        } else if(clazz == org.bukkit.material.SpawnEgg.class) {
            return new org.bukkit.material.SpawnEgg(org.bukkit.entity.EntityType.valueOf(data.get("type").getAsString()));
        } else if(clazz == org.bukkit.inventory.meta.PotionMeta.class) {
            return new org.bukkit.material.MaterialData(m);
        } else {
            // Generic Constructors (no data)
            
            try {
                Constructor<?> c;
                MaterialData md;
                if(clazz == org.bukkit.material.Vine.class ||
                   clazz == org.bukkit.material.Gate.class ||
                   clazz == org.bukkit.material.CocoaPlant.class ||
                   clazz == org.bukkit.material.TripwireHook.class) {
                    c = clazz.getConstructor();
                    md = (MaterialData)c.newInstance();
                } else {
                    c = clazz.getConstructor(Material.class);
                    md = (MaterialData)c.newInstance(m);
                }
                return md;
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        
        System.err.println("UNKNOWN CLASS : " + data.get("class").getAsString());
        
        return new MaterialData(m);
        
    }
    
    @Override
    public Spigot deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        
        JsonObject ob = json.getAsJsonObject();
        JsonObject data = ob.getAsJsonObject("data");
        
        if(data.has("value") && data.get("value").getAsString().equals("REMOVED_FROM_GAME")) {
            System.out.println("Skipping Removed Item");
            return null;
        }
        
        Material m = Material.getMaterial(ob.get("material").getAsString());
        MaterialData md = new MaterialData(m);
        
        if(data.size() > 0) {
            try {
                
                md = SpigotTypeAdapter.spigotDataTransform(m, data);
                
                if(ConversionUtil.isPotionable(md.getItemType())) {
                    
                    if(!data.has("type")) {
                        System.out.println("Skipping DualBit Potion");
                        return null;
                    }
                    
                    org.bukkit.potion.PotionData pd = new org.bukkit.potion.PotionData(org.bukkit.potion.PotionType.valueOf(data.get("type").getAsString()),
                            data.get("extended").getAsBoolean(),
                            data.get("upgraded").getAsBoolean());
                    
                    return new Spigot(m, md, pd);
                }
                
            } catch (Throwable t) {
                t.printStackTrace();
                System.err.println("ERROR WHILE PARSING : " + ob.toString());
            }
        }
        
        return new Spigot(m, md);
    }

}
