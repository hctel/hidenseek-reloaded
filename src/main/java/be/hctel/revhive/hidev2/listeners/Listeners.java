package be.hctel.revhive.hidev2.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import be.hctel.revhive.hidev2.Hide;
import be.hctel.revhive.hidev2.enums.HideAchievement;
import be.hctel.revhive.hidev2.enums.Role;
import be.hctel.revhive.revhiveutils.ranksystem.Rank;

public class Listeners implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Hide.ranks.join(e.getPlayer());
		try {
			Hide.stats.addPlayer(e.getPlayer());
			if(Hide.timer.gameHasStarted) {
				e.setJoinMessage(null);
				if(Hide.ranks.getRank(e.getPlayer()) == Rank.REGULAR || Hide.ranks.getRank(e.getPlayer()) == Rank.GOLD || Hide.ranks.getRank(e.getPlayer()) == Rank.DIAMOND || Hide.ranks.getRank(e.getPlayer()) == Rank.EMERALD || Hide.ranks.getRank(e.getPlayer()) == Rank.ULTIMATE) {
					e.getPlayer().kickPlayer("§cOnly staff members can spectate in Hide&Seek");
				} else {
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.hidePlayer(Hide.plugin, e.getPlayer());
					}
				}
			} else {
				e.setJoinMessage(Hide.ranks.getRankColorCode(e.getPlayer()) + e.getPlayer().getName() + Hide.stats.getJoinMessage(e.getPlayer()));
				e.getPlayer().teleport(new Location(Bukkit.getWorld("HIDE_Lobby"), -79, 90, 61, 0.1f, 0.1f));
				Hide.selector.sendMapChoices(e.getPlayer());
				for(Player player : Bukkit.getOnlinePlayers()) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						
					}
				}
			}
		} catch (SQLException e1) {
			e.setJoinMessage(null);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		String msg;
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(e.getMessage().contains(p.getName())) {
				p.playSound(p.getLocation() , Sound.BLOCK_NOTE_PLING, 1.0f, 1.5f);
				p.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ " + Hide.ranks.getRankColorCode(player) + player.getName() + " §6has pinged you in their message. ↓");
			}
		}
		String header = Hide.stats.getRankHeader(player);
		if(Hide.timer.gameHasStarted) {
			msg = header + " " + Hide.ranks.getRankColorCode(player) + e.getPlayer().getName() + " §7§l » + §r" + e.getMessage();
		} else {
			int points = Hide.stats.getPoints(player);
			msg = "§e" + points + " §7▍ " + header + " " + Hide.ranks.getRankColorCode(player) + e.getPlayer().getName() + " §7§l » §r" + e.getMessage();
		}
		e.setCancelled(true);
		Bukkit.broadcastMessage(msg);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		Entity hitted = e.getEntity();
		Entity damager = e.getDamager();
		if(hitted instanceof ArmorStand && damager instanceof Player) {
			//code not solid block hitted
			Player h;
			for(Player player : Hide.gameManager.hiders) {
				if(player.getLocation().equals(hitted.getLocation())) {
					h = player;
					if(h.getHealth() > 5) {
						h.setHealth(h.getHealth()-5);
						h.getWorld().playSound(h.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
					} else {
						Hide.gameManager.killHider(h, (Player) damager);
						h.getWorld().playSound(h.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
						h.getWorld().playSound(h.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
					}
				}
			}
		}
		else if(hitted instanceof FallingBlock && damager instanceof Player) {
			Player h;
			for(Player player : Hide.gameManager.hiders) {
				if(player.getLocation().equals(hitted.getLocation())) {
					h = player;
					Hide.gameManager.getPlayerBlock(h).notSolid();
					if(h.getHealth() > 5) {
						h.setHealth(h.getHealth()-5);
						h.getWorld().playSound(h.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
					} else {
						Hide.gameManager.killHider(h, (Player) damager);
						h.getWorld().playSound(h.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
						h.getWorld().playSound(h.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
					}
				}
			}
		}
		else if(hitted instanceof Player && damager instanceof Player) {
			if(!(Hide.gameManager.getRole((Player) hitted) == Role.SEEKER)) {
				e.setCancelled(true);
			}
		}
		else e.setCancelled(true);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		System.out.println("inventoryClickEvent trigerred!");
		ItemStack clicked = e.getCurrentItem();
		Inventory inv = e.getInventory();
		if(inv.getName().equals("Choose your block!")) {
			Hide.blockSelect.listener(e);
		}
	}
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getCause().equals(DamageCause.FALL) && e.getEntity() instanceof Player) {
			e.setCancelled(true);
		} else if(e.getCause().equals(DamageCause.PROJECTILE) && e.getEntity() instanceof Player) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Hide.gameManager.killSeeker(e.getEntity(), e.getEntity().getKiller());
		e.setDeathMessage(null);
		Hide.stats.addPoints(e.getEntity().getKiller(), 30);
		if(!Hide.stats.getAchievements(e.getEntity().getKiller()).contains(HideAchievement.SEEKER1)) {
			Hide.stats.unlockAchievement(e.getEntity().getKiller(), HideAchievement.SEEKER1);
		}
	}
	@EventHandler
	public void blockForm(BlockFormEvent e) {
		e.setCancelled(true);
	}
	
}
