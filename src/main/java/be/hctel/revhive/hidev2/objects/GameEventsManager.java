package be.hctel.revhive.hidev2.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import be.hctel.revhive.hidev2.Hide;
import be.hctel.revhive.hidev2.enums.Block;
import be.hctel.revhive.hidev2.enums.HideMap;
import be.hctel.revhive.hidev2.enums.Role;

public class GameEventsManager {
	private Stat stats;
	private Plugin plugin;
	private BukkitScheduler scheduler;
	public int ticksRemaining = 6600;
	public int secondsRemaining = 330;
	public boolean gameHasStarted = false;
	private HashMap<Player, Role> roles = new HashMap<Player, Role>();
	private HashMap<Player, Location> lastSecondLocation = new HashMap<Player, Location>();
	private HashMap<Player, Integer> timeInSamePlace = new HashMap<Player, Integer>();
	private HashMap<Player, PlayerBlock> blocks = new HashMap<Player, PlayerBlock>();
	private HashMap<Player, Block> playerblock = new HashMap<Player, Block>();
	private HashMap<Player, Integer> killedAt = new HashMap<Player, Integer>();
	private ArrayList<Player> queuedSeekers = new ArrayList<Player>();
	public ArrayList<Player> hiders = new ArrayList<Player>();
	private ArrayList<Player> seekers = new ArrayList<Player>();
	private HideMap map;
	
	
	public GameEventsManager(Plugin plugin, BukkitScheduler scheduler, Stat stats) {
			this.stats = stats;
			this.plugin = plugin;
			this.scheduler = scheduler;
	}
	
