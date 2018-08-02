/*
 * CodexConverter112
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc112.component;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionData;

public class Spigot  {

    private Material material;
    private MaterialData legacyData;
    private PotionData potionData;
    
    public Spigot(Material material, MaterialData legacyData) {
        this(material, legacyData, null);
    }
    
    public Spigot(Material material, MaterialData legacyData, PotionData pData) {
        this.material = material;
        this.legacyData = legacyData;
        this.potionData = pData;
    }
    

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public MaterialData getLegacyData() {
        return legacyData;
    }

    public void setLegacyData(MaterialData data) {
        this.legacyData = data;
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
