package com.ss.apireaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.Functions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ss.info.ColorCode;
import com.ss.info.PetLevels;
import com.ss.info.PlayerAPI;

public class Pets {


	private JsonArray petList;

	public JsonArray getPetList() {
		if(petList == null) {
			try {
				setPetList(Profiles.getActiveProfile().
						getAsJsonObject("members").
						getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).
						getAsJsonArray("pets"));
			} catch(NullPointerException e) {
			}
		}
		return petList;
	}

	public void setPetList(JsonArray newPetList) {
		this.petList = newPetList;
	}

	private Boolean hasPetActive;

	public Boolean isPetActive() {

		if(hasPetActive == null) {
			getActivePet();
		} 

		return hasPetActive;
	}

	public void setPetActiveStatus(Boolean newStatus) {
		hasPetActive = newStatus;
	}

	private String petName;

	public String getPetName() {

		if(petName == null) {
			if(isPetActive()) {


				setPetName(ColorCode.itemColor(PetLevels.petRarity(getActivePet().get("tier").getAsString())) + WordUtils.capitalizeFully(getActivePet().get("type").getAsString().replace("_", " ")));
			} else setPetName("§7--");
		}

		return petName;
	}

	public void setPetName(String newName) {
		this.petName = newName;
	}

	private Integer petLevel;

	public int getPetLevel() {

		if(petLevel == null) {
			if(isPetActive()) {
				setPetLevel(PetLevels.petLevel(getActivePet().get("exp").getAsDouble(), PetLevels.petRarity(getActivePet().get("tier").getAsString())));
			}

		}

		return petLevel;
	}

	public void setPetLevel(int newLevel) {
		this.petLevel = newLevel;
	}




	private JsonObject activePet;

	public JsonObject getActivePet() {

		if(activePet == null) {

			try {
				for(int i = 0; i < getPetList().size(); i++) {
					if(getPetList().get(i).getAsJsonObject().get("active").getAsBoolean()) {
						setActivePet(getPetList().get(i).getAsJsonObject());
						setPetActiveStatus(true);
						break;
					}
					if(i == petList.size() - 1) {
						setPetActiveStatus(false);
					}
				}
			} catch (NullPointerException e) {}

		}

		return activePet;
	}

	public void setActivePet(JsonObject newPet) {
		this.activePet = newPet;
	}

	private String fullPetString;

	public String getFullPetString() {
		if(fullPetString == null) {
			if(getActivePet() == null) {
				setFullPetString("§8--");
			} else setFullPetString(getPetName() + " §7[Lv" + getPetLevel() + "]");
		}
		return fullPetString;
	}

	public void setFullPetString(String newString) {
		this.fullPetString = newString;
	}

	private String petsSortedByExp;

	public String getPetsSortedByExp() {
		if(petsSortedByExp == null) {
			if(getPetList() == null) {
				setPetsSortedByExp("");

			} else {
				StringBuilder petsSortedString = new StringBuilder();

				ListMultimap<Double, Pet> petsSorted = ArrayListMultimap.create();


				for(int pet = 0; pet < getPetList().size(); pet++) {

					petsSorted.put(getPetList().get(pet).getAsJsonObject().get("exp").getAsDouble(), 
							new Pet(getPetList().get(pet).getAsJsonObject().get("type").getAsString(), 
									getPetList().get(pet).getAsJsonObject().get("tier").getAsString()));



				}
				Iterator<Double> petsExp = petsSorted.keySet().iterator();
				List<Double> exp = new ArrayList();
				while(petsExp.hasNext()) {

					exp.add(petsExp.next());
				}
				Collections.sort(exp);
				Collections.reverse(exp);
				for(int x=0; x < exp.size(); x++) {
					for(int y=0; y < petsSorted.get(exp.get(x)).size(); y++) {

						petsSortedString.append(ColorCode.itemColor(PetLevels.petRarity(petsSorted.get(exp.get(x)).get(y).getRarity())) + WordUtils.capitalizeFully(petsSorted.get(exp.get(x)).get(y).getName().replace("_", " ")) + " §7[Lv" + PetLevels.petLevel(exp.get(x), PetLevels.petRarity(petsSorted.get(exp.get(x)).get(y).getRarity())) + "]");

						if(x == exp.size() - 1 && y == petsSorted.get(exp.get(x)).size() - 1) break;

						petsSortedString.append("\n");

					}

				}

				setPetsSortedByExp(petsSortedString.toString());
			}
		}

		return petsSortedByExp;
	}

	public void setPetsSortedByExp(String newString) {
		this.petsSortedByExp = newString;
	}




}

class Pet {
	private String name;
	private String rarity;

	public Pet() {

	}

	public Pet(String name, String rarity){
		this.name = name;
		this.rarity = rarity;
	}

	public String getName() {
		return name;
	}

	public String getRarity() {
		return rarity;
	}
}

