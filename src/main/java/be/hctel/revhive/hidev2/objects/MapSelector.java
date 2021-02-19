package be.hctel.revhive.hidev2.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.hctel.revhive.hidev2.enums.HideMap;


public class MapSelector {
	private HideMap[] maps;
	public HideMap map;
	private HashMap<HideMap, Integer> votes = new HashMap<HideMap, Integer>();
	private HashMap<Integer, HideMap> mapPosition = new HashMap<Integer,HideMap>();
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
			votes.put(this.maps[i], 0);
			mapPosition.put(a, this.maps[i]);
			a++;
		}
	}
	
	public void registerPlayerVote(Player player, int index) {
		index--;
		if(lockVotes == true) {
			player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §cVoting is not active right now! §7!oThe winning map was " + map.getFriendlyName());
		}
		if(playerVote.containsKey(player)) {
			if(playerVote.get(player) == index) {
				player.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §cYou have already voted for that map!");
				return;
			} else {
				
				switch(index) {
				case 5: 
					player.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §aYou voted for §cRandom map§a! §7[" +votes.get(mapPosition.get(5))+ " total]");
					playerVote.replace(player, index);
					votes.replace(mapPosition.get(index), votes.get(mapPosition.get(playerVote.get(player)))-1);
					votes.replace(mapPosition.get(index), votes.get(mapPosition.get(index))+1);
				default:
					player.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §aYou voted for §6 "+ mapPosition.get(index).getFriendlyName() + "§a! §7[" + votes.get(mapPosition.get(index)) + " total]");
					playerVote.replace(player, index);
					votes.replace(mapPosition.get(index), votes.get(mapPosition.get(playerVote.get(player)))-1);
					votes.replace(mapPosition.get(index), votes.get(mapPosition.get(index))+1);
				}
				playerVote.replace(player, index);
			}
		} else {
			playerVote.put(player, index);
			switch(index) {
			case 5: 
				player.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §aYou voted for §cRandom map§a! §7[" +votes.get(mapPosition.get(5))+ " total]");
				playerVote.put(player, index);
				votes.replace(mapPosition.get(index), votes.get(mapPosition.get(index))+1);
			default:
				player.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §aYou voted for §6 "+ mapPosition.get(index).getFriendlyName() + "§a! §7[" + votes.get(mapPosition.get(index)) + " total]");
				playerVote.put(player, index);
				votes.replace(mapPosition.get(index), votes.get(mapPosition.get(index))+1);
			}
		}
	}
	
	public HideMap getMap() {
		lockVotes = true;
		int maxValueInMap =(Collections.max(this.votes.values()));
		for(Entry<HideMap, Integer> entry : this.votes.entrySet()) {
			if(entry.getValue() == maxValueInMap) {
				this.map = entry.getKey();
				return entry.getKey();
			}
		}
		return HideMap.VENICE_BRIDGE;
	}
	public void sendMapChoices(Player player) {
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §e§lVote for a map! §7Use §r/v # §7or click.");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l1. §6" + mapPosition.get(0).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(0)) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l2. §6" + mapPosition.get(1).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(1)) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l3. §6" + mapPosition.get(2).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(2)) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l4. §6" + mapPosition.get(3).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(3)) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l5. §6" + mapPosition.get(4).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(4)) + "§7 Votes]");
		player.sendMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l6. §cRandom Map §7[§r" + votes.get(mapPosition.get(5)) + "§7 Votes]");
	}
	public void broadcastMapChoices() {
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §e§lVote for a map! §7Use §r/v # §7or click.");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l1. §6" + mapPosition.get(0).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(0)) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l2. §6" + mapPosition.get(1).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(1)) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l3. §6" + mapPosition.get(2).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(2)) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l4. §6" + mapPosition.get(3).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(3)) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l5. §6" + mapPosition.get(4).getFriendlyName() + " §7[§r" + votes.get(mapPosition.get(4)) + "§7 Votes]");
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §7§l6. §cRandom Map §7[§r" + votes.get(mapPosition.get(5)) + "§7 Votes]");
	}
}