	public void startGame(HideMap map) {
		this.map = map;
		Player seeker = pickSeeker();
		seekers.add(seeker);
		roles.put(seeker, Role.SEEKER);
		teleportToSeekerSpawn(seeker);
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player != seeker) {
				hiders.add(player);
				roles.put(player, Role.HIDER);
				playerblock.put(player, Hide.blockSelect.getBlock(player));
				blocks.put(player, new PlayerBlock(player, Hide.blockSelect.getBlock(player)));
				System.out.println(blocks.get(player).toString());
				blocks.get(player).spawnBlock();
				player.sendMessage("§6       ————————————————————————————————");
				player.sendMessage("§b§l        You are a §f§lHIDER! §3(" + playerblock.get(player).getFriendlyName() + ")");
				player.sendMessage("§aFind a hiding spot before the seeker's released!");
				player.sendMessage("§c   The seeker will be released in §l30 seconds!");
				player.sendMessage("§6       ————————————————————————————————");
				player.teleport(map.getSpawn());
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.hidePlayer(Hide.plugin, player);
				}
			}
		}
		for(Player player : hiders) {
			timeInSamePlace.put(player, 0);
			lastSecondLocation.put(player, map.getSpawn());
		}
		this.gameHasStarted = true;
		this.scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				if(gameHasStarted) {
					for(Player player : hiders) {
						if(player.getLocation().getX() == lastSecondLocation.get(player).getX() && player.getLocation().getY() == lastSecondLocation.get(player).getY() && player.getLocation().getZ() == lastSecondLocation.get(player).getZ()) {
							if(timeInSamePlace.get(player) < 4 && !blocks.get(player).isSolid) {
								if(timeInSamePlace.get(player) == 1) {
									player.sendTitle("§a§l»§7§l»»»", "", 0, 20, 0);
								}
								if(timeInSamePlace.get(player) == 2) {
									player.sendTitle("§a§l»»§7§l»»", "", 0, 20, 0);
								}
								if(timeInSamePlace.get(player) == 3) {
									player.sendTitle("§a§l»»»§7§l»", "", 0, 20, 0);
								}
							} if (timeInSamePlace.get(player) == 4  && !blocks.get(player).isSolid) {
								player.spawnParticle(Particle.REDSTONE, player.getLocation(), 20);
							}
							timeInSamePlace.replace(player, timeInSamePlace.get(player)+1);
						} else  {
							if(blocks.get(player).isSolid) blocks.get(player).notSolid();
							timeInSamePlace.replace(player, 0);
						}
					}
					if(secondsRemaining == 303) {
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 0.9f, 1.0f);
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1.5f, 1.0f);
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1.05f, 1.0f);
							player.sendMessage(Hide.header + " §eStarting in §f3");
						}
					}
					if(secondsRemaining == 302) {
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1.05f, 1.0f);
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 0.9f, 1.0f);
							player.sendMessage(Hide.header + " §eStarting in §f2");
						}
					}
					if(secondsRemaining == 301) {
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 0.9f, 1.0f);
							player.sendMessage(Hide.header + " §eStarting in §f1");
						}
					}
					if(secondsRemaining == 300) {
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0f, 1.0f);
							player.sendMessage(Hide.header + " §c§lReady or not, here they come!");
						}
					}
					secondsRemaining--;
				}
			}
		}, 0L, 20L);
		this.scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				if(gameHasStarted) {
					for(Player player : hiders) {
						blocks.get(player).teleportBlock();
					}
					if(hiders.isEmpty()) {
						endGame(Role.SEEKER);
					}
					if(ticksRemaining == 0) {
						if(hiders.isEmpty()) {
							endGame(Role.SEEKER);
						} else {
							endGame(Role.HIDER);
						}
					}
					ticksRemaining--;
				}
			}
		}, 0L, 1L);
	}
	public void killHider(Player player, Player seeker) {
		blocks.get(player).isKilled();
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ " + stats.getBlockLevel(player, playerblock.get(player)) + " §7" + playerblock.get(player).getFriendlyName() + " " + Hide.ranks.getRankColorCode(player) + player.getName() + " §6has been killed by " + Hide.ranks.getRankColorCode(seeker) + seeker.getName());
		stats.addPoints(seeker, 30);
		player.teleport(map.getSeekerStart());
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(Hide.plugin, player);
		}
		killedAt.put(player, secondsRemaining);
	}
	public void killSeeker(Player player, Player hider) {
		stats.addPoints(hider, 30);
		Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ " + "§6Seeker " + Hide.ranks.getRankColorCode(player) + player.getName() + " §6has been killed by " + Hide.ranks.getRankColorCode(hider) + hider.getName());
		player.teleport(map.getSpawn());
	}
	
	@SuppressWarnings("deprecation")
	public void teleportToSeekerSpawn(Player player) {
		player.teleport(map.getSeekerStart());
		if(secondsRemaining > 300) {
			long delay = ticksRemaining-6000;
			final Player toTeleport = player;
			player.sendMessage("§c        ———————————————————————————————————————————————————");
			player.sendMessage("");
			player.sendMessage("                         §6§lYou are a §c§lSEEKER!");
			player.sendMessage("§e        It's your job to find hidden block and KILL THEM!");
			player.sendMessage("");
			player.sendMessage("               You will be released in §l" + (int) delay/20 + " seconds!");
			player.sendMessage("");
			player.sendMessage("§c        ———————————————————————————————————————————————————");
			scheduler.scheduleAsyncDelayedTask(plugin, new Runnable() {
				public void run() {
					toTeleport.playSound(toTeleport.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
					toTeleport.teleport(map.getSpawn());
				}
			}, delay);
 		} else {
 			long delay = 200;
			final Player toTeleport = player;
			player.sendMessage("§c        ———————————————————————————————————————————————————");
			player.sendMessage("");
			player.sendMessage("                         §6§lYou are a §c§lSEEKER!");
			player.sendMessage("§e        It's your job to find hidden block and KILL THEM!");
			player.sendMessage("");
			player.sendMessage("               You will be released in §l" + (int) delay/20 + " seconds!");
			player.sendMessage("");
			player.sendMessage("§c        ———————————————————————————————————————————————————");
			scheduler.scheduleAsyncDelayedTask(plugin, new Runnable() {
				public void run() {
					toTeleport.playSound(toTeleport.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
					toTeleport.teleport(map.getSpawn());
				}
			}, delay);
 		}
	}
	
	public PlayerBlock getPlayerBlock(Player player) {
		return blocks.get(player);
	}
	public Role getRole(Player player) {
		return roles.get(player);
	}
	private void endGame(Role winners) {
		if(winners == Role.HIDER) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.sendTitle("§cGAME OVER!", "§6Hiders won the game!", 0, 40, 70);
				player.getInventory().clear();
				player.playSound(player.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
			}
			for(Player player : hiders) {
				player.sendMessage(Hide.header + "§6you gained §e50 points §6for winning as hider!");
				stats.addPoints(player, 50);
				stats.addWin(player);
			}
		}
		if(winners == Role.SEEKER) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.sendTitle("§cGAME OVER!", "§6Seekers won the game!", 0, 40, 70);
				player.getInventory().clear();
				player.playSound(player.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
			}
			for(Player player : seekers) {
				player.sendMessage(Hide.header + "§6you gained §e50 points §6for winning as a seeker!");
				stats.addPoints(player, 50);
				stats.addWin(player);
			}
		}
	}
	
	private Player pickSeeker() {
		Player seeker;
		Random r = new Random();
		if(seekers.isEmpty()) {
			if(!queuedSeekers.isEmpty()) {
				int picked = r.nextInt(queuedSeekers.size()-1);
				while(Bukkit.getPlayer(queuedSeekers.get(picked).getName()) == null) {
					picked = r.nextInt(queuedSeekers.size()-1); //to avoid to get no player if he disconnects
				}
				seeker = queuedSeekers.get(picked);
			} else {
				ArrayList<Player> onlinePlayers = new ArrayList<Player>();
				for(Player player : Bukkit.getOnlinePlayers()) {
					onlinePlayers.add(player);
				}
				int picked = r.nextInt(onlinePlayers.size()-1); 
				while(Bukkit.getPlayer(onlinePlayers.get(picked).getName()) == null) {
					picked = r.nextInt(onlinePlayers.size()-1); //to avoid to get no player if he disconnects
				}
				seeker = onlinePlayers.get(picked);
			}
		} else {
			if(!queuedSeekers.isEmpty()) {
				int picked = r.nextInt(queuedSeekers.size()-1);
				while(Bukkit.getPlayer(queuedSeekers.get(picked).getName()) == null || seekers.contains(queuedSeekers.get(picked))) {
					picked = r.nextInt(queuedSeekers.size()-1); //to avoid to get no player if he disconnects
				}
				seeker = queuedSeekers.get(picked);
			} else {
				ArrayList<Player> onlinePlayers = new ArrayList<Player>();
				for(Player player : Bukkit.getOnlinePlayers()) {
					onlinePlayers.add(player);
				}
				int picked = r.nextInt(onlinePlayers.size()-1); 
				while(Bukkit.getPlayer(onlinePlayers.get(picked).getName()) == null || seekers.contains(onlinePlayers.get(picked))) {
					picked = r.nextInt(onlinePlayers.size()-1); //to avoid to get no player if he disconnects
				}
				seeker = onlinePlayers.get(picked);
			}
			Bukkit.broadcastMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ " + Hide.stats.getBlockLevel(seeker, playerblock.get(seeker)) + " " + playerblock.get(seeker).getFriendlyName()+ " §9" + seeker.getName() + " §6has been made a seeker §7[seeker struggling]");
		}
		seekers.add(seeker);
		roles.put(seeker, Role.SEEKER);
		return seeker;
	}
}