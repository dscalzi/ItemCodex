package com.dscalzi.itemcodexlib;

import org.bukkit.Material;

public class Utility {

    /**
     * Check if the Material should has potion meta.
     * 
     * @param m The Material to test.
     * @return True if the Material should have potion meta, otherwise false.
     */
    public static boolean isPotionable(Material m) {
        return m == Material.POTION || m == Material.SPLASH_POTION || m == Material.LINGERING_POTION || m == Material.TIPPED_ARROW;
    }
    
}
