/*
 * CodexConverter112
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc112;

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
		this.getCommand("cc112").setExecutor(new MainExecutor());
	}
	
	public void export() throws IOException {
		File folder = this.getDataFolder();
		folder.mkdirs();
		File file1 = new File(folder, "items.csv");
		File file2 = new File(folder, "items.json");
		
		if(!file1.exists()) {
			file1.createNewFile();
			try(InputStream in = Main.class.getResourceAsStream("/items.csv")){
				Files.copy(in, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		if(!file2.exists()) {
			file2.createNewFile();
			try(InputStream in = Main.class.getResourceAsStream("/items.json")){
				Files.copy(in, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
	
}
