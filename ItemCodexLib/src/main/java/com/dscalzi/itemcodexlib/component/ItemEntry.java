/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

import java.util.List;

public class ItemEntry {

    // These MUST match the fields declared below.
    public static final String KEY_SPIGOT = "spigot";
    public static final String KEY_LEGACY = "legacy";
    public static final String KEY_ALIASES = "aliases";
    
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
