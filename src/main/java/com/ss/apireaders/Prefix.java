package com.ss.apireaders;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonObject;
import com.ss.info.ColorCode;
import com.ss.info.PlayerAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class Prefix {




	private String playerRankString;

	public String getPlayerRankString() {

		if(playerRankString == null) {
			setPlayerRankString(getPlayerRankString(PlayerAPI.getPlayerUUID()));
		}

		return playerRankString;
	}

	public void setPlayerRankString(String newRankString) {
		this.playerRankString = newRankString;
	}

	public static String getPlayerRankString(UUID playerUUID) {

		JsonObject playerJson = new JsonObject();
		try {


			playerJson = PlayerAPI.getAPI().getPlayerByUuid(playerUUID).get().getPlayer();
		} catch (ExecutionException e) {

			PlayerAPI.badAPI();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		boolean hasPlusColor = false;
		String playerRank = new String();

		try {
			switch(playerJson.get("newPackageRank").getAsString()) {
			case "NONE":
				playerRank = "§7";
				break;
			case "VIP":
				playerRank = "§a[VIP] ";
				break;
			case "VIP_PLUS":
				playerRank = "§a[VIP§6+§a] ";
				break;
			case "MVP":
				playerRank = "§b[MVP] ";
			case "MVP_PLUS":
				hasPlusColor = true;
				playerRank = "§b[MVP§c+§b] ";
				break;
			default:
				playerRank = playerJson.get("newPackageRank").getAsString();
				break;
			}


		} catch(NullPointerException e) {
			try {
				switch(playerJson.get("packageRank").getAsString()) {
				case "VIP":
					playerRank = "§a[VIP] ";
					break;
				case "VIP_PLUS":
					playerRank = "§a[VIP§6+§a] ";
					break;
				case "MVP":
					playerRank = "§b[MVP] ";
				case "MVP_PLUS":
					hasPlusColor = true;
					playerRank = "§b[MVP§c+§b] ";
					break;
				default:
					playerRank = playerJson.get("packageRank").getAsString();
					break;
				}
			} catch(NullPointerException npe) {
				if(!playerJson.has("newPackageRank")
						&& !playerJson.has("monthlyPackageRank") 
						&& !playerJson.has("prefix")) {
					playerRank = "§7";
				}
			}
		}

		if(playerJson.has("monthlyPackageRank")) {
			if(playerJson.get("monthlyPackageRank").getAsString().equals("SUPERSTAR")) {
				hasPlusColor = true;
				playerRank = "§6[MVP§c++§6] ";
			}
		}

		if(playerJson.has("rank")) {
			hasPlusColor = false;

			switch(PlayerAPI.getJsonAPI().get("rank").getAsString()) {
			case "YOUTUBER":
				playerRank = "§c[§fYOUTUBE§c] ";
				break;
			case "HELPER":
				playerRank = "§9[HELPER] ";
				break;
			case "MODERATOR":
				playerRank = "§2[MOD] ";
				break;
			case "ADMIN":
				playerRank = "§c[ADMIN] ";
				break;

			}
		}

		if(playerJson.has("prefix")) {
			hasPlusColor = false;
			playerRank = playerJson.get("prefix").getAsString() + " ";

		}


		if(playerJson.has("rankPlusColor") && hasPlusColor) {
			playerRank = playerRank.replaceAll("\\b§c\\b", ColorCode.getColorCode(playerJson.get("rankPlusColor").getAsString()));

		}

		return(playerRank);

	}

}


