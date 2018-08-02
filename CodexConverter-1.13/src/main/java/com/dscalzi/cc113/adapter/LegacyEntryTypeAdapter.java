package com.dscalzi.cc113.adapter;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

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
            System.out.println("SKIPPING " + l.getId() + ":" + l.getData());
            return null;
        }
        
        List<String> aliases = g.fromJson(ob.get("aliases"), new TypeToken<List<String>>(){}.getType());
        JsonObject sp = ob.get("spigot").getAsJsonObject();
        
        final Material leg = Material.valueOf(Material.LEGACY_PREFIX + sp.get("material").getAsString());
        BlockData d = null;
        Material m = null;
        
        if(!leg.isBlock()) {
            m = Bukkit.getServer().getUnsafe().fromLegacy(leg);
            if(m.isBlock()) {
                d = Bukkit.getServer().getUnsafe().fromLegacy(leg, l.getData());
                m = d.getMaterial();
            }
        } else {
            d = Bukkit.getServer().getUnsafe().fromLegacy(leg, l.getData());
            m = d.getMaterial();
        }
        
        System.out.println("PROCESSING:" + l.getId() + ":" + l.getData() + " NEW: " + m + " OLD: " + leg);
        
        Spigot s = new Spigot(m, d);
        return new ItemEntry(s, l, aliases);
    }
    
}
