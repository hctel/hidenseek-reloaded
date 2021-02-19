package be.hctel.revhive.hidev2.enums;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum HideMap {
	/**
	 * javadoc
	 */
	HOTEL("HIDE_Hotel", "Hotel", new Location(Bukkit.getWorld("HIDE_Hotel"), -3.0, 61.5, -22.0, 180.1f, 0.0f), new Location(Bukkit.getWorld("HIDE_Hotel"), -3.0, 64.5, -35.0, 0.1f, 0.0f), "Room Service", "HOTEL"),
	HUMBUG("HIDE_Humbug", "Humbug St.", new Location(Bukkit.getWorld("HIDE_Humbug"), 29.5, 6.5, 52.5, 134.1f, 0.1f), new Location(Bukkit.getWorld("HIDE_Humbug"), 29.5, 20, 68.5, -180.1f, 0.1f), "Bah! Humbug", "HIDE_Humbug"),
	INDUSTRIA("HIDE_Industria", "Industria", new Location(Bukkit.getWorld("HIDE_Industria"), -8.5, 16, -23.5, 0.1f, 0.1f), new Location(Bukkit.getWorld("HIDE_Industria"), -33.5, 4,27.5, 0.1f, 0.1f), "Industry Pro", "INDUSTRIA"),
	SUNSET_TERRACE("HIDE_Sunset", "Sunset terrace", new Location(Bukkit.getWorld("HIDE_Sunset"), 105.5, 44, 16.5, 90.1f, 0.1f), new Location(Bukkit.getWorld("HIDE_Sunset"), 68.5, 25, 9.5, 0.1f, 0.1f),"The Sun goes Down", "HIDE_SunsetTerrace"),
	TEITAKU("HIDE_Teitaku", "Teitaku", new Location(Bukkit.getWorld("HIDE_Teitaku"), -160.5, 12, 447.5, 90.1f, 0.1f), new Location(Bukkit.getWorld("HIDE_Teitaku"), -192.5, 12, 429.5, 180.1f, 0.1f), "Sushiiiii", "HIDE_Teitaku"),
	SEQUOIA("HIDE_Sequoia", "Sequoia", new Location(Bukkit.getWorld("HIDE_Sequoia"), -91.5, 38, -2.5, -90.1f, 0.1f), new Location(Bukkit.getWorld("HIDE_Sequoia"), -79.5, 24, 48.5, 0.1f, 0.1f), "May the forest be with you", "HIDE_Sequoia"),
	HOTEL_CALIFORNIA("HIDE_HotelCalifornia", "Hotel California", new Location(Bukkit.getWorld("HIDE_HotelCalifornia"), 18.5, 25, 6.5, 180.1f, 0.1f), new Location(Bukkit.getWorld("HIDE_HotelCalifornia"), 18.5, 25, 0.5, 0.1f, 0.1f), "Welcome to Hotel California!", "HIDE_HotelCalifornia"),
	GOLDRUSH("HIDE_Goldrush", "Goldrush", new Location(Bukkit.getWorld("HIDE_Goldrush"), 21.5, 75.5, -11.5, 90.1f, 0.0f), new Location(Bukkit.getWorld("HIDE_Goldrush"), 2.5, 47.5, -12.5, 0.1f, 0.0f), "Gold! Gold! Gold everywhere!", "HIDE_Goldrush"),
	LOTUS("HIDE_Lotus", "Lotus", new Location(Bukkit.getWorld("HIDE_Lotus"), 12.5, 113, 0.5, 90.1f, 0.1f), new Location(Bukkit.getWorld("HIDE_Lotus"), -9.5, 126, 0.5, 0.1f, 0.1f), "Photosynthesis", "LOTUS"),
	VENICE_BRIDGE("HIDE_Venice", "Venice Bridge", new Location(Bukkit.getWorld("HIDE_Venice"), 43.5, 74, -46.5, 0.1f, 0.1f), new Location(Bukkit.getWorld("HIDE_Venice"), -8.0, 65, 6.0, 0.1f, 0.1f), "VEEE-NICE!", "VENICE");
	
	
	
	String mapName;
	String friendlyName;
	Location seekerStart;
	Location hiderSpawn; 
	String achievementName;
	String achievementIdentifier;
	
	private static final Map<String, HideMap> lookup = new HashMap<String, HideMap>();

    static {
        for (HideMap d : HideMap.values()) {
            lookup.put(d.getFriendlyName(), d);
        }
    }
	
	private HideMap(String mapName,String friendlyName,Location seekerStart, Location hiderSpawn, String achievementName, String achievementIdentifier) {
		this.mapName = mapName;
		this.friendlyName = friendlyName;
		this.seekerStart = seekerStart;
		this.hiderSpawn = hiderSpawn;
		this.achievementName = achievementName;
		this.achievementIdentifier = achievementIdentifier;
	}
	public String getWorldName() {
		return mapName;
	}
	public String getFriendlyName() {
		return friendlyName;
	}
	public Location getSeekerStart() {
		return new Location(Bukkit.getWorld(mapName), seekerStart.getX(), seekerStart.getY(), seekerStart.getZ(), seekerStart.getYaw(), seekerStart.getPitch());
	}
	public Location getSpawn() {
		return new Location(Bukkit.getWorld(mapName), hiderSpawn.getX(), hiderSpawn.getY(), hiderSpawn.getZ(), hiderSpawn.getYaw(), hiderSpawn.getPitch());
	}
	public String getAchievementName(HideMap map) {
		return achievementName;
	}
	public String getAchievementIdentiier(HideMap map) {
		return achievementIdentifier;
	}
	public HideMap getMapByFriendlyName(String name) {
		return lookup.get(name);
	}
	public Block[] getDefaultBlocks(HideMap map) {
		if(map == HideMap.HOTEL) {
			Block[] blocks = {Block.WOOD, Block.BEACON, Block.QUARTZ, Block.SNOW};
			return blocks;
		}
		else if(map == HideMap.SUNSET_TERRACE) {
			Block[] blocks = {Block.JUNGLEWOOD, Block.LEAVES, Block.BOOKSHELF, Block.MELON, Block.HARD_CLAY};
			return blocks;
		}
		else if(map == HideMap.SEQUOIA) {
			Block[] blocks = {Block.GREEN_CLAY, Block.JUNGLELOG, Block.BOOKSHELF, Block.LEAVES, Block.BIRCHWOOD, Block.SPRUCESTAIRS};
			return blocks;
		}
		else if(map == HideMap.HUMBUG) {
			Block[] blocks = {Block.BOOKSHELF, Block.JUKEBOX, Block.ANVIL, Block.WORKBENCH, Block.DARKOAKWOOD};
			return blocks;
		}
		else if(map == HideMap.INDUSTRIA) {
			Block[] blocks = {Block.WORKBENCH, Block.BOOKSHELF, Block.ANVIL, Block.JUKEBOX, Block.BEACON, Block.FLOWER_POT};
			return blocks;
 		}
		else if(map == HideMap.HOTEL_CALIFORNIA) {
			Block[] blocks = {Block.RED_SANDSTONE, Block.FLOWER_POT, Block.LEAVES, Block.BEACON, Block.BOOKSHELF, Block.SPRUCEWOOD};
			return blocks;
		}
		else if(map == HideMap.GOLDRUSH) {
			Block[] blocks = {Block.ANVIL, Block.HAYBLOCK, Block.GOLD_ORE, Block.WORKBENCH, Block.BIRCHWOOD};
			return blocks;
		}
		else if(map == HideMap.LOTUS) {
			Block[] blocks = {Block.WORKBENCH, Block.BEACON, Block.BOOKSHELF, Block.ANVIL, Block.FURNACE};
			return blocks;
 		}
		else if(map == HideMap.VENICE_BRIDGE) {
			Block[] blocks = {Block.FLOWER_POT, Block.JUKEBOX};
			return blocks;
		}
		else {
			Block[] blocks = {Block.FLOWER_POT};
			return blocks;
		}
	}
	public Block[] getDisabledBlockstBlocks(HideMap map) {
		if(map == HideMap.HOTEL) {
			Block[] blocks = {};
			return blocks;
		}
		else if(map == HideMap.SUNSET_TERRACE) {
			Block[] blocks = {};
			return blocks;
		}
		else if(map == HideMap.SEQUOIA) {
			Block[] blocks = {};
			return blocks;
		}
		else if(map == HideMap.HUMBUG) {
			Block[] blocks = {};
			return blocks;
		}
		else if(map == HideMap.INDUSTRIA) {
			Block[] blocks = {};
			return blocks;
 		}
		else if(map == HideMap.HOTEL_CALIFORNIA) {
			Block[] blocks = {};
			return blocks;
		}
		else if(map == HideMap.GOLDRUSH) {
			Block[] blocks = {};
			return blocks;
		}
		else if(map == HideMap.LOTUS) {
			Block[] blocks = {};
			return blocks;
 		}
		else if(map == HideMap.VENICE_BRIDGE) {
			Block[] blocks = {};
			return blocks;
		}
		else {
			Block[] blocks = {};
			return blocks;
		}
	}
	
}
