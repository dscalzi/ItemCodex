package com.dscalzi.itemlistconverter.util;

import java.util.List;

public class ItemEntry {

    private Spigot spigot;
    private Legacy legacy;
    private List<String> aliases;
    
    public ItemEntry(Spigot spigot, Legacy legacy, List<String> aliases) {
        
        this.spigot = spigot;
        this.legacy = legacy;
        this.aliases = aliases;
        
    }

    public Spigot getSpigot() {
        return spigot;
    }

    public void setSpigot(Spigot spigot) {
        this.spigot = spigot;
    }

    public Legacy getLegacy() {
        return legacy;
    }

    public void setLegacy(Legacy legacy) {
        this.legacy = legacy;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    
}
