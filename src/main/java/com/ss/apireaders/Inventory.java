package com.ss.apireaders;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ss.info.ColorCode;
import com.ss.info.Decompress;
import com.ss.info.PlayerAPI;
import com.ss.info.Util;
import com.ss.info.GameItem;

public class Inventory {

	private JsonArray inventory;

	public JsonArray getInventory() {

		if(inventory == null && getInventoryAPIStatus()) {
			try {

				String encodedInventory = Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).getAsJsonObject("inv_contents").get("data").getAsString();
				JsonObject inventoryJson = Decompress.base64toJson(encodedInventory);
				setInventory(inventoryJson.getAsJsonObject("value").getAsJsonObject("i").getAsJsonObject("value").getAsJsonArray("list"));

			} catch(NullPointerException e) {}
		}
		return inventory;
	}

	public void setInventory(JsonArray newInventory) {
		this.inventory = newInventory;
	}

	private boolean hasInventoryAPIOn;

	public boolean getInventoryAPIStatus() {
		if(Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).has("inv_contents")) {
			setInventoryAPIStatus(true);
		} else {
			setInventoryAPIStatus(false);
		}

		return hasInventoryAPIOn;
	}

	public void setInventoryAPIStatus(boolean newStatus) {
		this.hasInventoryAPIOn = newStatus;
	}

	private ArrayList<JsonObject> items;

	public ArrayList<JsonObject> getItems() {

		if(items == null) {
			ArrayList<JsonObject> itemList = new ArrayList<>();
			for(JsonElement item : getInventory()) {
				itemList.add(item.getAsJsonObject());
			}

			setItems(itemList);
		}

		return items;
	}

	public void setItems(ArrayList<JsonObject> newItems) {
		items = newItems;
	}




	private String[] weaponsList;

	public String[] getWeaponsList() {
		if (weaponsList == null) {

		}

		return weaponsList;
	}


	public void setWeaponsList(String[] newList) {
		this.weaponsList = newList;
	}

	private ArrayList<GameItem> gameItems;

	public ArrayList<GameItem> getWeapons() {
		if(gameItems == null) {
			if(getInventoryAPIStatus()) {

				ArrayList<GameItem> weaponArray = new ArrayList<>();

				ListMultimap<Integer, GameItem> weaponList = ArrayListMultimap.create();

				for(int x=0; x<getInventory().size();x++) {

					JsonArray loreArray = new JsonArray();
					String name = new String();
					try {
						name = getInventory().
								get(x).getAsJsonObject().
								getAsJsonObject("tag").
								getAsJsonObject("value").
								getAsJsonObject("display").
								getAsJsonObject("value").
								getAsJsonObject("Name").
								get("value").getAsString();
					} catch(NullPointerException e) {
						name = null;
					}

					try{ 
						loreArray = getInventory().
								get(x).getAsJsonObject()
								.getAsJsonObject("tag").
								getAsJsonObject("value").
								getAsJsonObject("display").
								getAsJsonObject("value").
								getAsJsonObject("Lore").
								getAsJsonObject("value").
								getAsJsonArray("list");

					} catch(NullPointerException e) {
						loreArray = null;
					}

					if(loreArray != null) {
						String lastLine = loreArray.get(loreArray.size() - 1).getAsString();
						if(lastLine.contains("SWORD") || lastLine.contains("BOW")) {
							
							Integer anvilUses = null;

							if(getInventory().get(x).getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").has("anvil_uses")) {
								anvilUses = getInventory().get(x).getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").getAsJsonObject("anvil_uses").get("value").getAsInt();
								if(getInventory().get(x).getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").has("hot_potato_count")) {
									anvilUses -= getInventory().get(x).getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").getAsJsonObject("hot_potato_count").get("value").getAsInt();
								}
							}
	
							StringBuilder lore = new StringBuilder(name + "\n" + Util.arrayToString(loreArray, anvilUses));				

							weaponList.put(getRarity(lastLine), new GameItem(name, lore.toString()));
						}
					} else {
						if(name != null ) {
							weaponList.put(0, new GameItem(name, null));
						}
					}



				}
				weaponList.keySet().forEach((key) -> {
					Collections.sort(weaponList.get(key));
					Collections.reverse(weaponList.get(key));
					for(int w = 0; w < weaponList.get(key).size(); w++) {
						weaponArray.add(weaponList.get(key).get(w));
					}
				});


				Collections.reverse(weaponArray);
				setWeapons(weaponArray);

				//setWeaponsList(weaponNamesString.toString());

			} else {
				setWeapons(null);
			}

		}
		return gameItems;
	}

	public void setWeapons(ArrayList<GameItem> newWeapons) {
		gameItems = newWeapons;
	}

	private int getRarity(String description) {


		if(description.contains("COMMON") && !description.contains("UNCOMMON")) {
			return 0;
		} else if(description.contains("COMMON")) {
			return 1;
		} else if(description.contains("RARE")) {
			return 2;
		} else if(description.contains("EPIC")) {
			return 3;
		} else if(description.contains("LEGENDARY")) {
			return 4;
		} else if(description.contains("MYTHIC")) {
			return 5;
		} else if(description.contains("SPECIAL") && !description.contains("VERY")) {
			return 6;
		} else if(description.contains("VERY SPECIAL")) {
			return 7;
		} else {
			return -1; //If this happens, something screwed up.
		}
	}

	private ArrayList<String> uniqueTalismanNames;

	public ArrayList<String> getUniqueTalismans() {

		if(uniqueTalismanNames == null) {
			ArrayList<String> talismanList = new ArrayList<>();
			for(JsonElement item : getInventory()) {
				JsonArray lore = new JsonArray();
				try {
					lore = item.getAsJsonObject().
							getAsJsonObject("tag").
							getAsJsonObject("value").
							getAsJsonObject("display").
							getAsJsonObject("value").
							getAsJsonObject("Lore").
							getAsJsonObject("value").
							getAsJsonArray("list");
				} catch(NullPointerException e) {}


				if(lore.size() != 0 && lore.get(lore.size()-1).getAsString().contains("ACCESSORY")) {
					try {
						String name = ColorCode.removeColor(item.getAsJsonObject().
								getAsJsonObject("tag").
								getAsJsonObject("value").
								getAsJsonObject("display").
								getAsJsonObject("value").
								getAsJsonObject("Name")
								.get("value").getAsString().replace(WordUtils.capitalize(item.getAsJsonObject().
									getAsJsonObject("tag").
									getAsJsonObject("value").
									getAsJsonObject("ExtraAttributes").
									getAsJsonObject("value").
									getAsJsonObject("modifier").
									get("value").getAsString()), ""));

						if(Talismans.isUnique(name, talismanList)) talismanList.add(name);
					} catch(NullPointerException e) {}
					
				}
				
				
			}
			setUniqueTalismanNames(talismanList);
		}

		return uniqueTalismanNames;
	}

	public void setUniqueTalismanNames(ArrayList<String> newTalismanNames) {
		this.uniqueTalismanNames = newTalismanNames;
	}

}
