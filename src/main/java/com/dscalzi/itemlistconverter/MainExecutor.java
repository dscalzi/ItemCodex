/*
 * ItemListConverter
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemlistconverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class MainExecutor implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("convert")) {
				try {
					sender.sendMessage("Converting..");
					ConversionUtil.convert();
					sender.sendMessage(ChatColor.GREEN + "Conversion Completed!");
				} catch (IOException e) {
					sender.sendMessage(ChatColor.RED + "Error during conversion, see console.");
					e.printStackTrace();
				}
				return true;
			} else if(args[0].equalsIgnoreCase("genall")) {
				sender.sendMessage(ChatColor.GREEN + "See console for results.");
				ConversionUtil.generateEveryPotion();
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> ret = new ArrayList<String>();
		
		if(args.length == 1) {
			if("convert".startsWith(args[0].toLowerCase())) ret.add("convert");
			if("genall".startsWith(args[0].toLowerCase())) ret.add("genall");
		}
		
		return ret.size() > 0 ? ret : null;
	}

}
