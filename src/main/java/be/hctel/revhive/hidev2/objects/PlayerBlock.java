package be.hctel.revhive.hidev2.objects;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import be.hctel.revhive.hidev2.Hide;
import be.hctel.revhive.hidev2.enums.Block;
import be.hctel.revhive.hidev2.enums.Role;

public class PlayerBlock {
	
	public  FallingBlock block;
	private  Player player;
	private   Material material;
	public  Location solidBlockLocation;
	public  Block getBlock;
	public  byte data;
	public  boolean isSolid = false;
	public  boolean isAlive = true;
	private  FallingBlock b;
	public  FallingBlock a;
	private ArmorStand vehicle;
	public  Role getRole = Role.HIDER;
	@SuppressWarnings("deprecation")
	public PlayerBlock(Player player, Block block) {
		this.player = player;
		this.getBlock = block;
		material = getBlock.getMaterial();
		data = (byte) getBlock.getData();
		System.out.println(getBlock.getMaterial()+ " " + getBlock.getFriendlyName());
		System.out.println(player.getLocation().toString());
		
	}
	
	public void spawnBlock() {
		vehicle = spawnArmor(player.getLocation().add(0, -1, 0), Block.FLOWER_POT);
		a = spawnFalling(player.getLocation(), getBlock);
		vehicle.addPassenger(a);
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(p != player && p.getGameMode() != GameMode.SPECTATOR) {
				p.hidePlayer(Hide.plugin, player);
			}
		}
		
	}
	public  void teleportBlock() {
		Location teleportTo = player.getLocation();
		teleportTo.setYaw(0);
		teleportTo.add(0, -1, 0);
		vehicle.teleport(teleportTo);
	}
	public  String getMaterialName() {
		return material.toString();
	}
	@SuppressWarnings("deprecation")
	public  void solid() {
		solidBlockLocation = player.getLocation().add(0, -1, 0);
		if(solidBlockLocation.getBlock().getType() != Material.AIR) {
			player.sendTitle("§c§l🗙 §6§lYou can't be solid here", "Find another spot to hide", 0, 40, 20);
			return;
		}
		isSolid = true;
		player.removePassenger(block);
		a.remove();
		vehicle.remove();
		b = spawn(solidBlockLocation, getBlock);
		b.setGravity(false);
		b.setHurtEntities(false);
		b.setDropItem(false);
		for(Player p :Bukkit.getOnlinePlayers()) {
			if(p != this.player) {
				p.sendBlockChange(solidBlockLocation, material, data);
			}
		}
		player.sendTitle("§6You are now §a§lhidden§6!", "", 0, 40, 20);
	}
	public  void notSolid() {
		if(isSolid == true) {
			isSolid = false;
			b.remove();
			spawnBlock();
			player.sendTitle("§6You are now §c§lvisible§6!", "", 0, 40, 20);
		}
	}
	@SuppressWarnings("deprecation")
	public  void isKilled() {
		getRole = Role.SEEKER;
		isAlive = false;
		isSolid=false;
		a.remove();
		b.remove();
		for(Player p : player.getLocation().getWorld().getPlayers()) {
			if(p != player && p.getGameMode() != GameMode.SPECTATOR) {
				p.showPlayer(player);
			}
		}
	}
	@SuppressWarnings("deprecation")
	private  FallingBlock spawn(Location loc, Block block) {
		if(block.getData() != 0) {
			return loc.getWorld().spawnFallingBlock(loc, getBlock.getMaterial(), (byte) getBlock.getData());
		}
		return loc.getWorld().spawnFallingBlock(loc, getBlock.getMaterial(), (byte) 0x00);
	}
	
	@SuppressWarnings("unused")
	private  ArmorStand spawnArmor(Location l, Block block) {
		Location loc = l.add(0, -1, 0);
		ArmorStand ar = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		ar.setVisible(false);
		ar.setGravity(false);
		ar.setBasePlate(false);
		ar.setCustomName(player.getName());
		ar.setCustomNameVisible(false);
		//ar.setHelmet(block.getItemStack(block));
		ar.setInvulnerable(true);
		ar.setSmall(false);
		ar.setCanPickupItems(false);
		ar.setSilent(true);
		return ar;
	}
	private FallingBlock spawnFalling(Location l, Block block) {
		Location loc = l.add(0, -1, 0);
		@SuppressWarnings("deprecation")
		FallingBlock bl = l.getWorld().spawnFallingBlock(loc, block.getMaterial(), (byte) block.getData());
		bl.setGravity(false);
		bl.setInvulnerable(false);
		return bl;
	}
	public static PlayerBlock createPlayerBlock(Player player,Block block) {
		return new PlayerBlock(player, block);
	}
	
}