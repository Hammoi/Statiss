package com.ss.apireaders;

import com.ss.info.PlayerAPI;

public class FairySouls {

	private Integer soulsCollected;

	public Integer getSoulsCollected() {

		if(soulsCollected == null) {
			try {
				setSoulsCollected(Profiles.getActiveProfile().
						getAsJsonObject("members").
						getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).
						get("fairy_souls_collected").getAsInt());
			} catch(NullPointerException e) {
				setSoulsCollected(0);
			}
		}

		return soulsCollected;
	}

	public void setSoulsCollected(int newValue) {
		this.soulsCollected = newValue;
	}

	public static int totalSouls = 209;
}
