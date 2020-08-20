package com.ss.apireaders;

import com.ss.info.PlayerAPI;

public class Purse {
	private Double purse;

	public Double getPurse() {

		if(purse == null) {
			try {
				setPurse(Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("coin_purse").getAsDouble());
			} catch(NullPointerException e) {
				setPurse(0);
			}
		}

		return purse;
	}

	public void setPurse(double newPurse) {
		this.purse = newPurse;
	}
}
