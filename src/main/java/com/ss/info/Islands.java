package com.ss.info;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class Islands {
	public static final Hashtable<String, String> islands = new Hashtable<String, String>() {
		{
			put("dynamic", "Private Island");
			put("hub", "Hub");
			put("mining_1", "Gold Mine");
			put("mining_2", "Deep Caverns");
			put("combat_1", "Spider's Den");
			put("combat_2", "Blazing Fortress");
			put("combat_3", "The End");
			put("farming_1", "The Barn");
			put("farming_2", "Mushroom Desert");
			put("foraging_1", "The Park");
			put("winter", "Jerry's Workshop");
			put("dungeon_hub", "Dungeon Hub");
			put("dungeon", "Dungeon");
		}
	};
	
}
