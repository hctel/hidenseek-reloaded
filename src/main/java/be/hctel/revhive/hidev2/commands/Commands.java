package be.hctel.revhive.hidev2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("about")) {
				player.sendMessage("HIDEandSEEK-recode. Coded by ThinkPad_-hctel in February 2021. Original game creation by play.hivemc.com.");
			}
		}
		return false;
	}

}
