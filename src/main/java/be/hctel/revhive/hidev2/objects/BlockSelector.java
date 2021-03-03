package be.hctel.revhive.hidev2.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONException;

import be.hctel.revhive.hidev2.Hide;
import be.hctel.revhive.hidev2.enums.Block;
import be.hctel.revhive.hidev2.enums.HideMap;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;

public class BlockSelector {
	private Stat stats;
	private HideMap map;

	private HashMap<Player, Block> block = new HashMap<Player, Block>();
	public BlockSelector(Stat stats, HideMap map) {
		this.stats = stats;
		this.map = map;
	}
	public void setMap(HideMap map) {
		this.map = map;
		System.out.println(map.toString());
	}
	public void openBlockSelector(Player player) throws JSONException {
		System.out.println("Creating and opening block selector for " + player.getName());
		ArrayList<Block> baseBlocks = new ArrayList<Block>();
		for(Block block : map.getDefaultBlocks(map)) {
			baseBlocks.add(block);
			System.out.println("Adding block " + block + " to baseBlocks");
		}
		ArrayList<Block> customBlocks = stats.getBlocks(player);
		for(Block block : map.getDisabledBlockstBlocks(map)) {
			if(customBlocks.contains(block)) customBlocks.remove(block); System.out.println("checking and removing from CustomBlocks if necessary " + block);
		}
		for(Block block : customBlocks) {
			if(baseBlocks.contains(block)) baseBlocks.remove(block);
		}
		int i = baseBlocks.size() + customBlocks.size() + 9;
		int invSize = 54;
		System.out.println("Setting inv size");
		if(i > 45) {
			invSize = 54;
		}
		else if (i <= 45 && i > 36) {
			invSize = 54;
		}
		else if (i <= 36 && i > 27) {
			invSize = 45;
		}
		else if (i <= 27 && i > 18) {
			invSize = 36;
		}
		else if (i <= 18 && i > 9) {
			invSize = 27;
		}
		else if (i <= 9 && i > 0) {
			invSize = 18;
		}
		Inventory inv = Bukkit.createInventory(null, invSize, "Choose your block!");
		int a = 0;
		for(Block b : baseBlocks) {
			ItemStack it = b.getItemStack(b);
			ItemMeta meta = it.getItemMeta();
			meta.setDisplayName("§e§l" + b.getFriendlyName());
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§7Will this " + b.getFriendlyName() + " to");
			lore.add("§7a good choice?");
			lore.add("");
			lore.add(stats.getBlockLevel(player, b) + " §7" + (stats.getRawBlockExperience(player, baseBlocks.get(a))-stats.getBlockLvl(player, baseBlocks.get(a))*50 + "§r/" + (stats.getBlockLvl(player, baseBlocks.get(a))*50)));
			lore.add("");
			meta.setLore(lore);
			it.setItemMeta(meta);
			System.out.println(it.toString());
			inv.setItem(a, it);
			System.out.println(a);
			a++;
		}
		int c = 18;
		for(Block b : customBlocks) {
			ItemStack it = b.getItemStack(b);
			ItemMeta meta = it.getItemMeta();
			meta.setDisplayName("§e§l" + b.getFriendlyName());
			meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.setLore(new ArrayList<String>());
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§7Will this " + b.getFriendlyName() + " to");
			lore.add("§7a good choice?");
			lore.add("");
			lore.add(stats.getBlockLevel(player, b) + " §7" + (stats.getRawBlockExperience(player, b)-stats.getBlockLvl(player, b)*50 + "§r/" + (stats.getBlockLvl(player, b)*50)));
			lore.add("");
			meta.setLore(lore);
			it.setItemMeta(meta);
			System.out.println(it.toString());
			inv.setItem(c, it);
			System.out.println(c);
			c++;
		}
		player.openInventory(inv);
		System.out.println("Inventory opened");
		Random r = new Random();
		int s1 = r.nextInt(1);
		if(s1 == 0) {
			int s2 = r.nextInt(baseBlocks.size()-1);
			block.put(player, baseBlocks.get(s2));
		} else {
			int s2 = r.nextInt(customBlocks.size()-1);
			block.put(player, customBlocks.get(s2));
		}
	}
	@SuppressWarnings("deprecation")
	public void listener(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		Block b;
		if(e.getCurrentItem().equals(null)) return;
		if(e.getCurrentItem().getData().getData() == 0) b = Block.getByJSONName(e.getCurrentItem().getType().toString());
		else b = Block.getByJSONName(e.getCurrentItem().getType().toString() + ":" + e.getCurrentItem().getData().getData());
		block.replace(player, b);
		//manager.addPlayer(player, b);
		player.closeInventory();
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1f);
		player.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §aGood choice! §eSet yout block to §f" + b.getFriendlyName());
	}
	public Block getBlock(Player player) {
		return block.get(player);
	}
}