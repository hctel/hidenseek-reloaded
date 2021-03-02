package be.hctel.revhive.hidev2.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.hctel.revhive.hidev2.Hide;
import be.hctel.revhive.hidev2.enums.HideMap;


public class MapSelector {
	private HideMap[] maps;
	public HideMap map;
	private ArrayList<HideMap> mapList = new ArrayList<HideMap>();
	private HashMap<Integer, Integer> votes = new HashMap<Integer, Integer>();
	private HashMap<Player, Integer> playerVote = new HashMap<Player, Integer>();
	private boolean lockVotes = false;
	public MapSelector(HideMap[] maps) {
		this.maps = maps;
		Random r = new Random();
		ArrayList<Integer> availMaps = new ArrayList<Integer>();
		for(int i = 0; i < 6; i++) {
			int selected = r.nextInt(this.maps.length);
			while(availMaps.contains(selected)) {
				selected = r.nextInt(this.maps.length);
			}
			availMaps.add(selected);
		}
		int a = 0;
		for(int i : availMaps) {
			votes.put(a, 0);
			mapList.add(this.maps[i]);
			a++;
		}
	}
	
	public void registerPlayerVote(Player player, int index) {
		index--;
		int voteamount = Hide.ranks.getRank(player).getVoteAmount();
		if(lockVotes) {
			player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §cVoting is not active right now! §7§lThe winning map was " + map.getFriendlyName());
			return;
		}
		if(playerVote.containsKey(player)) {
			if(playerVote.get(player) == index) {
				player.sendMessage(Hide.header + "§cYou have already voted for that map!");
				return;
			} else {
				votes.replace(playerVote.get(player), (votes.get(playerVote.get(player))-voteamount));
				votes.replace(index, votes.get(index)+voteamount);
				playerVote.replace(player, index);
				if(index == 5) {
					player.sendMessage(Hide.header + "§aYou voted for §cRandom map§a! §7[" + (votes.get(index)+voteamount) + " votes]");
				} else {
					player.sendMessage(Hide.header + "§aYou voted for §6" + mapList.get(index).getFriendlyName() + "§a! §7[" + (votes.get(index)+voteamount) + " votes]");
				}
			}
		} else {
			votes.put(index, votes.get(index)+voteamount);
			playerVote.put(player, index);
			if(index == 5) {
				player.sendMessage(Hide.header + "§aYou voted for §cRandom map§a! §7[" + (votes.get(index)+voteamount) + " votes]");
			} else {
				player.sendMessage(Hide.header + "§aYou voted for §6" + mapList.get(index).getFriendlyName() + "§a! §7[" + (votes.get(index)+voteamount) + " votes]");
			}
		}
	}
	
	public HideMap getMap() {
		int indexMax = 0;
		int valueMax = 0;
		for(int i : votes.keySet()) {
			if(votes.get(i) > valueMax) {
				indexMax = i;
				valueMax = votes.get(i);
			}
		}
		this.map = mapList.get(indexMax);
		Bukkit.broadcastMessage(Hide.header + " §bVoting has ended! The map §f" + map.getFriendlyName() + " §bhas won!");
		return mapList.get(indexMax);
	}
	public void sendMapChoices(Player player) {
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §e§lVote for a map! §7Use §r/v #.");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l1. §6" + mapList.get(0).getFriendlyName() + " §7[§r" + votes.get(0) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l2. §6" + mapList.get(1).getFriendlyName() + " §7[§r" + votes.get(1) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l3. §6" + mapList.get(2).getFriendlyName() + " §7[§r" + votes.get(2) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l4. §6" + mapList.get(3).getFriendlyName() + " §7[§r" + votes.get(3) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l5. §6" + mapList.get(4).getFriendlyName() + " §7[§r" + votes.get(4) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l6. §cRandom Map §7[§r" + votes.get(5) + "§7 Votes]");
	}
	public void broadcastMapChoices() {
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §e§lVote for a map! §7Use §r/v #.");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l1. §6" + mapList.get(0).getFriendlyName() + " §7[§r" + votes.get(0) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l2. §6" + mapList.get(1).getFriendlyName() + " §7[§r" + votes.get(1) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l3. §6" + mapList.get(2).getFriendlyName() + " §7[§r" + votes.get(2) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l4. §6" + mapList.get(3).getFriendlyName() + " §7[§r" + votes.get(3) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l5. §6" + mapList.get(4).getFriendlyName() + " §7[§r" + votes.get(4) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l6. §cRandom Map §7[§r" + votes.get(5) + "§7 Votes]");
	}
}