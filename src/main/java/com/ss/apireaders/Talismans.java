package com.ss.apireaders;

import java.util.ArrayList;
import java.util.stream.IntStream;

import org.apache.commons.lang3.text.WordUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ss.info.ColorCode;
import com.ss.info.Decompress;
import com.ss.info.PlayerAPI;
import com.ss.info.TalismanGroups;
import com.ss.info.Util;
import java.util.Arrays;

public class Talismans {

	public static int totalTalismans = 53;

	private JsonArray talismanBag;

	public JsonArray getTalismanBag() {
		if(talismanBag == null) {

			String encodedBag = Profiles.getActiveProfile().
					getAsJsonObject("members").
					getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).
					getAsJsonObject("talisman_bag").
					get("data").getAsString();
			JsonObject bagJson = Decompress.base64toJson(encodedBag);
			setTalismanBag(bagJson.
					getAsJsonObject("value").
					getAsJsonObject("i").
					getAsJsonObject("value").
					getAsJsonArray("list"));

		}

		return talismanBag;
	}

	public void setTalismanBag(JsonArray newBag) {
		this.talismanBag = newBag;
	}

	private ArrayList<String> uniqueTalismans;

	public ArrayList<String> getUniqueTalismans() {

		if(uniqueTalismans == null) {

			ArrayList<String> names = new ArrayList<>();

			for(JsonElement talisman : getTalismanBag()) {
				try {

					String talismanName = ColorCode.removeColor(talisman.getAsJsonObject().
							getAsJsonObject("tag").
							getAsJsonObject("value").
							getAsJsonObject("display").
							getAsJsonObject("value").
							getAsJsonObject("Name")
							.get("value").getAsString().replace(WordUtils.capitalize(talisman.getAsJsonObject().
									getAsJsonObject("tag").
									getAsJsonObject("value").
									getAsJsonObject("ExtraAttributes").
									getAsJsonObject("value").
									getAsJsonObject("modifier").
									get("value").getAsString()), ""));

					String talismanLore = Util.arrayToString(talisman.getAsJsonObject().
							getAsJsonObject("tag").
							getAsJsonObject("value").
							getAsJsonObject("display").
							getAsJsonObject("value").
							getAsJsonObject("Lore").getAsJsonObject("value").getAsJsonArray("list"));
					
									
					if(isUnique(talismanName.toString(), names)) names.add(talismanName.toString());
				} catch(NullPointerException e) {}
			}
			setUniqueTalismans(names);
		}

		return uniqueTalismans;
	}

	public void setUniqueTalismans(ArrayList<String> newTalismans) {
		this.uniqueTalismans = newTalismans;
	}

	public static boolean isUnique(String name, ArrayList<String> group) {
		if(!group.contains(name)) {
			boolean duplicate = false;
			try {
				for(String n : TalismanGroups.getGroup(name).getNames()) {
					if(n != name) {
						if(group.contains(n)) duplicate = true; 
					}
				}
			} catch(NullPointerException e) {
				return true;
			}
			if(!duplicate) {
				return true;
			}
		}
		return false;
	}




}
