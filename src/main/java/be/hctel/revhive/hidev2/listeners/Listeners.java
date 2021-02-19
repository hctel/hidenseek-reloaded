package be.hctel.revhive.hidev2.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import be.hctel.revhive.hidev2.Hide;

public class Listeners implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		try {
			Hide.stats.addPlayer(e.getPlayer());
			if(Hide.timer.gameHasStarted) {
				e.setJoinMessage(null);
			} else {
				e.setJoinMessage("§9"+e.getPlayer().getName() + Hide.stats.getJoinMessage(e.getPlayer()));
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
		String header = Hide.stats.getRankHeader(player);
		if(Hide.timer.gameHasStarted) {
			msg = header + " §9" + e.getPlayer().getName() + " §7§l » + §r" + e.getMessage();
		} else {
			int points = Hide.stats.getPoints(player);
			msg = "§e" + points + " §7▍ " + header + " §9" + e.getPlayer().getName() + " §7§l » §r" + e.getMessage();
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
		}
		else if(hitted instanceof FallingBlock && damager instanceof Player) {
			//code solid block hitten
		}
		else if(hitted instanceof Player && damager instanceof Player) {
			//code seeker hit
		}
	}
}
