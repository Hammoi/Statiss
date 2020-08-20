package com.ss.apireaders;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ss.info.Decompress;
import com.ss.info.PlayerAPI;

public class Armor {
	private JsonArray armor;

	public JsonArray getArmor() {

		if(armor == null) {
			setArmor(Decompress.base64toJson(Profiles.getActiveProfile().
					getAsJsonObject("members").
					getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).
					getAsJsonObject("inv_armor").
					get("data").getAsString()).
					getAsJsonObject("value").
					getAsJsonObject("i").
					getAsJsonObject("value").
					getAsJsonArray("list"));
		}

		return armor;
	}

	public void setArmor(JsonArray newArmor) {
		this.armor = newArmor;
	}

	private JsonObject boots;

	public JsonObject getBoots() {

		if(boots == null) {
			try {
				setBoots(getArmor().get(0).getAsJsonObject());
			} catch (NullPointerException e) {}
		}

		return boots;
	}

	public void setBoots(JsonObject newBoots) {
		this.boots = newBoots;
	}

	private String bootsName;

	public String getBootsName() {

		if(bootsName == null) {
			try {
				setBootsName(getBoots().getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("display").getAsJsonObject("value").getAsJsonObject("Name").get("value").getAsString());
			} catch(NullPointerException e) {
				setBootsName("§8--");
			}

		}

		return bootsName;
	}

	public void setBootsName(String newName) {
		this.bootsName = newName;
	}

	private String bootsLore;

	public String getBootsLore() {

		if(bootsLore == null) {		
			setBootsLore(getArmorLore(getBoots(), getBootsName()));
		}

		return bootsLore;
	}

	public void setBootsLore(String newLore) {
		this.bootsLore = newLore;
	}

	private JsonObject leggings;

	public JsonObject getLeggings() {

		if(leggings == null) {
			setLeggings(getArmor().get(1).getAsJsonObject());
		}

		return leggings;
	}

	public void setLeggings(JsonObject newLeggings) {
		this.leggings = newLeggings;
	}

	private String leggingsName;

	public String getLeggingsName() {

		if(leggingsName == null) {
			try {
				setLeggingsName(getLeggings().getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("display").getAsJsonObject("value").getAsJsonObject("Name").get("value").getAsString());
			} catch(NullPointerException e) {
				setLeggingsName("§8--");
			}

		}

		return leggingsName;
	}

	public void setLeggingsName(String newName) {
		this.leggingsName = newName;
	}


	private String leggingsLore;

	public String getLeggingsLore() {

		if(leggingsLore == null) {
			setLeggingsLore(getArmorLore(getLeggings(), getLeggingsName()));
		}

		return leggingsLore;
	}

	public void setLeggingsLore(String newLore) {
		this.leggingsLore = newLore;
	}


	private JsonObject chestplate;

	public JsonObject getChestplate() {

		if(chestplate == null) {
			try {
				setChestplate(getArmor().get(2).getAsJsonObject());
			} catch (NullPointerException e) {}
		}

		return chestplate;
	}

	public void setChestplate(JsonObject newChestplate) {
		this.chestplate = newChestplate;
	}

	private String chestplateName;

	public String getChestplateName() {

		if(chestplateName == null) {
			try {
				setChestplateName(getChestplate().getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("display").getAsJsonObject("value").getAsJsonObject("Name").get("value").getAsString());
			} catch(NullPointerException e) {
				setChestplateName("§8--");
			}

		}

		return chestplateName;
	}

	public void setChestplateName(String newName) {
		this.chestplateName = newName;
	}

	private String chestplateLore;

	public String getChestplateLore() {

		if(chestplateLore == null) {


			setChestplateLore(getArmorLore(getChestplate(), getChestplateName()));
		}

		return chestplateLore;
	}

	public void setChestplateLore(String newLore) {
		this.chestplateLore = newLore;
	}

	private JsonObject helmet;

	public JsonObject getHelmet() {

		if(helmet == null) {
			try {
				setHelmet(getArmor().get(3).getAsJsonObject());
			} catch (NullPointerException e) {}
		}

		return helmet;
	}

	public void setHelmet(JsonObject newHelmet) {
		this.helmet = newHelmet;
	}

	private String helmetName;

	public String getHelmetName() {

		if(helmetName == null) {
			try {
				setHelmetName(getHelmet().getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("display").getAsJsonObject("value").getAsJsonObject("Name").get("value").getAsString());
			} catch(NullPointerException e) {
				setHelmetName("§8--");
			}

		}

		return helmetName;
	}

	public void setHelmetName(String newName) {
		this.helmetName = newName;
	}

	private String helmetLore;

	public String getHelmetLore() {

		if(helmetLore == null) {

			setHelmetLore(getArmorLore(getHelmet(), getHelmetName()));
		}

		return helmetLore;
	}

	public void setHelmetLore(String newLore) {
		this.helmetLore = newLore;
	}

	private String armorList;

	public String getArmorList() {

		if(armorList == null) {
			StringBuilder armorNames = new StringBuilder();
			for(int i = 3; i > -1; i--) {

				try {
					armorNames.append("   " + getArmor().get(i).getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("display").getAsJsonObject("value").getAsJsonObject("Name").get("value").getAsString());
				} catch(NullPointerException e) {
					armorNames.append("   §8--");
				}

				if(i != 0) {
					armorNames.append("\n");
				}
			}
			setArmorList(armorNames.toString());
		}

		return armorList;
	}

	public void setArmorList(String newArmorList) {
		this.armorList = newArmorList;
	}

	public String getArmorLore(JsonObject armorPiece, String bootsName) {
		JsonArray lore = new JsonArray();
		try {
			lore = armorPiece.
					getAsJsonObject("tag").
					getAsJsonObject("value").
					getAsJsonObject("display").
					getAsJsonObject("value").
					getAsJsonObject("Lore").
					getAsJsonObject("value").
					get("list").getAsJsonArray();
		} catch(NullPointerException e) {
			return null;
		}
		StringBuilder stringLore = new StringBuilder();

		stringLore.append(bootsName + "\n");


		Integer anvilUses = null;

		if(armorPiece.getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").has("anvil_uses")) {
			anvilUses = armorPiece.getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").getAsJsonObject("anvil_uses").get("value").getAsInt();
			if(armorPiece.getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").has("hot_potato_count")) {
				anvilUses -= armorPiece.getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").getAsJsonObject("hot_potato_count").get("value").getAsInt();
			}
		}

		for(int loreLine = 0; loreLine < lore.size(); loreLine++) {
			stringLore.append(lore.get(loreLine).getAsString());
			if(loreLine < lore.size() - 1) {
				stringLore.append("\n");
			}


			if(loreLine == lore.size() - 2 && anvilUses != null) {
				stringLore.append("§7Anvil Uses: §c" + anvilUses + "\n");
			}

		}
		return stringLore.toString();

	}


}
