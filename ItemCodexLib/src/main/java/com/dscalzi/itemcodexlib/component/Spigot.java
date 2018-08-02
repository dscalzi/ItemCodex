/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import org.bukkit.Material;
import org.bukkit.potion.PotionData;

public class Spigot  {
    
    // These MUST match the declared fields in org.bukkit.potion.PotionData
    public static final String KEY_POTION_TYPE = "type";
    public static final String KEY_POTION_EXTENDED = "extended";
    public static final String KEY_POTION_UPGRADED = "upgraded";
    
    // These MUST match the fields declared below.
    public static final String KEY_MATERIAL = "material";
    public static final String KEY_POTION_DATA = "potionData";
    
    private Material material;
    private PotionData potionData;
    
    public Spigot(Material material) {
        this(material, null);
    }
    
    public Spigot(Material material, PotionData potionData) {
        this.material = material;
        this.potionData = potionData;
    }
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public boolean hasPotionData() {
        return this.potionData != null;
    }
    
    public PotionData getPotionData() {
        return potionData;
    }

    public void setPotionData(PotionData pData) {
        this.potionData = pData;
    }
    
}
