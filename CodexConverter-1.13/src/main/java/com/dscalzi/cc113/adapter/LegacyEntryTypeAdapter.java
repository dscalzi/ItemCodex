/*
 * CodexConverter113
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc113.adapter;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.dscalzi.cc113.ConversionUtil;
import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.dscalzi.itemcodexlib.component.Legacy;
import com.dscalzi.itemcodexlib.component.Spigot;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class LegacyEntryTypeAdapter implements JsonDeserializer<ItemEntry> {

    private static final List<Short> REMOVED = Arrays.asList(
            (short) 62, // BURNING_FURNACE
            (short) 75 // REDSTONE_TORCH_OFF
            );
    
    @SuppressWarnings("deprecation")
    @Override
    public ItemEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        Gson g = new Gson();
        JsonObject ob = json.getAsJsonObject();
        
        Legacy l = g.fromJson(ob.get("legacy"), Legacy.class);
        
        if(REMOVED.contains(l.getId())) {
            System.out.println("SKIPPING REMOVED " + l.getId() + ":" + l.getData());
            return null;
        }
        
        List<String> aliases = g.fromJson(ob.get("aliases"), new TypeToken<List<String>>(){}.getType());
        JsonObject sp = ob.get("spigot").getAsJsonObject();
        
        if(sp.has("data")) {
            JsonObject data = sp.get("data").getAsJsonObject();
            if(data.has("value") && data.get("value").getAsString().equals("REMOVED_FROM_GAME")) {
                System.out.println("SKIPPING REMOVED POTION " + l.getId() + ":" + l.getData());
                return null;
            }
        }
        
        final Material leg = Material.valueOf(Material.LEGACY_PREFIX + sp.get("material").getAsString());
        BlockData d = null;
        Material m = null;
        PotionData potionData = null;
        
        if(!leg.isBlock()) {
            m = Bukkit.getServer().getUnsafe().fromLegacy(leg);
            if(m.isBlock()) {
                d = Bukkit.getServer().getUnsafe().fromLegacy(leg, (byte)l.getData());
                m = d.getMaterial();
            } else {
                if(ConversionUtil.isPotionable(m)) {
                    JsonObject pD = sp.get("data").getAsJsonObject();
                    boolean extended = pD.get("extended").getAsBoolean(), upgraded = pD.get("upgraded").getAsBoolean();
                    if(extended && upgraded) {
                        // Not allowed anymore
                        System.out.println("SKIPPING DUALBIT " + l.getId() + ":" + l.getData());
                        return null;
                    }
                    potionData = new PotionData(PotionType.valueOf(pD.get("type").getAsString()), extended, upgraded);
                }
            }
        } else {
            d = Bukkit.getServer().getUnsafe().fromLegacy(leg, (byte)l.getData());
            m = d.getMaterial();
        }
        
        System.out.println("PROCESSING:" + l.getId() + ":" + l.getData() + " NEW: " + m + " OLD: " + leg);
        
        Spigot s = new Spigot(m, potionData);
        return new ItemEntry(s, l, aliases);
    }
    
}
