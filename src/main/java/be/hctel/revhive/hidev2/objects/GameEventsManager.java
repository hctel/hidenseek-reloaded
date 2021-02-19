package be.hctel.revhive.hidev2.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import be.hctel.revhive.hidev2.Hide;
import be.hctel.revhive.hidev2.enums.Block;
import be.hctel.revhive.hidev2.enums.Role;

public class GameEventsManager {
	private Stat stats;
	private Plugin plugin;
	private BukkitScheduler scheduler;
	public int ticksRemaining = 6000;
	public int secondsRemaining = 300;
	public boolean gameHasStarted = false;
	private HashMap<Player, Role> roles = new HashMap<Player, Role>();
	private HashMap<Player, Location> lastSecondLocation = new HashMap<Player, Location>();
	private HashMap<Player, PlayerBlock> blocks = new HashMap<Player, PlayerBlock>();
	private HashMap<Player, Block> playerblock = new HashMap<Player, Block>();
	private HashMap<Player, Integer> killedAt = new HashMap<Player, Integer>();
	private ArrayList<Player> queuedSeekers = new ArrayList<Player>();
	private ArrayList<Player> hiders = new ArrayList<Player>();
	private ArrayList<Player> seekers = new ArrayList<Player>();
	
	
	public GameEventsManager(Plugin plugin, BukkitScheduler scheduler, Stat stats) {
			this.stats = stats;
			this.plugin = plugin;
			this.scheduler = scheduler;
	}
	
	public void startGame() {
		pickSeeker();
		this.gameHasStarted = true;
		this.scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				
			}
		}, 0L, 20L);
		this.scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			public void run() {
				/*
				 * tick code, teleport blocks, check for level ups, check if hiders are still alive
				 */
			}
		}, 0L, 1L);
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
				Bukkit.broadcastMessage(Hide.stats.getBlockLevel(seeker, playerblock.get(seeker)) + " " + playerblock.get(seeker).getFriendlyName()+ " §9" + seeker.getName() + " §6has been made a seeker §7(seeker struggling)");
			}
		}
		return seeker;
	}
}