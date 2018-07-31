package com.dscalzi.itemlistconverter.util;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionData;

public class Spigot  {

    private Material material;
    private MaterialData data;
    private PotionData pData;
    
    public Spigot(Material material, MaterialData data) {
        
        this.material = material;
        this.data = data;
        
    }
    
    public Spigot(Material material, MaterialData data, PotionData pData) {
        
        this(material, data);
        this.pData = pData;
        
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public MaterialData getData() {
        return data;
    }

    public void setData(MaterialData data) {
        this.data = data;
    }

    public boolean hasPotionData() {
        return this.pData != null;
    }
    
    public PotionData getPotionData() {
        return pData;
    }

    public void setPotionData(PotionData pData) {
        this.pData = pData;
    }
    
}
