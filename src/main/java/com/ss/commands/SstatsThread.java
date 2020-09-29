package com.ss.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import com.ss.apireaders.Dungeons;
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
import com.ss.info.Util;
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

		DecimalFormat nf = new DecimalFormat("#.#");
		nf.setGroupingUsed(true);
		nf.setGroupingSize(3);



		PlayerInfo playerInfo = new PlayerInfo();
		String playerProfileHover = "§aCharacter Information \n" +
				"§7Rank: " + (prefix.getPlayerRankString().equals("§7") ? "§8Default" : prefix.getPlayerRankString()).replace("[", "").replace("]", "") + "\n" +
				"§7Level: §6" + nf.format(playerInfo.getNetworkLevel().intValue()) + "\n" +
				"§7Experience until next Level: §6" + nf.format(playerInfo.getExptoNextLevel()) + "\n" +
				"§7Achievement Points: §e" + nf.format(playerInfo.getAchievementPoints()) + "\n" +
				"§7Quests Completed: §6" + nf.format(playerInfo.getQuestsCompleted()) + "\n" +
				"§7Karma: §d" + nf.format(playerInfo.getKarma());

		System.out.println("SS: Grabbed character information.");

		if(Config.getDebugMode()) {
			sendMessage(player, "§7Grabbed character information.");
		}

		String guildHover = new String();

		if(guild.isInGuild()) {
			guildHover = "§aGuild Information \n" +
					"§7Name: §6" + guild.getName() + "\n" +
					"§7Tag: " + guild.getGuildTag().replace("[", "").replace("]", "") + "\n" + 
					"§7Level: §6" + GuildLevels.getGuildLevel(guild.getExp()) + "\n" +
					"§7Members: §e" + guild.getMemberCount() + "\n" + 
					"§7Guildmaster: " + Prefix.getPlayerRankString(guild.getGuildMasterUUID()) + PlayerAPI.UUIDtoName(guild.getGuildMasterUUID());
			System.out.println("SS: Grabbed player's guild information.");
			if(Config.getDebugMode()) {
				sendMessage(player, "§7Grabbed player's guild information.");
			}
		} else {
			guildHover = "";
			System.out.println("SS: Didn't grab player's guild information since they're not in one.");
			if(Config.getDebugMode()) {
				sendMessage(player, "§7Didn't grab player's guild information since they're not in one.");
			}
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

				if(Config.getDebugMode()) {
					sendMessage(player, "§7Grabbed player's skyblock profile names.");
				}

				StringBuilder memberString = new StringBuilder();
				if(profile.getProfileMembers() != null) {
					for(int m = 0; m < profile.getProfileMembers().size(); m++) {
						memberString.append("   " + Prefix.getPlayerRankString(MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(profile.getProfileMembers().get(m)).getId()) + profile.getProfileMembers().get(m) + "\n");

					}
				} else memberString.append("");

				System.out.println("SS: Grabbed player's co-op members.");

				if(Config.getDebugMode()) {
					sendMessage(player, "§7Grabbed player's co-op members.");
				}

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


				if(Config.getDebugMode()) {
					sendMessage(player, "§7Grabbed player's skyblock profile information.");
				}

				double skillAverage = Double.parseDouble(String.format(Locale.US, "%.2f", ((double) skills.getMining() +
						skills.getCombat() +
						skills.getForaging() +
						skills.getFishing() +
						skills.getFarming() +
						skills.getAlchemy() +
						skills.getEnchanting() +
						skills.getTaming())/8));



				String skillHover = "§aPlayer Skills \n" + 
						"§7Mining: " + ColorCode.skillColor(skills.getMining(), 50) + "\n" + 
						"§7Combat: " + ColorCode.skillColor(skills.getCombat(), 50) + "\n" + 
						"§7Foraging: " + ColorCode.skillColor(skills.getForaging(), 50) + "\n" + 
						"§7Fishing: " + ColorCode.skillColor(skills.getFishing(), 50) + "\n" + 
						"§7Farming: " + ColorCode.skillColor(skills.getFarming(), 50) + "\n" + 
						"§7Alchemy: " + ColorCode.skillColor(skills.getAlchemy(), 50) + "\n" + 
						"§7Enchanting: " + ColorCode.skillColor(skills.getEnchanting(), 50) + "\n" + 
						"§7Taming: " + ColorCode.skillColor(skills.getTaming(), 50) +
						(skills.getApiStatus() ? "\n\n§7Carpentry: " + ColorCode.skillColor(skills.getCarpentry(), 50) + "\n§7Runescrafting: " + ColorCode.skillColor(skills.getRunescrafting(), 25) : "\n\n§cSkill API Disabled.");

				System.out.println("SS: Grabbed player's skyblock skills information.");

				if(Config.getDebugMode()) {
					sendMessage(player, "§7Grabbed player's skyblock skills information.");
				}

				String petHover = new String();

				if(pets.getPetsSortedByExp().equals("")) {
					petHover = null;
				} else {
					petHover = "§aPets Sorted By Experience \n" +
							pets.getPetsSortedByExp();
				}

				System.out.println("SS: Grabbed player's skyblock pets information.");

				if(Config.getDebugMode()) {
					sendMessage(player, "§7Grabbed player's skyblock pets information.");
				}

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
						hoverText("§7Skill Average: ", new String[] {ColorCode.skillColor(skillAverage, 50)}, new String[] {skillHover}),
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
				info.add(clickText("§eNext Page", new SStats().getCommandName() + " " + args[0] + (profileName != null ? " " + profileName : "") + " 2"));

				info.add(new ChatComponentTranslation("§8--------------------------------"));

				System.out.println("SS: Successfully sent player info.");
				if(Config.getDebugMode()) {
					sendMessage(player, "§7Successfully sent player info.");
				}
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
							"§7XP: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getRevXp())) + nf.format(slayer.getRevXp()) + "\n" +
							"§8Tiers Slain:");

					for(int s = 0; s < slayer.getRevsSlain().size(); s++) {
						revHover.append("\n   §7T" + (s+1) + "'s: §6" + nf.format(slayer.getRevsSlain().get(s)));
					}

					StringBuilder taranHover = new StringBuilder("§cTarantula Slayer \n" +
							"§7XP: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getTaranXp())) +  nf.format(slayer.getTaranXp()) + "\n" +
							"§8Tiers Slain:");

					for(int s = 0; s < slayer.getTaransSlain().size(); s++) {
						taranHover.append("\n   §7T" + (s+1) + "'s: §6" + nf.format(slayer.getTaransSlain().get(s)));
					}

					StringBuilder svenHover = new StringBuilder("§3Sven Slayer \n" +
							"§7XP: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getSvenXp())) + nf.format(slayer.getSvenXp()) + "\n" +
							"§8Tiers Slain:");

					for(int s = 0; s < slayer.getSvensSlain().size(); s++) {
						svenHover.append("\n   §7T" + (s+1) + "'s: §6" + nf.format(slayer.getSvensSlain().get(s)));
					}
					info.addAll(Arrays.asList(new IChatComponent[] {	
							new ChatComponentTranslation("§8--------------------------------"),
							hoverText("§8Stats of ", new String[] {fullName + " ", guild.getGuildTag()}, new String[] {playerProfileHover, guildHover}),
							new ChatComponentTranslation("§7Slayers:"),
							hoverText("   ", new String[] {"§aRevenant §7Level: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getRevXp())) + Slayers.getSlayerLevel(slayer.getRevXp())}, new String[] {revHover.toString()}),
							hoverText("   ", new String[] {"§cTarantula §7Level: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getTaranXp())) + Slayers.getSlayerLevel(slayer.getTaranXp())}, new String[] {taranHover.toString()}),
							hoverText("   ", new String[] {"§3Sven §7Level: " + ColorCode.slayerColor(Slayers.getSlayerLevel(slayer.getSvenXp())) + Slayers.getSlayerLevel(slayer.getSvenXp())}, new String[] {svenHover.toString()}),
							new ChatComponentTranslation("§7Fairy Souls: " + (souls.getSoulsCollected() >= FairySouls.totalSouls ? "§5" : "§d") + souls.getSoulsCollected() + "§7/§5" + FairySouls.totalSouls),
							new ChatComponentTranslation("§7Unique Talismans: " + (inventory.getInventoryAPIStatus() ? (new Integer(totalUniqueTalismans.size()) >= Talismans.totalTalismans ? "§6" : "§e") + totalUniqueTalismans.size() + "§7/§6" + Talismans.totalTalismans : "§cAPI is Disabled.")),
							new ChatComponentTranslation("§7Purse: §6" + nf.format(purse.getPurse())),
							new ChatComponentTranslation("§7Highest Critical Damage: §e" + nf.format(dmg.getHighestCrit())),
							clickText("§eNext Page", new SStats().getCommandName() + " " + args[0] + (profileName != null ? " " + profileName : "") + " 3"),
							new ChatComponentTranslation("§8--------------------------------")
					}));
					System.out.println("SS: Successfully sent page two of player's info.");

					if(Config.getDebugMode()) {
						sendMessage(player, "§7Successfully sent page two of player's info.");
					}

					break;
				case "3":

					Dungeons dungeons = new Dungeons();


					String[] hovers = new String[6];

					for(int x = 0; x < 6; x++) {

						StringBuilder progressBar = new StringBuilder("§2--------------------");
						String hover;
						if(x == 5) {
							progressBar.insert(2 + (int) Math.ceil((20 * dungeons.getCatacombsLevel()[1]) / Util.dungeonLevelReqs[(int) dungeons.getCatacombsLevel()[0]]), "§f");
							hover = "§7Progress to Level " + (int) Math.ceil(dungeons.getCatacombsLevel()[0]) + ": §e" + "\n"
									+ progressBar + " §e" + nf.format(dungeons.getCatacombsLevel()[1]) + "§6/§e" + nf.format(Util.dungeonLevelReqs[(int) dungeons.getCatacombsLevel()[0]]);

						} else {
							progressBar.insert(2 + (int) Math.ceil((20 * dungeons.getAllClasses()[x][1]) / Util.dungeonLevelReqs[(int) dungeons.getAllClasses()[x][0]]), "§f");
							hover = "§7Progress to Level " + (int) Math.ceil(dungeons.getAllClasses()[x][0]) + ": §e" + "\n"
									+ progressBar + " §e" + nf.format(dungeons.getAllClasses()[x][1]) + "§6/§e" + nf.format(Util.dungeonLevelReqs[(int) dungeons.getAllClasses()[x][0]]);

						}


						hovers[x] = hover;

						StringBuilder t = new StringBuilder();

						for(double[] d : dungeons.getAllClasses()) {
							t.append(d[0] + ", ");
						}

					}

					info.addAll(Arrays.asList(new IChatComponent[] {	
							new ChatComponentTranslation("§8--------------------------------"),
							hoverText("§8Stats of ", new String[] {fullName + " ", guild.getGuildTag()}, new String[] {playerProfileHover, guildHover}),
							hoverText("§cCatacombs§7 Level: ", new String[] {ColorCode.skillColor(dungeons.getCatacombsLevel()[0], 50)}, new String[] {hovers[5]}),
							new ChatComponentTranslation("§7Active Class: " + (dungeons.getActiveClass().equals("") ? "§cN/A" : "§a" + dungeons.getActiveClass())),
							new ChatComponentTranslation("§7Class Levels:")
					}));

					String[] classes = new String[] {"Archer", "Berserk", "Mage", "Tank", "Healer"};

					for(int x = 0; x < 5; x++) {
						info.add(hoverText("   ", new String[] {(dungeons.getActiveClass().equals(classes[x]) ? "§a" : "§7") + classes[x] + "§7: " + ColorCode.skillColor(dungeons.getAllClasses()[x][0], 50)}, new String[] {hovers[x]}));
					}

					String[] bosses = new String[] {"Bonzo", "Scarf", "The Professor", "Thorn", "Livid", "Sadan", "Maxor", "Goldor", "Storm", "Necron"};
					info.add(new ChatComponentTranslation("§7Runs Completed:"));
					try {
						for(int x = 0; x < dungeons.getFloorsStarted().length; x++) {

							if(dungeons.getFloorsStarted()[x] != -1) {
								String hover = "§cThe Catacombs §8- " + (x == 0 ? "§eEntrance" : "§eFloor " + x) + "\n\n"
										+ (x == 0 ? "" : "§7" + bosses[x-1] + " Kills: §a" + (dungeons.getFloorsCompleted()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getFloorsCompleted()[x])) + "\n")
										+ "§7The Watcher Kills: " + (dungeons.getWatcherKills()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getWatcherKills()[x])) + "\n"
										+ "§7Fastest Time: " + (dungeons.getFastestTime()[x] == -1 ? "§cN/A" : "§a" + (Util.milliToMinute(dungeons.getFastestTime()[x])[0] < 10 ? "0" : "") + Util.milliToMinute(dungeons.getFastestTime()[x])[0] + "m " + (Util.milliToMinute(dungeons.getFastestTime()[x])[1] < 10 ? "0" : "") + Util.milliToMinute(dungeons.getFastestTime()[x])[1]) + "s\n"
										+ "§7Fastest Time (S): " + (dungeons.getFastestTimeS()[x] == -1 ? "§cN/A" : "§a" + (Util.milliToMinute(dungeons.getFastestTimeS()[x])[0] < 10 ? "0" : "") + Util.milliToMinute(dungeons.getFastestTimeS()[x])[0] + "m " + (Util.milliToMinute(dungeons.getFastestTimeS()[x])[1] < 10 ? "0" : "") + Util.milliToMinute(dungeons.getFastestTimeS()[x])[1]) + "s\n"
										+ "§7Fastest Time (S+): " + (dungeons.getFastestTimeSPlus()[x] == -1 ? "§cN/A" : "§a" + (Util.milliToMinute(dungeons.getFastestTimeSPlus()[x])[0] < 10 ? "0" : "") + Util.milliToMinute(dungeons.getFastestTimeSPlus()[x])[0] + "m " + (Util.milliToMinute(dungeons.getFastestTimeSPlus()[x])[1] < 10 ? "0" : "") + Util.milliToMinute(dungeons.getFastestTimeSPlus()[x])[1]) + "s\n"
										+ "§7Best Score: " + (dungeons.getBestScore()[x] == -1 ? "§cN/A" : "§a" + dungeons.getBestScore()[x]) + "\n"
										+ "§7Most Archer Damage: " + (dungeons.getArcherMostDamage()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getArcherMostDamage()[x])) + "\n"
										+ "§7Most Berserk Damage: " + (dungeons.getBerserkMostDamage()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getBerserkMostDamage()[x])) + "\n"
										+ "§7Most Healer Damage: " + (dungeons.getHealerMostDamage()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getHealerMostDamage()[x])) + "\n"
										+ "§7Most Mage Damage: " + (dungeons.getMageMostDamage()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getMageMostDamage()[x])) + "\n"
										+ "§7Most Tank Damage: " + (dungeons.getTankMostDamage()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getTankMostDamage()[x])) + "\n"
										+ "§7Most Ally Healing: " + (dungeons.getMostHealing()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getMostHealing()[x])) + "\n"
										+ "§7Total Enemies Killed: " + (dungeons.getTotalMobsKilled()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getTotalMobsKilled()[x])) + "\n"
										+ "§7Most Enemies Killed: " + (dungeons.getMostMobsKilled()[x] == -1 ? "§cN/A" : "§a" + nf.format(dungeons.getMostMobsKilled()[x]));







								info.add(hoverText("   ", new String[] {"§7" + (x == 0 ? "Entrance" : "Floor " + x) + ": " + (dungeons.getFloorsCompleted()[x] == dungeons.getFloorsStarted()[x] ? "§2" : "§a") + (dungeons.getFloorsCompleted()[x] == -1 ? "0" : nf.format(dungeons.getFloorsCompleted()[x])) + "§7/§2" + nf.format(dungeons.getFloorsStarted()[x])}, new String[] {hover}));
							}
						}
					} catch(NullPointerException e) {
						e.printStackTrace();
						info.remove(info.size()-1);
					}

					int totalStartedRuns = 0;
					int totalCompletedRuns = 0;

					for(int x = 0; x<dungeons.getFloorsStarted().length; x++) {
						totalStartedRuns += dungeons.getFloorsStarted()[x];
						totalCompletedRuns += dungeons.getFloorsCompleted()[x];
					}

					double wlRatio = ((double) totalCompletedRuns) / totalStartedRuns * 100;

					DecimalFormat pf = new DecimalFormat("#.##");
					System.out.println((((double) totalCompletedRuns) / totalStartedRuns) * 100);
					info.addAll(Arrays.asList(new IChatComponent[] {
							hoverText("§7Win/Loss Ratio: ", new String[] {ColorCode.skillColor(pf.format(wlRatio), 100) + "%"}, new String[] {(totalStartedRuns == totalCompletedRuns ? "§2" : "§a") + totalCompletedRuns + " §7out of §2" + totalStartedRuns + " §7runs completed."} ),
							new ChatComponentTranslation("§7Secrets Found: §6" + nf.format(dungeons.getSecretsFound())),
							new ChatComponentTranslation("§8--------------------------------")
					}));


					System.out.println("SS: Successfully sent page three of player's info.");

					if(Config.getDebugMode()) {
						sendMessage(player, "§7Successfully sent page three of player's info.");
					}

					break;
				}
				//Auction Info

			} 
		} else {
			System.out.println("SS: Player does not play skyblock.");

			if(Config.getDebugMode()) {
				sendMessage(player, "§7Player does not play skyblock.");
			}

			info.addAll(Arrays.asList(new IChatComponent[] {
					new ChatComponentTranslation("§8--------------------------------"),
					hoverText("§8Stats of " + fullName + " ", new String[] {guild.getGuildTag()}, new String[] {guildHover}),
					new ChatComponentTranslation(playerProfileHover.replace("§aCharacter Information \n", "")),
					new ChatComponentTranslation("§8--------------------------------")
			}));
			System.out.println("SS: Successfully sent player info.");

			if(Config.getDebugMode()) {
				sendMessage(player, "§7Successfully sent player info.");
			}
		}





		sendMessages(player, info);
		PlayerAPI.shutdownAPI();


		setAPIStatus(null);
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
