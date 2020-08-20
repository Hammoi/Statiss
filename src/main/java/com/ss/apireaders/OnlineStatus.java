package com.ss.apireaders;

import com.ss.info.PlayerAPI;

import net.hypixel.api.util.GameType;

public class OnlineStatus {

	private Boolean isOnline;

	public Boolean getOnlineStatus() {

		if(isOnline == null) {
			setOnlineStatus(PlayerAPI.getPlayerSession().isOnline());
		}

		return isOnline;
	}

	public void setOnlineStatus(boolean status) {
		this.isOnline = status;
	}
	
	private GameType gameType;
	
	public GameType getGameType() {
		
		if(gameType == null && getOnlineStatus()) {
			setGameType(PlayerAPI.getPlayerSession().getGameType());
		}
		
		return gameType;
	}
	
	public void setGameType(GameType newType) {
		this.gameType = newType;
	}
	
	private String gameMode;
	
	public String getGameMode() {
		
		if(gameMode == null) {
			setGameMode(PlayerAPI.getPlayerSession().getMode());
		}
		
		return gameMode;
	}
	
	public void setGameMode(String newMode) {
		this.gameMode = newMode;
	}
	
	private String map;
	
	public String getMap() {
		
		if(map == null) {
			setMap(PlayerAPI.getPlayerSession().getMap());
		}
		
		return map;
	}
	
	public void setMap(String newMap) {
		this.map = newMap;
	}
}


