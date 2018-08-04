/*
 * CodexConverter113
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc113;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable() {
        try {
            export();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getCommand("cc113").setExecutor(new MainExecutor(this));
    }
    
    public void export() throws IOException {
        File folder = this.getDataFolder();
        folder.mkdirs();
        File f = new File(folder, "itemsLegacy.json");
        
        if(!f.exists()) {
            f.createNewFile();
            try(InputStream in = Main.class.getResourceAsStream("/itemsLegacy.json")){
                Files.copy(in, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
    
}
