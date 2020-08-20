package com.ss.info;

public class GuildLevels {
	private static final int[] guildLevelReqs = {100000, 150000, 250000, 500000, 750000, 1000000, 1250000, 1500000, 2000000, 2500000, 2500000, 2500000, 2500000, 2500000, 3000000};

	public static int getGuildLevel(long guildExp) {

		int level = 0;

		for(int x = 0; x < guildLevelReqs.length; x++) {
			if(guildExp > guildLevelReqs[x]) {
				level++;
				guildExp -= guildLevelReqs[x];
			}
		}

		while(guildExp >= 3000000) {
			level++;
			guildExp -= 3000000;
		}

		return level;
	}

}
