package be.hctel.revhive.hidev2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.revhive.hidev2.Hide;

public class CommandVote implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 1) {
				if(args[0].equals("1") || args[0].equals("2") || args[0].equals("3") || args[0].equals("4") || args[0].equals("5") || args[0].equals("6")) Hide.selector.registerPlayerVote(player, Integer.parseInt(args[0]));
				else {
					player.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §c" + args[0] + " is not an option!");
				}
			} else {
				Hide.selector.sendMapChoices(player);
			}
			
		}
		return false;
	}

}
