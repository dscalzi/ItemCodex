/*
 * ItemListConverter
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemlistconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

public class ConversionUtil {
	
	public static void main(String[] args) {
		try {
			convert();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//fixSplash();
		//generateEveryPotion();
		System.out.println("Done");
		//int metadata = 64 >> 32 | (0 << 5) | (0 << 6) | (0 << 13) | (1 << 14);
		//int target = 16384;//16416;
		//System.out.println("target  " + target + "   got  " + metadata);
	}
	
	/**
	 * Debugging method.
	 * 
	 * Generates every possible potion combination and displays the values and calculated
	 * legacy data value.
	 */
	public static void generateEveryPotion() {
		List<Material> types = new ArrayList<Material>(Arrays.asList(Material.POTION, Material.SPLASH_POTION));
		for(Material m : types) {
			for(PotionType t : PotionType.values()) {
				try {
					ItemStack i = new ItemStack(m);
					PotionMeta meta = (PotionMeta)i.getItemMeta();
					meta.setBasePotionData(new PotionData(t, false, false));
					i.setItemMeta(meta);
					System.out.println("-------- " + m.name() + " " + t.name());
					System.out.println("Normal: " + metaCalculation(i, false));
					if(t.isExtendable()) {
					meta.setBasePotionData(new PotionData(t, true, false));
						i.setItemMeta(meta);
						System.out.println("Extended: " + metaCalculation(i, false));
					}
					if(t.isUpgradeable()) {
						System.out.println("-------- " + m.name() + " " + t.name() + " (II)");
						meta.setBasePotionData(new PotionData(t, false, true));
						i.setItemMeta(meta);
						System.out.println("Normal: " + metaCalculation(i, false));
					}
				} catch (Exception e) {
					//Do nothing
				}
			}
		}
	}
	
	/**
	 * Resolves a potion ItemStack based on the provided legacy data value.
	 * 
	 * @param metaId The legacy data value.
	 * @return The potion which corresponds to the legacy data value.
	 */
	public static ItemStack getPotionFromMeta(int metaId) {
		List<Material> types = new ArrayList<Material>(Arrays.asList(Material.POTION, Material.SPLASH_POTION));
		for(Material m : types) {
			for(PotionType t : PotionType.values()) {
				try {
					ItemStack i = new ItemStack(m);
					PotionMeta meta = (PotionMeta)i.getItemMeta();
					meta.setBasePotionData(new PotionData(t, false, false));
					i.setItemMeta(meta);
					if(metaCalculation(i, false) == metaId) {
						return i;
					}
					if(t.isExtendable()) {
						meta.setBasePotionData(new PotionData(t, true, false));
						i.setItemMeta(meta);
						if(metaCalculation(i, false) == metaId) {
							return i;
						}
					}
					if(t.isUpgradeable()) {
						meta.setBasePotionData(new PotionData(t, false, true));
						i.setItemMeta(meta);
						if(metaCalculation(i, false) == metaId) {
							return i;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Map Essential's potion types to the Spigot API.
	 * 
	 * @param essPotionType The essentials potion type.
	 * @return Spigot PotionType corresponding to the provided value.
	 */
	public static PotionType mapEssentialsToSpigot(String essPotionType) {
		switch(essPotionType) {
		case "REGENERATION":
			return PotionType.REGEN;
		case "SWIFTNESS":
			return PotionType.SPEED;
		case "HEALING":
			return PotionType.INSTANT_HEAL;
		case "STRENGTH":
			return PotionType.STRENGTH;
		case "LEAPING":
			return PotionType.JUMP;
		case "POISON":
			return PotionType.POISON;
		case "FIRE_RESISTANCE":
			return PotionType.FIRE_RESISTANCE;
		case "INVISIBILITY":
			return PotionType.INVISIBILITY;
		case "SLOWNESS":
			return PotionType.SLOWNESS;
		case "WATER_BREATHING":
			return PotionType.WATER_BREATHING;
		case "NIGHT_VISION":
			return PotionType.NIGHT_VISION;
		case "LUCK":
			return PotionType.LUCK;
		case "WEAKNESS":
			return PotionType.WEAKNESS;
		case "HARMING":
			return PotionType.INSTANT_DAMAGE;
		default:
			return PotionType.WATER;
		}
	}
	
	/**
	 * Calculate legacy potion data value.
	 * 
	 * @param i The potion to calculate.
	 * @param dualBit If it is a dualbit (extended + upgraded) potion.
	 * @return The legacy potion data value.
	 */
	public static int metaCalculation(ItemStack i, boolean dualBit) {
		Material type = i.getType();
		if(type != Material.POTION && type != Material.SPLASH_POTION) {
			return -1;
		}
		PotionMeta meta = (PotionMeta)i.getItemMeta();
		PotionData data = meta.getBasePotionData();
		
		int tier = data.isUpgraded() ? 1 : 0;
		int extended = data.isExtended() ? 1 : 0;
		int drinkable = type == Material.POTION ? 1 : 0;
		int splash = type == Material.SPLASH_POTION ? 1 : 0;
		int effect = -1;
		boolean noEffect = false;
		boolean dualBitPoss = false;
		switch(data.getType()) {
		case WATER:
			effect = 0;
			noEffect = true;
			break;
		case AWKWARD:
			effect = 16;
			noEffect = true;
			break;
		case THICK:
			effect = 32;
			noEffect = true;
			break;
		case MUNDANE:
			effect = 64;
			noEffect = true;
			break;
		case UNCRAFTABLE:
			effect = 373;
			noEffect = true;
			break;
		case REGEN:
			effect = 1;
			dualBitPoss = true;
			break;
		case SPEED:
			effect = 2;
			dualBitPoss = true;
			break;
		case FIRE_RESISTANCE:
			effect = 3;
			break;
		case POISON:
			effect = 4;
			dualBitPoss = true;
			break;
		case INSTANT_HEAL:
			effect = 5;
			break;
		case NIGHT_VISION:
			effect = 6;
			break;
		case WEAKNESS:
			effect = 8;
			break;
		case STRENGTH:
			effect = 9;
			dualBitPoss = true;
			break;
		case SLOWNESS:
			effect = 10;
			break;
		case JUMP:
			effect = 11;
			break;
		case INSTANT_DAMAGE:
			effect = 12;
			break;
		case WATER_BREATHING:
			effect = 13;
			break;
		case INVISIBILITY:
			effect = 14;
			break;
		case LUCK:
			effect = -1;
			break;
		}
		if(effect == -1) {
			return -1;
		}
		
		if(noEffect) {
			if(type == Material.SPLASH_POTION) {
				return effect >> 32 | (0 << 5) | (0 << 6) | (0 << 13) | (1 << 14);
			} else {
				return effect;
			}
		}
		
		if(dualBitPoss && tier == 1 && dualBit) {
			return 96 + (effect | (0 << 5) | (extended << 6) | (drinkable << 13) | (splash << 14));
		}
		
		int metadata = effect | (tier << 5) | (extended << 6) | (drinkable << 13) | (splash << 14);
		
		return metadata;
	}
	
	/**
	 * Check if the ItemStack should has potion meta.
	 * 
	 * @param i The ItemStack to test.
	 * @return True if the ItemStack should have potion meta, otherwise false.
	 */
	public static boolean isPotionable(ItemStack i) {
		return i.getType() == Material.POTION || i.getType() == Material.SPLASH_POTION || i.getType() == Material.LINGERING_POTION || i.getType() == Material.TIPPED_ARROW;
	}
	
	/**
	 * Convert Essentials potion identifier into an ItemStack.
	 * 
	 * @param identifierDIRTY The Essentials potion identifier.
	 * @param type The material type of the ItemStack.
	 * @return An ItemStack based on the Essentials identifier.
	 */
	public static ItemStack resolveEssentialsPotion(String identifierDIRTY, Material type) {
		try {
			String identifier = identifierDIRTY.replaceAll("[\\{\\}]", "").split(":")[1].replace("\"", "").toUpperCase();
			boolean isStrong = identifier.startsWith("STRONG");
			boolean isExtended = identifier.startsWith("LONG");
			if(isStrong || isExtended) {
				identifier = identifier.substring(identifier.indexOf("_") + 1);
			}
			ItemStack item = new ItemStack(type);
			PotionMeta m = (PotionMeta)item.getItemMeta();
			m.setBasePotionData(new PotionData(mapEssentialsToSpigot(identifier), isExtended, isStrong));
			item.setItemMeta(m);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Utility function to deal with the since removed dual bit potions.
	 * 
	 * @param metaId The legacy potion data value. 
	 * @param potionMeta The potion meta JsonObject which will be populated.
	 * @return True if the provided data value corresponds to a dualbit potion, otherwise false.
	 */
	public static boolean dualBitException(short metaId, JsonObject potionMeta) {
		
		switch(metaId) {
		case 8289:
		case 16481:
			potionMeta.addProperty("effect", PotionType.REGEN.name());
			potionMeta.addProperty("extended", true);
			potionMeta.addProperty("upgraded", true);
			return true;
		case 8290:
		case 16482:
			potionMeta.addProperty("effect", PotionType.SPEED.name());
			potionMeta.addProperty("extended", true);
			potionMeta.addProperty("upgraded", true);
			return true;
		case 8292:
		case 16484:
			potionMeta.addProperty("effect", PotionType.POISON.name());
			potionMeta.addProperty("extended", true);
			potionMeta.addProperty("upgraded", true);
			return true;
		case 8297:
		case 16489:
			potionMeta.addProperty("effect", PotionType.STRENGTH.name());
			potionMeta.addProperty("extended", true);
			potionMeta.addProperty("upgraded", true);
			return true;
		}
		
		return false;
	}

	/**
	 * Fix the typeId for splash potions in the old items.csv file.
	 * Can be run from the command line (without a server instance running).
	 */
	public static void fixSplash() {
		File file = new File("src/main/resources/items.csv");
		System.out.println(file.getAbsolutePath());
		File target = new File("src/main/resources/itemsNew.csv");
		
		String n = "";
		int lastLine = -50;
		
		try(FileReader reader = new FileReader(file);
			BufferedReader rx = new BufferedReader(reader)){
			
			for (int i = 0; rx.ready(); i++){
				try {
					String line = rx.readLine().trim();
					if (line.startsWith("#")) {
						n += line + "\n";
						continue;
					}
						
					String[] parts = line.split(",");
					if (parts.length < 3) {
						System.out.println("Issue on line " + 1);
						continue;
					}
					
					if(parts[0].toLowerCase().contains("spl") && parts[1].equals("373")) {
						parts[1] = "438";
						n += String.join(",", parts) + "\n";
						lastLine = i;
					} else if(i-lastLine == 1 && parts[0].toLowerCase().startsWith("sp") && parts[1].equals("373")) {
						parts[1] = "438";
						n += String.join(",", parts) + "\n";
						System.out.println(parts[0]);
					} else {
						n += line + "\n";
					}
					
				} catch (Exception ex){
					ex.printStackTrace();
					System.out.println("Error parsing items.csv on line " + i);
				}
			}
			
			Files.write(target.toPath(), n.getBytes());
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Convert the old items.csv into JSON format, added Spigot item data.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static void convert() throws FileNotFoundException, IOException {
		File file = new File("plugins/ItemListConverter/items.csv");
		System.out.println(file.getAbsolutePath());
		File target = new File("plugins/ItemListConverter/items.json");
		
		try(FileReader reader = new FileReader(file);
			BufferedReader rx = new BufferedReader(reader)){
			
			// Create Gson.
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			
			// Root Object
			JsonObject root = new JsonObject();
			root.addProperty("version", Bukkit.getBukkitVersion().split("-")[0]);
			root.addProperty("notice", "If you change this file, it will not be automatically updated after the next release.");
			
			// Root items array.
			JsonArray rootItemsArray = new JsonArray();
			
			// Generic item object.
			JsonObject itemsContainer = new JsonObject();
			// Aliases for said container.
			JsonArray aliases = new JsonArray();
			
			boolean sameItem = true;
			
			//final JsonArray vers = new JsonArray();
			//vers.add(Bukkit.getBukkitVersion().split("-")[0]);
			
			for (int i = 0; rx.ready(); i++){
				try {
					String line = rx.readLine().trim().toLowerCase();
					if (line.startsWith("#")) {
						continue;
					}
						
					String[] parts = line.split(",");
					if (parts.length < 3) {
						System.out.println("Issue on line " + 1);
						continue;
					}
					
					int csvLegacyId = Integer.parseInt(parts[1]);
					short csvLegacyData = Short.parseShort(parts[2]);
					
					ItemStack iStack;
					
					if(csvLegacyData > -128 && csvLegacyData < 127) {
						iStack = new ItemStack(csvLegacyId, 1, csvLegacyData, (byte) csvLegacyData);
					} else {
						iStack = new ItemStack(csvLegacyId, 1, csvLegacyData);
					}
					
					JsonObject spigotWork = null;
					JsonObject legacyWork = null;
					
					boolean sameSpigot = itemsContainer.has("spigot") && ((spigotWork=itemsContainer.get("spigot").getAsJsonObject())!=null && spigotWork.has("material") && spigotWork.get("material").getAsString().equals(iStack.getType().toString()));
					boolean sameLegacy = itemsContainer.has("legacy") && ((legacyWork=itemsContainer.get("legacy").getAsJsonObject())!=null && legacyWork.has("data") && legacyWork.get("data").getAsString().equals(parts[2]));
					boolean notPotion  = !isPotionable(iStack) || parts.length < 4;
					
					if(sameSpigot && sameLegacy && notPotion){
						aliases.add(parts[0]);
						continue;
					}
					
					if(parts.length >= 4 && parts[3].startsWith("*")) {
						aliases.add(parts[0]);
						continue;
					}
					
					// Item parsing is complete, add the container to the root array.
					if(!sameItem) {
						itemsContainer.add("aliases", aliases);
						rootItemsArray.add(itemsContainer);
						aliases = new JsonArray();
						itemsContainer = new JsonObject();
					}
					
					sameItem = false;
					
					// Load spigot data.
					JsonObject spigotObj = new JsonObject();
					spigotObj.addProperty("material", iStack.getType().toString());
					spigotObj.add("data", spigotDataTransform(iStack.getData()));
					
					// Load legacy data.
					JsonObject legacyObj = new JsonObject();
					legacyObj.addProperty("id", csvLegacyId);
					legacyObj.addProperty("data", csvLegacyData);
					
					// Add data to item container.
					itemsContainer.add("spigot", spigotObj);
					itemsContainer.add("legacy", legacyObj);
					
					aliases.add(parts[0]);
					
					// If item has potion meta, add to item container.
					// Replace previously resolved data (empty) with potion data.
					if(isPotionable(iStack)) {
						// Potion Data Object.
						JsonObject potionData = new JsonObject();
						ItemStack potion = null;
						
						// Try to resolve the potion by the legacy data value.
						if(iStack.getType() == Material.POTION || iStack.getType() == Material.SPLASH_POTION) {
							potion = getPotionFromMeta(csvLegacyData);
						}
						
						// Try to resolve the potion by Essential's storage strategy.
						if(potion == null) {
							if(parts.length >= 4) {
								potion = resolveEssentialsPotion(parts[3], iStack.getType());
							} else if(parts.length == 3 && csvLegacyId == 441 && csvLegacyData == 0) {
								// Fix to properly route a basic lingering potion (water).
								ItemStack lingerWaterBottle = new ItemStack(iStack.getType());
								PotionMeta m = (PotionMeta)lingerWaterBottle.getItemMeta();
								m.setBasePotionData(new PotionData(PotionType.WATER, false, false));
								lingerWaterBottle.setItemMeta(m);
								potion = lingerWaterBottle;
							}
						}
						
						if(dualBitException(csvLegacyData, potionData)){
							
						} else if(potion == null) {
							potionData.addProperty("value", "REMOVED_FROM_GAME");
							System.out.println(parts[1] + ":" + parts[2] + " " + parts[0]);
						} else {
							PotionData d = ((PotionMeta)potion.getItemMeta()).getBasePotionData();
							potionData.addProperty("type", d.getType().name());
							potionData.addProperty("extended", d.isExtended());
							potionData.addProperty("upgraded", d.isUpgraded());
						}
						
						potionData.addProperty("class", "org.bukkit.inventory.meta.PotionMeta");
						
						spigotObj.add("data", potionData);
					}
					
					// API Method does not work? Using deprecated solution in spigotDataTransform.
					// If item is a monster egg, we need to resolve the data.
					/*if(iStack.getType() == Material.MONSTER_EGG) {
						
						JsonObject monsterData = new JsonObject();
						
						SpawnEggMeta d = (SpawnEggMeta)iStack.getItemMeta();
						System.out.println(d);
						EntityType t = d.getSpawnedType();
						System.out.println(t);
						String s = t.toString();
						
						monsterData.addProperty("type", s);
						monsterData.addProperty("class", "org.bukkit.inventory.meta.SpawnEggMeta");
						
						spigotObj.add("data", monsterData);
					}*/
					
				} catch (Exception ex){
					ex.printStackTrace();
					System.out.println("Error parsing items.csv on line " + i);
				}
			}
			try(JsonWriter writer = gson.newJsonWriter(new FileWriter(target))){
				root.add("items", rootItemsArray);
				gson.toJson(root, writer);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static JsonObject spigotDataTransform(MaterialData d) {
		
		Class<?> clazz = d.getClass();
		JsonObject data = new JsonObject();
		
		if(clazz == MaterialData.class) {
			return data;
		} else if(clazz == org.bukkit.material.Wood.class) {
			data.addProperty("species", ((org.bukkit.material.Wood)d).getSpecies().toString());
		} else if(clazz == org.bukkit.material.Sapling.class) {
			data.addProperty("species", ((org.bukkit.material.Sapling)d).getSpecies().toString());
		} else if(clazz == org.bukkit.material.Tree.class) {
			data.addProperty("species", ((org.bukkit.material.Tree)d).getSpecies().toString());
		} else if(clazz == org.bukkit.material.Leaves.class) {
			data.addProperty("species", ((org.bukkit.material.Leaves)d).getSpecies().toString());
		} else if(clazz == org.bukkit.material.Sandstone.class) {
			data.addProperty("type", ((org.bukkit.material.Sandstone)d).getType().toString());
		} else if(clazz == org.bukkit.material.LongGrass.class) {
			data.addProperty("species", ((org.bukkit.material.LongGrass)d).getSpecies().toString());
		} else if(clazz == org.bukkit.material.Wool.class) {
			data.addProperty("color", ((org.bukkit.material.Wool)d).getColor().toString());
		} else if(clazz == org.bukkit.material.Step.class) {
			data.addProperty("material", ((org.bukkit.material.Step)d).getMaterial().toString());
		} else if(clazz == org.bukkit.material.MonsterEggs.class) {
			data.addProperty("material", ((org.bukkit.material.MonsterEggs)d).getMaterial().toString());
			//data.addProperty("variant", ((org.bukkit.material.MonsterEggs)d).get);
		} else if(clazz == org.bukkit.material.SmoothBrick.class) {
			data.addProperty("material", ((org.bukkit.material.SmoothBrick)d).getMaterial().toString());
		} else if(clazz == org.bukkit.material.Mushroom.class) {
			data.addProperty("texture", ((org.bukkit.material.Mushroom)d).getBlockTexture().toString());
		} else if(clazz == org.bukkit.material.WoodenStep.class) {
			data.addProperty("species", ((org.bukkit.material.WoodenStep)d).getSpecies().toString());
		} else if(clazz == org.bukkit.material.Coal.class) {
			data.addProperty("type", ((org.bukkit.material.Coal)d).getType().toString());
		} else if(clazz == org.bukkit.material.Dye.class) {
			data.addProperty("color", ((org.bukkit.material.Dye)d).getColor().toString());
		} else if(clazz == org.bukkit.material.SpawnEgg.class) {
			data.addProperty("type", ((org.bukkit.material.SpawnEgg)d).getSpawnedType().toString());
		}
		
		data.addProperty("class", clazz.getName());
		
		// org.bukkit.material.Crops
		// org.bukkit.material.CocoaPlant.class
		
		return data;
	}
	
}
