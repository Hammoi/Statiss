package com.ss.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.math.BigDecimal;
import com.ss.Config;
import com.ss.apireaders.Armor;
import com.ss.apireaders.Damage;
import com.ss.apireaders.FairySouls;
import com.ss.apireaders.Inventory;
import com.ss.apireaders.OnlineStatus;
import com.ss.apireaders.Pets;
import com.ss.apireaders.PlayerGuild;
import com.ss.apireaders.PlayerInfo;
import com.ss.apireaders.Prefix;
import com.ss.apireaders.Profiles;
import com.ss.apireaders.Purse;
import com.ss.apireaders.Skills;
import com.ss.apireaders.Slayers;
import com.ss.apireaders.Talismans;
import com.ss.info.ColorCode;
import com.ss.info.GuildLevels;
import com.ss.info.Islands;
import com.ss.info.PetLevels;
import com.ss.info.PlayerAPI;
import com.ss.info.GameItem;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.GuildReply.Guild;
import net.hypixel.api.util.GameType;
import net.hypixel.api.util.ILeveling;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NamedTag;
public class SstatsThread implements Runnable{

	Thread ssThread;
	ICommandSender sender;
	EntityPlayer player = (EntityPlayer) sender;
	String[] args;



	public SstatsThread(ICommandSender commandSender, String[] commandArgs) {
		sender = commandSender;
		args = commandArgs;
	}


	private void sendMessage(EntityPlayer player, String string) {
		player.addChatMessage(new ChatComponentTranslation(string));
	}

	private void sendMessage(EntityPlayer player, IChatComponent string) {
		player.addChatMessage(string);
	}

	private void sendMessages(EntityPlayer player, String[] strings) {
		for(int x = 0; x<strings.length; x++) {
			player.addChatMessage(new ChatComponentTranslation(strings[x]));
		}

	}

	private void sendMessages(EntityPlayer player, ArrayList<IChatComponent> strings) {
		for(int x = 0; x<strings.size(); x++) {
			player.addChatMessage(strings.get(x));
		}

	}


