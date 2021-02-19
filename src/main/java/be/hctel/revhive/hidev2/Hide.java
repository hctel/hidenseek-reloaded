package be.hctel.revhive.hidev2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import be.hctel.revhive.hidev2.commands.CommandVote;
import be.hctel.revhive.hidev2.commands.Commands;
import be.hctel.revhive.hidev2.enums.HideMap;
import be.hctel.revhive.hidev2.listeners.Listeners;
import be.hctel.revhive.hidev2.objects.BlockSelector;
import be.hctel.revhive.hidev2.objects.GameEventsManager;
import be.hctel.revhive.hidev2.objects.MapSelector;
import be.hctel.revhive.hidev2.objects.StartTimer;
import be.hctel.revhive.hidev2.objects.Stat;

public class Hide extends JavaPlugin {
	/*
	 * Variables for general tasks/methods
	 */
	public static Plugin plugin;
	public static BukkitScheduler scheduler;
	
	/*
	 * Variables for SQL connection used by stats
	 */
	private Connection con;
	private String host, user, password, database;
	private int port;
	
	/*
	 * Internal objects, to make the game playable
	 */
	public static MapSelector selector;
	public static BlockSelector blockSelect;
	public static Stat stats;
	public static StartTimer timer;
	public static GameEventsManager gameManager;
	
	@Override
	public void onEnable() {
		saveDefaultConfig(); 									//Makes sure the file exists and is loaded
		plugin = this;
		loadWorlds();											//Loads the worlds
		loadSQLCredentials();
		openConnection(host, port, user, password, database);	//Opens the SQL connection
		scheduler = Bukkit.getScheduler();
		selector = new MapSelector(HideMap.values());			//Defining internal and general variables
		stats = new Stat(plugin, con);
		timer = new StartTimer(scheduler, plugin);
		gameManager = new GameEventsManager(this, scheduler, stats);
		Bukkit.getPluginManager().registerEvents(new Listeners(), plugin);//Registering listeners
		getCommand("vote").setExecutor(new CommandVote());		//Registering commands
		getCommand("v").setExecutor(new CommandVote());
	}
	
	public void openConnection(String host, int port, String user, String password, String database) {
		try {
			if (con != null && !con.isClosed()) {
			    return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	    synchronized (this) {
	        try {
				if (con != null && !con.isClosed()) {
				    return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
	        	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?autoReconnect=true&useSSL=false","root","1234");
					System.out.println("SQL is operational!");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}	
	public static String getUUID(Player player) {
		return player.getUniqueId().toString().replace("-", "");
	}
	private void loadWorlds() {
		ArrayList<World> worldList = new ArrayList<World>();
		for(HideMap wName: HideMap.values()) {
			worldList.add(Bukkit.createWorld(new WorldCreator(wName.getWorldName())));
		}
		for(World w : worldList) {
			Bukkit.getWorlds().add(w);
		}
		for(World w : Bukkit.getWorlds()) {
			System.out.println(w.toString());
		}
		getCommand("about").setExecutor(new Commands());
	}
	private void loadSQLCredentials() {
		this.database = getConfig().getString("SQLCred.database");
		this.user = getConfig().getString("SQLCred.user");
		this.password = getConfig().getString("SQLCred.pass");
		this.host = getConfig().getString("SQLCred.host");
		this.port = getConfig().getInt("SQLCred.port");		
	}
}