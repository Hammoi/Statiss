package com.ss.info;

public class GameItem implements Comparable<GameItem>{
	//private int rarity;
	private String name;
	private String lore;


	public GameItem(String name, String lore){
		//this.rarity = rarity;
		this.name = name;
		this.lore = lore;
	}
	//
	//	public int getRarity() {
	//		return rarity;
	//	}

	public String getName() {
		return name;
	}

	public String getLore() {
		return lore;
	}
	@Override
	public int compareTo(GameItem otherWeapon) {
		
		return this.getName().compareTo(otherWeapon.getName());
	}

}