	private IChatComponent hoverText(String key, String[] infos, String[] hoverTexts) {
		if(hoverTexts[0] == null) {
			StringBuilder text = new StringBuilder();
			for(int t = 0; t < infos.length; t++) {
				text.append(infos[t]);
			}
			return new ChatComponentTranslation(key + text.toString());
		}
		IChatComponent componentKey = new ChatComponentText(key);
		ArrayList<IChatComponent> componentInfos = new ArrayList<IChatComponent>();
		for(String info : infos) {
			componentInfos.add(new ChatComponentText(info));
		}

		ArrayList<ChatStyle> styles = new ArrayList<ChatStyle>();

		for(String text : hoverTexts) {
			styles.add(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(text))));

		}
		ChatStyle style1 = componentKey.getChatStyle().setParentStyle(null);


		IChatComponent component = componentKey;

		for(int count = 0; count < hoverTexts.length; count++) {
			componentInfos.get(count).setChatStyle(styles.get(count));
			component.appendSibling(componentInfos.get(count));
		}


		return component;
	}

	private IChatComponent clickText(String text, String command) {
		IChatComponent componentText = new ChatComponentText(text);

		StringBuilder clicked = new StringBuilder();



		ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

		componentText.setChatStyle(style);

		return componentText;
	}

	private static Boolean isAPIWorking;

	public static Boolean getAPIStatus() {
		return isAPIWorking;
	}

	public static void setAPIStatus(Boolean newStatus) {
		isAPIWorking = newStatus;
	}

	@Override
	public void run() {

		EntityPlayer player = (EntityPlayer) sender;



		String profileName = null;
		String pageNumber = null;

		for(String arg : args) {
			if(!arg.equals(args[0])) {
				if(arg.matches("-?(0|[1-9]\\d*)")) pageNumber = arg;
				else profileName = arg;
			}
		}


		if(!PlayerAPI.startAPI()) return;

		try {
			PlayerAPI.setPlayerUUID(MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(args[0]).getId());
		} catch(NullPointerException e) {
			sendMessage(player, "§cPlayer not found.");
			setAPIStatus(null);
			PlayerAPI.shutdownAPI();
			return;
		}

		if(PlayerAPI.getJsonAPI() == null) {
			if(getAPIStatus()) {
				sendMessage(player, "§cPlayer " + args[0] + " seemed to have never joined Hypixel.");
			} else {
				if(Config.isValidKey()) {
					sendMessage(player, "§cCouldn't reach the Hypixel API.");
				}

			}
			setAPIStatus(null);
			PlayerAPI.shutdownAPI();
			return;
		}



		if(profileName != null) {
			boolean foundProfile = false;

			for(Entry<String, JsonElement> profile : PlayerAPI.getJsonAPI().getAsJsonObject("stats").getAsJsonObject("SkyBlock").getAsJsonObject("profiles").entrySet()) {
				if(profile.getValue().getAsJsonObject().get("cute_name").getAsString().toLowerCase().equals(profileName.toLowerCase())) {
					try {
						Profiles.setActiveProfile(PlayerAPI.getAPI().getSkyBlockProfile(profile.getKey()).get().getProfile());
						foundProfile = true;
						break;
					} catch (InterruptedException | ExecutionException e) {

					}

				}
			}

			if(!foundProfile) {
				sender.addChatMessage(new ChatComponentTranslation("§cCouldn't find " + args[0] + "'s profile " + profileName + "."));
				return;
			}
		}

		ArrayList<IChatComponent> info = new ArrayList<>();


		Prefix prefix = new Prefix();
		PlayerGuild guild = new PlayerGuild();
		Skills skills = new Skills();

		String playerRank = prefix.getPlayerRankString();




		PlayerInfo playerInfo = new PlayerInfo();
		String playerProfileHover = "§aCharacter Information \n" +
				"§7Rank: " + (prefix.getPlayerRankString().equals("§7") ? "§8Default" : prefix.getPlayerRankString()).replace("[", "").replace("]", "") + "\n" +
				"§7Level: §6" + playerInfo.getNetworkLevel().intValue() + "\n" +
				"§7Experience until next Level: §6" + playerInfo.getExptoNextLevel() + "\n" +
				"§7Achievement Points: §e" + playerInfo.getAchievementPoints() + "\n" +
				"§7Quests Completed: §6" + playerInfo.getQuestsCompleted() + "\n" +
				"§7Karma: §d" + playerInfo.getKarma();

		System.out.println("SS: Grabbed character information.");

		String guildHover = new String();

		if(guild.isInGuild()) {
			guildHover = "§aGuild Information \n" +
					"§7Name: §6" + guild.getName() + "\n" +
					"§7Tag: " + guild.getGuildTag().replace("[", "").replace("]", "") + "\n" + 
					"§7Level: §6" + GuildLevels.getGuildLevel(guild.getExp()) + "\n" +
					"§7Members: §e" + guild.getMemberCount() + "\n" + 
					"§7Guildmaster: " + Prefix.getPlayerRankString(guild.getGuildMasterUUID()) + PlayerAPI.UUIDtoName(guild.getGuildMasterUUID());
			System.out.println("SS: Grabbed player's guild information.");
		} else {
			guildHover = "";
			System.out.println("SS: Didn't grab player's guild information since they're not in one.");
		}

		String fullName = prefix.getPlayerRankString() + PlayerAPI.getJsonAPI().get("displayname").getAsString();

		OnlineStatus status = new OnlineStatus();

		String statusHover = new String();

		try {
			statusHover = "§aPlayer Status \n" + 
					"§7Status: " + (status.getOnlineStatus() ? (status.getGameType().equals(GameType.SKYBLOCK) ? "§aOnline" : "§eNot playing Skyblock") : "§cOffline") + "\n" + 
					"§7Game Type: §e" + WordUtils.capitalize(status.getGameType().getName().toLowerCase()) +
					(status.getGameType().equals(GameType.SKYBLOCK) ? "\n§7Island: §6" + Islands.islands.get(status.getGameMode()) : "");
		} catch(NullPointerException e) {}



		if(PlayerAPI.doesPlaySkyblock()) {
			if(pageNumber == null || pageNumber.equals("1")) {
				Pets pets = new Pets();
				Armor armors = new Armor();
				Inventory inventory = new Inventory();
				Profiles profile = new Profiles();

				StringBuilder playerProfiles = new StringBuilder();

				Profiles.getAllProfiles().forEach((playerProfile) -> {
					if(PlayerAPI.getJsonAPI().getAsJsonObject("stats").getAsJsonObject("SkyBlock").getAsJsonObject("profiles").getAsJsonObject(playerProfile.get("profile_id").getAsString()).has("cute_name")) {

						String cuteName = PlayerAPI.getJsonAPI().
								getAsJsonObject("stats").
								getAsJsonObject("SkyBlock").
								getAsJsonObject("profiles").
								getAsJsonObject(playerProfile.get("profile_id").getAsString()).
								get("cute_name").getAsString();

						if(cuteName != profile.getCuteName()) {
							playerProfiles.append("   §d" + cuteName);

							if(playerProfile != Profiles.getAllProfiles().get(Profiles.getAllProfiles().size()-1)) {

								playerProfiles.append("\n");
							}
						} 
					}

				});


				System.out.println("SS: Grabbed player's skyblock profile names.");


				StringBuilder memberString = new StringBuilder();
				if(profile.getProfileMembers() != null) {
					for(int m = 0; m < profile.getProfileMembers().size(); m++) {
						memberString.append("   " + Prefix.getPlayerRankString(MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(profile.getProfileMembers().get(m)).getId()) + profile.getProfileMembers().get(m) + "\n");

					}
				} else memberString.append("");

				System.out.println("SS: Grabbed player's co-op members.");

				DateTimeFormatter formatter =
						DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
						.withLocale(Locale.US)
						.withZone(ZoneId.systemDefault());



				String profileHover = "§aProfile Information\n" +
						"§7Name: §d" + profile.getCuteName() + "\n" +
						"§7Members:\n" +
						memberString +
						"§7First Joined: §5" + formatter.format(Instant.ofEpochMilli(profile.getDateJoined())) + "\n" +
						"§7Last Joined: §a" + formatter.format(Instant.ofEpochMilli(profile.getLastPlayed())) +
						(playerProfiles.toString().equals("") ? "" : "\n§7Other Profiles:\n" + 
								playerProfiles.toString());

				System.out.println("SS: Grabbed player's skyblock profile information.");

				double skillAverage = Double.parseDouble(String.format(Locale.US, "%.2f", ((double) skills.getMining() +
						skills.getCombat() +
						skills.getForaging() +
						skills.getFishing() +
						skills.getFarming() +
						skills.getAlchemy() +
						skills.getEnchanting() +
						skills.getTaming())/8));



				String skillHover = "§aPlayer Skills \n" + 
						"§7Mining: " + ColorCode.skillColor(skills.getMining()) + "\n" + 
						"§7Combat: " + ColorCode.skillColor(skills.getCombat()) + "\n" + 
						"§7Foraging: " + ColorCode.skillColor(skills.getForaging()) + "\n" + 
						"§7Fishing: " + ColorCode.skillColor(skills.getFishing()) + "\n" + 
						"§7Farming: " + ColorCode.skillColor(skills.getFarming()) + "\n" + 
						"§7Alchemy: " + ColorCode.skillColor(skills.getAlchemy()) + "\n" + 
						"§7Enchanting: " + ColorCode.skillColor(skills.getEnchanting()) + "\n" + 
						"§7Taming: " + ColorCode.skillColor(skills.getTaming()) +
						(skills.getApiStatus() ? "\n\n§7Carpentry: " + ColorCode.skillColor(skills.getCarpentry()) + "\n§7Runescrafting: " + ColorCode.runescraftingColor(skills.getRunescrafting()) : "\n\n§cSkill API Disabled.");

				System.out.println("SS: Grabbed player's skyblock skills information.");


				String petHover = new String();

				if(pets.getPetsSortedByExp().equals("")) {
					petHover = null;
				} else {
					petHover = "§aPets Sorted By Experience \n" +
							pets.getPetsSortedByExp();
				}

				System.out.println("SS: Grabbed player's skyblock pets information.");

				info.addAll(Arrays.asList(new IChatComponent[] {
						new ChatComponentTranslation("§8--------------------------------"),
						hoverText("§8Stats of ", new String[] {fullName + " ", guild.getGuildTag()}, new String[] {playerProfileHover, guildHover}),
						hoverText("§7Status: ", new String[] {(status.getOnlineStatus() ? (status.getGameType().equals(GameType.SKYBLOCK) ? "§aOnline" : "§eNot playing Skyblock") : "§cOffline")}, new String[] {(status.getOnlineStatus() ? statusHover : null)}),
				}));

				try {
					info.add(hoverText("§7Profile: ", new String[] {"§d" + PlayerAPI.getJsonAPI().getAsJsonObject("stats").getAsJsonObject("SkyBlock").getAsJsonObject("profiles").getAsJsonObject(Profiles.getActiveProfile().get("profile_id").getAsString()).get("cute_name").getAsString()}, new String[] {profileHover}));
				} catch(NullPointerException e) {
					info.add(hoverText("§7Profile: ", new String[] {"§cUnknown"}, new String[] {profileHover}));
				}
				info.addAll(Arrays.asList(new IChatComponent[] {	
						hoverText("§7Skill Average: ", new String[] {ColorCode.skillColor(skillAverage)}, new String[] {skillHover}),
						hoverText("§7Active Pet: ", new String[] {pets.getFullPetString()}, new String[] {petHover}),
						new ChatComponentTranslation("§7Equipped Armor:"),
						hoverText("   ", new String[] {armors.getHelmetName()}, new String[] {armors.getHelmetLore()}),
						hoverText("   ", new String[] {armors.getChestplateName()}, new String[] {armors.getChestplateLore()}),
						hoverText("   ", new String[] {armors.getLeggingsName()}, new String[] {armors.getLeggingsLore()}),
						hoverText("   ", new String[] {armors.getBootsName()}, new String[] {armors.getBootsLore()}),
						new ChatComponentTranslation("§7Weapons: "),		
				}));
				if(inventory.getWeapons() != null) {
					for(GameItem gameItem : inventory.getWeapons()) {
						info.add(hoverText("   ", new String[] {gameItem.getName()}, new String[] {gameItem.getLore()}));
					}
				} else info.add(new ChatComponentTranslation("   §cInventory API is disabled."));
				info.add(clickText("§eNext Page", new SStats().getCommandName() + " " + args[0] + (args.length > 1 ? " " + profileName : "") + " 2"));

				info.add(new ChatComponentTranslation("§8--------------------------------"));

				System.out.println("SS: Successfully sent player info.");
			} else {
				switch(pageNumber) {
				case "2":

					Slayers slayer = new Slayers();

					FairySouls souls = new FairySouls();
					Talismans talismans = new Talismans();
					Inventory inventory = new Inventory();

					ArrayList<String> totalUniqueTalismans = new ArrayList<>();

					if(inventory.getInventoryAPIStatus()) {
						totalUniqueTalismans = talismans.getUniqueTalismans();
						for(String talisman : inventory.getUniqueTalismans()) {
							if(Talismans.isUnique(talisman, totalUniqueTalismans)) {
								totalUniqueTalismans.add(talisman);
							}
						}
					}

					Purse purse = new Purse();
					Damage dmg = new Damage();

					StringBuilder revHover = new StringBuilder("§aRevenant Slayer \n" +
							"§7XP: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getRevXp())) + slayer.getRevXp() + "\n" +
							"§8Tiers Slain:");

					for(int s = 0; s < slayer.getRevsSlain().size(); s++) {
						revHover.append("\n   §7T" + (s+1) + "'s: §6" + slayer.getRevsSlain().get(s));
					}

					StringBuilder taranHover = new StringBuilder("§cTarantula Slayer \n" +
							"§7XP: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getTaranXp())) +  slayer.getTaranXp() + "\n" +
							"§8Tiers Slain:");

					for(int s = 0; s < slayer.getTaransSlain().size(); s++) {
						taranHover.append("\n   §7T" + (s+1) + "'s: §6" + slayer.getTaransSlain().get(s));
					}

					StringBuilder svenHover = new StringBuilder("§3Sven Slayer \n" +
							"§7XP: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getSvenXp())) + slayer.getSvenXp() + "\n" +
							"§8Tiers Slain:");

					for(int s = 0; s < slayer.getSvensSlain().size(); s++) {
						svenHover.append("\n   §7T" + (s+1) + "'s: §6" + slayer.getSvensSlain().get(s));
					}

					DecimalFormat formatter = new DecimalFormat("0.0");

					info.addAll(Arrays.asList(new IChatComponent[] {	
							new ChatComponentTranslation("§8--------------------------------"),
							hoverText("§8Stats of ", new String[] {fullName + " ", guild.getGuildTag()}, new String[] {playerProfileHover, guildHover}),
							new ChatComponentTranslation("§7Slayers:"),
							hoverText("   ", new String[] {"§aRevenant §7Level: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getRevXp())) + Slayers.getSlayerLevel(slayer.getRevXp())}, new String[] {revHover.toString()}),
							hoverText("   ", new String[] {"§cTarantula §7Level: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getTaranXp())) + Slayers.getSlayerLevel(slayer.getTaranXp())}, new String[] {taranHover.toString()}),
							hoverText("   ", new String[] {"§3Sven §7Level: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getSvenXp())) + Slayers.getSlayerLevel(slayer.getSvenXp())}, new String[] {svenHover.toString()}),
							new ChatComponentTranslation("§7Fairy Souls: " + (souls.getSoulsCollected() >= FairySouls.totalSouls ? "§5" : "§d") + souls.getSoulsCollected() + "§7/§5" + FairySouls.totalSouls),
							new ChatComponentTranslation("§7Unique Talismans: " + (inventory.getInventoryAPIStatus() ? (new Integer(totalUniqueTalismans.size()) >= Talismans.totalTalismans ? "§6" : "§e") + totalUniqueTalismans.size() + "§7/§6" + Talismans.totalTalismans : "§cAPI is Disabled.")),
							new ChatComponentTranslation("§7Purse: §6" + formatter.format(BigDecimal.valueOf(purse.getPurse()))),
							new ChatComponentTranslation("§7Highest Critical Damage: §e" + dmg.getHighestCrit()),
							new ChatComponentTranslation("§8--------------------------------")
					}));
					break;
				}
				//Auction Info

			} 
		} else {
			System.out.println("SS: Player does not play skyblock.");
			info.addAll(Arrays.asList(new IChatComponent[] {
					new ChatComponentTranslation("§8--------------------------------"),
					hoverText("§8Stats of " + fullName + " ", new String[] {guild.getGuildTag()}, new String[] {guildHover}),
					new ChatComponentTranslation(playerProfileHover.replace("§aCharacter Information \n", "")),
					new ChatComponentTranslation("§8--------------------------------")
			}));
			System.out.println("SS: Successfully sent player info.");
		}





		sendMessages(player, info);
		PlayerAPI.shutdownAPI();


		setAPIStatus(null);
		//Slayer TODO
		//Minions


		//Next Pages:
		//Sea Creatures
		//Collections

	}

	public void start() {

		if(ssThread == null) {
			ssThread = new Thread(this);
			ssThread.start();
		} else {
			System.out.println("SS: Thread already started.");
		}

	}
}
