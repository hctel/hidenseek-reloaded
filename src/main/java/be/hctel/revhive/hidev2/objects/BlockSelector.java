package be.hctel.revhive.hidev2.objects;

import java.util.ArrayList;
import java.util.HashMap;

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

import be.hctel.revhive.hidev2.enums.Block;
import be.hctel.revhive.hidev2.enums.HideMap;

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
		Block[] baseBlocks = map.getDefaultBlocks(map);
		ArrayList<Block> customBlocks = stats.getBlocks(player);
		for (Block block : map.getDisabledBlockstBlocks(map)) {
			if(customBlocks.contains(block)) customBlocks.remove(block);
		}
		int blockSize = baseBlocks.length + customBlocks.size();
		int invSize = 54;
		if(blockSize <= 9) {
			invSize = 18;
		}
		if(blockSize <= 18) {
			invSize = 27;
		}
		if(blockSize <= 27) {
			invSize = 36;
		}
		if(blockSize <= 36) {
			invSize = 45;
		}
		if(blockSize <= 45) {
			invSize = 54;
		}
		if(blockSize > 45) {
			invSize = 54;
		}
		Inventory inv = Bukkit.createInventory(null, invSize, "Choose your Block!");
		for(int i = 0; i < baseBlocks.length-1; i++) {
			ItemStack toAdd = new ItemStack(baseBlocks[i].getMaterial());
			ItemMeta meta = toAdd.getItemMeta();
			meta.setDisplayName("§e§l" + baseBlocks[i].getFriendlyName());
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§7Will this " + baseBlocks[i].getFriendlyName() + " to");
			lore.add("§7a good choice?");
			lore.add("");
			lore.add("§b§lBlock level");
			lore.add(stats.getBlockLevel(player, baseBlocks[i]));
			lore.add("§b► Click to select");
			meta.setLore(lore);
			toAdd.setItemMeta(meta);
			inv.setItem(i, toAdd);
		}
		if(!customBlocks.isEmpty()) for(int i = 0; i < customBlocks.size()-1; i++) {
			ItemStack toAdd = new ItemStack(customBlocks.get(i).getMaterial());
			ItemMeta meta = toAdd.getItemMeta();
			meta.setDisplayName("§e§l" + customBlocks.get(i).getFriendlyName());
			meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§7Will this " + customBlocks.get(i).getFriendlyName() + " to");
			lore.add("§7a good choice?");
			lore.add("");
			lore.add("§b§lBlock level");
			lore.add(stats.getBlockLevel(player, customBlocks.get(i)));
			lore.add("§b► Click to select");
			meta.setLore(lore);
			toAdd.setItemMeta(meta);
			inv.setItem(i+18, toAdd);
		}
		player.openInventory(inv);
	}
	@SuppressWarnings("deprecation")
	public void listener(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		Block b;
		if(e.getCurrentItem() == null) return;
		if(e.getCurrentItem().getData().getData() == 0) b = Block.getByJSONName(e.getCurrentItem().getType().toString());
		else b = Block.getByJSONName(e.getCurrentItem().getType().toString() + ":" + e.getCurrentItem().getData().getData());
		block.put(player, b);
		//manager.addPlayer(player, b);
		player.closeInventory();
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1f);
		player.sendRawMessage("§8▍ §bHide§aAnd§eSeek§8 ▏ §aGood choice! §eSet yout block to " + b.getFriendlyName());
	}
	public Block getBlock(Player player) {
		return block.get(player);
	}
}