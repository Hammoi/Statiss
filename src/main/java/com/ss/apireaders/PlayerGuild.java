package com.ss.apireaders;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonObject;
import com.ss.info.ColorCode;
import com.ss.info.PlayerAPI;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.GuildReply.Guild;
import net.hypixel.api.reply.GuildReply.Guild.Member;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.UsernameCache;

public class PlayerGuild {
	private Guild guild;

	public Guild getPlayerGuild() {

		if(guild == null) {
			setPlayerGuild(PlayerAPI.getPlayerUUID());
		}

		return guild;
	}

	public void setPlayerGuild(Guild newGuild) {
		if(newGuild == null) {
			setInGuildStatus(false);
		} else setInGuildStatus(true);
		this.guild = newGuild;
	}

	public void setPlayerGuild(UUID playerUUID) {

		try {
			setPlayerGuild(PlayerAPI.getAPI().getGuildByPlayer(playerUUID).get().getGuild());
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (ExecutionException e) {

			PlayerAPI.badAPI();
		}
	}

	private String guildTag;

	public String getGuildTag() {

		if(guildTag == null) {
			if(isInGuild()) {
				String playerGuildTag = new String();
				if(!(getPlayerGuild().getTag() == null)) {
					if(!(getPlayerGuild().getTagColor() == null)) {
						setGuildTag(ColorCode.getColorCode(getPlayerGuild().getTagColor()) + "[" + getPlayerGuild().getTag() + "]");
					} else {
						setGuildTag("§7[" + getPlayerGuild().getTag() + "]");
					}
				} else {
					setGuildTag("");
				}

			} else {
				setGuildTag("");
			}
		}
		return guildTag;

	}
	public void setGuildTag(String newTag) {
		this.guildTag = newTag;
	}


	private Long exp;

	public Long getExp() {
		if(exp == null && isInGuild()) {
			setExp(getPlayerGuild().getExp());
		} else {
			setExp((long) -1);
		}

		return exp;
	}

	public void setExp(Long newExp) {
		this.exp = newExp;
	}

	private Integer memberCount;

	public Integer getMemberCount() {

		if(memberCount == null  && isInGuild()) {
			setMemberCount(getGuildMembers().size());
		} else {
			setMemberCount(-1);
		}

		return memberCount;
	}

	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

	private UUID guildMasterUUID;

	public UUID getGuildMasterUUID() {

		if(guildMasterUUID == null) {
			
			if(isInGuild()) {
				for(int x=0; x<getGuildMembers().size(); x++) {
					if(getGuildMembers().get(x).getRank().toLowerCase().contains("guild master") || getGuildMembers().get(x).getRank().toLowerCase().contains("guildmaster")) {
						setGuildMasterUUID(getGuildMembers().get(x).getUuid());
					}
				}
			} else {
				setGuildMasterUUID(null);
			}
		}

		return guildMasterUUID;
	}

	public void setGuildMasterUUID(UUID guildMasterUUID) {
		this.guildMasterUUID = guildMasterUUID;
	}

	private String guildName;

	public String getName() {
		if(guildName == null && isInGuild()) {
			setName(getPlayerGuild().getName());
		} else {
			setName("");
		}

		return guildName;
	}

	public void setName(String newName) {
		this.guildName = newName;
	}

	private List<Member> guildMembers;

	public List<Member> getGuildMembers() {
		if(guildMembers == null && isInGuild()) {
			setGuildMembers(getPlayerGuild().getMembers());
		}
		return guildMembers;

	}

	public void setGuildMembers(List<Member> newMembers) {
		this.guildMembers = newMembers;
	}

	private Boolean inGuild;

	public Boolean isInGuild() {

		if(inGuild == null) {
			getPlayerGuild();
		}

		return inGuild;
	}

	public void setInGuildStatus(boolean newStatus) {
		inGuild = newStatus;
	}
}
