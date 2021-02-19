package be.hctel.revhive.hidev2.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Block {

    LEAVES("Leaves", Material.LEAVES, "LEAVES"),
    OBSIDIAN("Obsidian", Material.OBSIDIAN, "OBSIDIAN"),
    REDSTONE("Redstone block", Material.REDSTONE_BLOCK, "REDSTONE_BLOCK"),
    ICE("Ice", Material.PACKED_ICE, "ICE"),
    PUMPKIN("Pumpkin", Material.PUMPKIN, "PUMPKIN"),
    IRON("Iron block", Material.IRON_BLOCK, "IRON_BLOCK"),
    TNT("TNT", Material.TNT, "TNT"),
    LAPIS("Lapis-lazuli block", Material.LAPIS_BLOCK, "LAPIS_BLOCK"),
    QUARTZORE("Quartz ore", Material.QUARTZ_ORE, "QUARTZ_ORE"),
    REDSTONEORE("Redstone ore", Material.REDSTONE_ORE, "REDSTONE_ORE"),
    SOULSNAD("Soul sand", Material.SOUL_SAND, "SOUL_SAND"),
    JACKOLANTERN("Jack 'o' lantern", Material.JACK_O_LANTERN, "JACK_O_LANTERN"),
    JUKEBOX("Jukebox", Material.JUKEBOX, "JUKEBOX"),
    BOOKSHELF("Bookshelf", Material.BOOKSHELF, "BOOKSHELF"),
    MELON("Melon block", Material.MELON_BLOCK, "MELON_BLOCK"),
    REDSTONELAMP("Redstone lamp (off)", Material.REDSTONE_LAMP_OFF, "REDSTONE_LAMP_OFF"),
    ENDPORTAL("End portal frame", Material.ENDER_PORTAL_FRAME, "ENDER_PORTAL_FRAME"),
    ENCHANT("Enchantment table", Material.ENCHANTMENT_TABLE, "ENCHANTMENT_TABLE"),
    DIAMOND("Diamond block", Material.DIAMOND_BLOCK, "DIAMOND_BLOCK"),
    EMERALD("Emerald block", Material.EMERALD_BLOCK, "EMERALD_BLOCK"),
	GRANITE("Polished granite", Material.STONE, 2, "STONE:2"),
	STONE("Stone block", Material.STONE, "STONE"),
	RED_SANDSTONE("Red sandstone", Material.RED_SANDSTONE, "RED_SANDSTONE"),
	HARD_CLAY("Hardened clay", Material.HARD_CLAY, "HARD_CLAY"),
	GOLD_ORE("Gold ore", Material.GOLD_ORE, "GOLD_ORE"),
	GREEN_CLAY("Green clay", Material.STAINED_CLAY, 5, "STAINED_CLAY:5"),
	ANDESITE("Polished andesite", Material.STONE, 6, "STONE:6"),
	BEACON("Beacon", Material.BEACON, "BEACON"),
	DARKSTAIRS("Dark Oak Stairs", Material.DARK_OAK_STAIRS, "DARK_OAK_STAIRS"),
	WORKBENCH("Crafting Table", Material.WORKBENCH, "WORKBENCH"),
	WOOL("Wool", Material.WOOL, "WOOD"),
	WOOD("Oak wood planks", Material.WOOD, "WOOD"),
	CAKE("Cake", Material.CAKE_BLOCK, "CAKE_BLOCK"),
	NOTEBLOCK("Noteblock", Material.NOTE_BLOCK, "NOTE_BLOCK"),
	PRISMARINE("Prismarine", Material.PRISMARINE, 2, "PRISMARINE:2"),
	CAULDRON("Cauldron", Material.CAULDRON, "CAULDRON"),
	JUNGLEWOOD("Jungle planks", Material.WOOD, 3, "WOOD:3"),
	BRICKS("Bricks", Material.BRICK, "BRICK"),
	BIRCHWOOD("Birch planks", Material.WOOD, 2, "WOOD:2"),
	ANVIL("Anvil", Material.ANVIL, "ANVIL"),
	SPRUCEWOOD("Spruce planks", Material.WOOD, 1, "WOOD:1"),
	FURNACE("Furnace", Material.FURNACE, "FURNACE"),
	COAL("Coal block", Material.COAL_BLOCK, "COAL_BLOCK"),
	QUARTZSTAIRS("Quartz stairs", Material.QUARTZ_STAIRS, "QUARTZ_STAIRS"),
	LOG("Oak log", Material.LOG, "LOG"),
	JUNGLELOG("Jungle log", Material.LOG, 3, "LOG:3"),
	HOPPER("Hopper", Material.HOPPER, "HOPPER"),
	HAYBLOCK("Hay block", Material.HAY_BLOCK, "HAY_BLOCK"),
	PISTON("Piston", Material.PISTON_BASE, "PISTON_BASE"),
	FLOWER_POT("Flower pot", Material.FLOWER_POT, "FLOWER_POT"),
	SNOW("Snow block", Material.SNOW_BLOCK, "SNOW_BLOCK"),
	QUARTZ("Quartz block", Material.QUARTZ_BLOCK, "QUARTZ_BLOCK"),
	DISPENSER("Dispenser", Material.DISPENSER, "DISPENSER"),
	DARKOAKWOOD("Dark oak planks", Material.WOOD, 5, "WOOD:5"),
	SPRUCESTAIRS("Spruce stairs", Material.SPRUCE_WOOD_STAIRS, "SPRUCE_WOOD_STARIS");
    
    String friendlyName;
    Material material;
    int data;
    String JSONName;
    private static final Map<String, Block> lookup = new HashMap<String, Block>();

    static {
        for (Block d : Block.values()) {
            lookup.put(d.getJsonName(d), d);
        }
    }
    private static final Map<Material, Block> lookupMaterial = new HashMap<Material, Block>();

    static {
        for (Block d : Block.values()) {
            lookupMaterial.put(d.getMaterial(), d);
        }
    }
    
    private Block(String friendlyName, Material material, String JSONName) {
        this.friendlyName = friendlyName;
        this.material = material;
        this.data = 0;
        this.JSONName = JSONName;
    }
    private Block(String friendlyName, Material material, int data, String JSONName) {
    	this.friendlyName = friendlyName;
        this.material = material;
        this.data = data;
        this.JSONName = JSONName;
    }
    
    public String getFriendlyName(Block block) {
        return friendlyName;
    }
    
    @SuppressWarnings("deprecation")
	public static ItemStack getItemStack(Block block) {
    	if(block.getData() == 0) {
    		return new ItemStack(block.getMaterial());
    	}
    	else {
    		return new ItemStack(block.getMaterial(), 2, (short) 0, (byte) block.getData());
    	}
        
    }
    
    public String getFriendlyName() {
        return "§7" + friendlyName + " §r";
    }
    
    public Material getMaterial() {
        return material;
    }
    /**
     * @deprecated Returns 0 if the blockData is 0. Can cause error if using ItemStack constructor
     * @return
     */
    public int getData() {
    	return data;
    }
    
    public String getJsonName(Block block) {
    	return JSONName;
    }
    
    public static Block getByJSONName(String JSONName) {
    	return lookup.get(JSONName);
    }
    
    public ArrayList<Block> getBuyableBlocks() {
    	ArrayList<Block> out = new ArrayList<Block>();
    	out.add(Block.OBSIDIAN);
    	out.add(Block.QUARTZORE);
    	out.add(Block.REDSTONE);
    	out.add(Block.REDSTONEORE);
    	out.add(Block.SOULSNAD);
    	out.add(Block.JACKOLANTERN);
    	out.add(Block.JUKEBOX);
    	out.add(Block.LAPIS);
    	out.add(Block.ICE);
    	out.add(Block.BOOKSHELF);
    	out.add(Block.MELON);
    	out.add(Block.PUMPKIN);
    	out.add(Block.REDSTONELAMP);
    	out.add(Block.LEAVES);
    	out.add(Block.ENDPORTAL);
    	out.add(Block.ENCHANT);
    	out.add(Block.IRON);
    	out.add(Block.DIAMOND);
    	out.add(Block.EMERALD);
    	out.add(Block.TNT);
    	return out;
    }
}