/*
 * CodexConverter113
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc113;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.dscalzi.itemcodexlib.ItemCodex;

public class MainExecutor implements CommandExecutor, TabCompleter {

    private JavaPlugin plugin;
    
    public MainExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
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
            } else if(args[0].equalsIgnoreCase("test")) {
                this.test();
            }
        }
        
        return false;
    }
    
    public void test() {
        System.out.println("Loading ItemCodex..");
        ItemCodex ic = new ItemCodex(this.plugin);
        System.out.println("Codex loaded");
        
        System.out.println("Getting 1 (Stone)");
        ic.getItem("1").ifPresent(i -> {
            System.out.println("GOT = " + i.getItemStack().getType());
        });
        
        System.out.println("Getting 1:0 (Stone)");
        ic.getItem("1:0").ifPresent(i -> {
            System.out.println("GOT = " + i.getItemStack().getType());
        });
        
        System.out.println("Getting stone (Stone)");
        ic.getItem("stone").ifPresent(i -> {
            System.out.println("GOT = " + i.getItemStack().getType());
        });
        
        System.out.println("Getting fireresistancepot");
        ic.getItem("fireresistancepot").ifPresent(i -> {
            System.out.println("GOT = " + i.getItemStack().getType() + " " + ((PotionMeta) i.getItemStack().getItemMeta()).getBasePotionData().getType());
        });
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ret = new ArrayList<String>();
        
        if(args.length == 1) {
            if("convert".startsWith(args[0].toLowerCase())) ret.add("convert");
            if("test".startsWith(args[0].toLowerCase())) ret.add("test");
        }
        
        return ret.size() > 0 ? ret : null;
    }

}

