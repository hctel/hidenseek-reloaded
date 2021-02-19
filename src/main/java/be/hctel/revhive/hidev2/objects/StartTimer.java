package be.hctel.revhive.hidev2.objects;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import be.hctel.revhive.hidev2.Hide;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class StartTimer {
	private Plugin plugin;
	private BukkitScheduler scheduler;
	public boolean gameHasStarted = false;
	public int timeBeforeStart = 35;
	private int minPlayers = 12;
	
	public StartTimer(BukkitScheduler scheduler, Plugin plugin) {
		this.plugin = plugin;
		this.scheduler = scheduler;
		this.scheduler.scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			public void run() {
				if(!gameHasStarted) {
					if(Bukkit.getOnlinePlayers().size() >= minPlayers) {
						if(timeBeforeStart > 20) {
							int time = timeBeforeStart-15;
							String message = "§aStarting in " + time;
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
							} 
						} else if(timeBeforeStart <= 20 && timeBeforeStart > 15) {
							int time = timeBeforeStart-15;
							String message = "§aStarting in §c" + time;
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
								player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
							} 
						} else if(timeBeforeStart == 15) {
							int time = timeBeforeStart;
							Hide.blockSelect = new BlockSelector(Hide.stats, Hide.selector.getMap());
							String message = "§eChoose your block │ §aStarting in " + time;
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
								player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0f, 1.0f);
								Hide.blockSelect.openBlockSelector(player);
							} 
						} else if(timeBeforeStart < 15) {
							int time = timeBeforeStart;
							String message = "§eChoose your block │ §aStarting in " + time;
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
							}
						} else if(timeBeforeStart == 0) {
							//Hide.gameManager.startGame();
							gameHasStarted = true;
							
						}
						timeBeforeStart--;
					} else {
						if(timeBeforeStart != 35) {
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.sendTitle("§cStart cancelled!", "§eWe don't have enough players to start the game anymore", 0, 70, 70);
								player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 0.5f);
								timeBeforeStart = 35;
							}
						}
						int playersremaining = Bukkit.getOnlinePlayers().size() - minPlayers;
						String message;
						if(playersremaining == 1) {
							 message = "§e" + playersremaining + " player needed to start";
						}
						else {
							 message = "§e" + playersremaining + " players needed to start";
						}
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
						}
					}
				}
			}
		},0L,20L);
	}
	
}