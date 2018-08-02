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
    public Material manualConverter(Material m, short data) {
        if(m == Material.LEGACY_COAL && data == 1) {
            return Material.CHARCOAL;
        } else if(m == Material.LEGACY_GOLDEN_APPLE && data == 1) {
            return Material.ENCHANTED_GOLDEN_APPLE;
        } else if(m == Material.LEGACY_RAW_FISH) {
            switch(data) {
                case 1: return Material.SALMON;
                case 2: return Material.TROPICAL_FISH;
                case 3: return Material.PUFFERFISH;
            }
        } else if(m == Material.LEGACY_COOKED_FISH && data == 1) {
            return Material.COOKED_SALMON;
        } else if(m == Material.LEGACY_INK_SACK) {
            switch(data) {
                case 1: return Material.ROSE_RED;
                case 2: return Material.CACTUS_GREEN;
                case 3: return Material.COCOA_BEANS;
                case 4: return Material.LAPIS_LAZULI;
                case 5: return Material.PURPLE_DYE;
                case 6: return Material.CYAN_DYE;
                case 7: return Material.LIGHT_GRAY_DYE;
                case 8: return Material.GRAY_DYE;
                case 9: return Material.PINK_DYE;
                case 10: return Material.LIME_DYE;
                case 11: return Material.DANDELION_YELLOW;
                case 12: return Material.LIGHT_BLUE_DYE;
                case 13: return Material.MAGENTA_DYE;
                case 14: return Material.ORANGE_DYE;
                case 15: return Material.BONE_MEAL;
            }
        } else if(m == Material.LEGACY_BED) {
            switch(data) {
                case 0: return Material.WHITE_BED;
                case 1: return Material.ORANGE_BED;
                case 2: return Material.MAGENTA_BED;
                case 3: return Material.LIGHT_BLUE_BED;
                case 4: return Material.YELLOW_BED;
                case 5: return Material.LIME_BED;
                case 6: return Material.PINK_BED;
                case 7: return Material.GRAY_BED;
                case 8: return Material.LIGHT_GRAY_BED;
                case 9: return Material.CYAN_BED;
                case 10: return Material.PURPLE_BED;
                case 11: return Material.BLUE_BED;
                case 12: return Material.BROWN_BED;
                case 13: return Material.GREEN_BED;
                case 14: return Material.RED_BED;
                case 15: return Material.BLACK_BED;
            }
        } else if(m == Material.LEGACY_MAP) {
            return null; // Data > 0 removed
        } else if(m == Material.LEGACY_MONSTER_EGG) {
            switch(data) {
                case 50: return Material.CREEPER_SPAWN_EGG;
                case 51: return Material.SKELETON_SPAWN_EGG;
                case 52: return Material.SPIDER_SPAWN_EGG;
                case 53: return null; // Giant spawn egg
                case 54: return Material.ZOMBIE_SPAWN_EGG;
                case 55: return Material.SLIME_SPAWN_EGG;
                case 56: return Material.GHAST_SPAWN_EGG;
                case 57: return Material.ZOMBIE_PIGMAN_SPAWN_EGG;
                case 58: return Material.ENDERMAN_SPAWN_EGG;
                case 59: return Material.CAVE_SPIDER_SPAWN_EGG;
                case 60: return Material.SILVERFISH_SPAWN_EGG;
                case 61: return Material.BLAZE_SPAWN_EGG;
                case 62: return Material.MAGMA_CUBE_SPAWN_EGG;
                case 65: return Material.BAT_SPAWN_EGG;
                case 66: return Material.WITCH_SPAWN_EGG;
                case 67: return Material.ENDERMITE_SPAWN_EGG;
                case 68: return Material.GUARDIAN_SPAWN_EGG;
                case 69: return Material.SHULKER_SPAWN_EGG;
                case 90: return Material.PIG_SPAWN_EGG;
                case 91: return Material.SHEEP_SPAWN_EGG;
                case 92: return Material.COW_SPAWN_EGG;
                case 93: return Material.CHICKEN_SPAWN_EGG;
                case 94: return Material.SQUID_SPAWN_EGG;
                case 95: return Material.WOLF_SPAWN_EGG;
                case 96: return Material.MOOSHROOM_SPAWN_EGG;
                case 97: return null; // Snow Golem egg
                case 98: return Material.OCELOT_SPAWN_EGG;
                case 99: return null; // Iron Golem egg
                case 100: return Material.HORSE_SPAWN_EGG;
                case 101: return Material.RABBIT_SPAWN_EGG;
                case 102: return Material.POLAR_BEAR_SPAWN_EGG;
                case 105: return Material.PARROT_SPAWN_EGG;
                case 120: return Material.VILLAGER_SPAWN_EGG;
            }
        } else if(m == Material.LEGACY_SKULL_ITEM) {
            switch(data) {
                case 1: return Material.WITHER_SKELETON_SKULL;
                case 2: return Material.ZOMBIE_HEAD;
                case 3: return Material.PLAYER_HEAD;
                case 4: return Material.CREEPER_HEAD;
                case 5: return Material.DRAGON_HEAD;
            }
        } else if(m == Material.LEGACY_BANNER) {
            switch(data) {
                case 1: return Material.RED_BANNER;
                case 2: return Material.GREEN_BANNER;
                case 3: return Material.BROWN_BANNER;
                case 4: return Material.BLUE_BANNER;
                case 5: return Material.PURPLE_BANNER;
                case 6: return Material.CYAN_BANNER;
                case 7: return Material.LIGHT_GRAY_BANNER;
                case 8: return Material.GRAY_BANNER;
                case 9: return Material.PINK_BANNER;
                case 10: return Material.LIME_BANNER;
                case 11: return Material.YELLOW_BANNER;
                case 12: return Material.LIGHT_BLUE_BANNER;
                case 13: return Material.MAGENTA_BANNER;
                case 14: return Material.ORANGE_BANNER;
                case 15: return Material.WHITE_BANNER;
            }
        }
        
        return Material.AIR;
    }
    
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
        
        boolean manual = false;
        
        if(!leg.isBlock()) {
            m = Bukkit.getServer().getUnsafe().fromLegacy(leg);
            if((l.getData() != 0 || (l.getData() == 0 && leg == Material.LEGACY_BED)) && !ConversionUtil.isPotionable(m)) {
                m = manualConverter(leg, l.getData());
                System.out.println("MANUALLY RESOLVED " + m + " FROM " + leg + " " + l.getId() + ":" + l.getData());
                manual = true;
                if(m == null) {
                    System.out.println("SKIPPING REMOVED " + l.getId() + ":" + l.getData());
                    return null;
                }
                if(m == Material.AIR) {
                    System.out.println("FAILED CONVERSION " + l.getId() + ":" + l.getData());
                }
            }
            if(m.isBlock() && !manual) {
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
        
        System.out.println("PROCESSED:" + l.getId() + ":" + l.getData() + " NEW: " + m + " OLD: " + leg);
        
        Spigot s = new Spigot(m, potionData);
        return new ItemEntry(s, l, aliases);
    }
    
}
