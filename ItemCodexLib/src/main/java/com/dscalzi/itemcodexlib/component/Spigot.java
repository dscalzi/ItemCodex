package com.dscalzi.itemcodexlib.component;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.potion.PotionData;

public class Spigot  {

    private Material material;
    private BlockData data;
    private PotionData potionData;
    
    public Spigot(Material material) {
        this(material, null);
    }
    
    public Spigot(Material material, BlockData data) {
        this(material, data, null);
    }
    
    public Spigot(Material material, BlockData data, PotionData pData) {
        this.material = material;
        this.data = data;
        this.potionData = pData;
    }
    

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public boolean hasBlockData() {
        return this.material.isBlock();
    }
    
    public BlockData getData() {
        return data;
    }

    public void setData(BlockData data) {
        this.data = data;
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
