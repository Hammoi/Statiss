package com.ss.info;

import java.util.HashMap;
import java.util.Map;

import scala.actors.threadpool.Arrays;

public enum TalismanGroups {
	SPEED("Speed Talisman", "Speed Ring", "Speed Artifact"),
	FEATHER("Feather Talisman", "Feather Ring", "Feather Artifact"),
	PIGGY_BANK("Piggy Bank", "Cracked Piggy Bank", "Broken Piggy Bank"), //???
	POTION_AFFINITY("Potion Affinity Talisman", "Potion Affinity Ring", "Potion Affinity Artifact"),
	HEALING("Healing Talisman", "Healing Ring"),
	SEA_CREATURE("Sea Creature Talisman", "Sea Creature Ring", "Sea Creature Artifact"),
	PERSONAL_COMPACTOR("Personal Compactor 4000", "Personal Compactor 5000", "Personal Compactor 6000"),
	ZOMBIE("Zombie Talisman", "Zombie Ring", "Zombie Artifact"),
	INTIMIDATION("Intimidation Talisman", "Intimidation Ring", "Intimidation Artifact"),
	BAT("Bat Talisman", "Bat Ring", "Bat Artifact"),
	CANDY("Candy Talisman", "Candy Ring", "Candy Artifact"),
	SPIDER("Spider Talisman", "Spider Ring", "Spider Artifact"),
	WOLF("Wolf Talisman", "Wolf Ring"),
	RED_CLAW("Red Claw Talisman", "Red Claw Ring", "Red Claw Artifact"),
	HUNTER("Hunter Talisman", "Hunter Ring"),
	CAMPFIRE("Campfire Initiate Badge", "Campfire Adept Badge", "Campfire Cultist Badge", "Campfire Scion Badge", "Campfire God Badge"),
	LOVE("Shiny Yellow Rock", "Yellow Rock of Love", "Mediocre Ring of Love", "Rubbish Ring of Love", "Modest Ring of Love", "Refined Ring of Love", "Classy Ring of Love", "Exquisite Ring of Love", "Invaluable Ring of Love", "Legendary Ring of Love"),
	SEAL("Shady Ring", "Crooked Artifact", "Seal of the Family"),
	CHEETAH("Cat Talisman", "Lynx Talisman", "Cheetah Talisman"),
	SCARF("Scarf's Studies", "Scarf's Thesis", "Scarf's Grimoire"),
	TREASURE("Treasure Talisman", "Treasure Ring", "Treasure Artifact");


	private final String[] names;

	private static final Map<String, TalismanGroups> lookup = new HashMap<String, TalismanGroups>();

    static {
        for (TalismanGroups d : TalismanGroups.values()) {
        	for(String n : d.getNames()) {
        		lookup.put(n, d);
        	}
        }
    }
	
	
	private TalismanGroups(String... names) {
		this.names = names;
	}

	public static TalismanGroups getGroup(String name) {
		return lookup.get(name);
	}
	
	
	
	public String[] getNames() {
		return this.names;
	}

}
