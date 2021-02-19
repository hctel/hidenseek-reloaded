package be.hctel.revhive.hidev2.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

import be.hctel.revhive.hidev2.enums.Block;
import be.hctel.revhive.hidev2.enums.DefaultFontInfo;
import be.hctel.revhive.hidev2.enums.HideAchievement;

public class Stat {
	Plugin plugin;
	Connection con;
	HashMap<Player, JSONObject> jsons = new HashMap<Player, JSONObject>();
	
 	public Stat(Plugin plugin, Connection con) {
		this.plugin = plugin;
		this.con = con;
	}
 	
 	public void addPlayer(Player player) throws SQLException, JSONException {
 		Statement st = con.createStatement();
 		String baseline = "{\"UUID\":\"" + getUUID(player) + "\",\"total_points\":0,\"victories\":0,\"hiderkills\":0,\"seekerkills\":0,\"deaths\":0,\"gamesplayed\":0,\"blocks\":\"\",\"bookupgrade\":null,\"timealive\":0,\"rawBlockExperience\":{},\"blockExperience\":{},\"achievements\":{\"SEEKER25\":{\"progress\":0,\"unlockedAt\":0},\"SEEKER1\":{\"progress\":0,\"unlockedAt\":0},\"SETINPLACE\":{\"progress\":0,\"unlockedAt\":0},\"HIDER500\":{\"progress\":0,\"unlockedAt\":0},\"HIDER250\":{\"progress\":0,\"unlockedAt\":0},\"HIDER50\":{\"progress\":0,\"unlockedAt\":0},\"HIDER1000\":{\"progress\":0,\"unlockedAt\":0},\"HIDER1\":{\"progress\":0,\"unlockedAt\":0},},\"lastlogin\":" + new Date().getTime() +",\"firstlogin\":" + new Date().getTime() + ",\"title\":\"Blind\"}";
 		String uuid = getUUID(player);		
		String url = "https://api.hivemc.com/v1/player/" + uuid + "/HIDE";
		if(getJSON(url, 3000) != "{\"code\":404,\"short\":\"noprofile\",\"message\":\"No HIDE profile exists for unknown\"}") {
			baseline = getJSON(url, 3000);
			ResultSet rs = st.executeQuery("SELECT * FROM HIDE WHERE UUID = '" + uuid + "';");
			if(!rs.next()) {
				st.execute("INSERT INTO HIDE (UUID, JSON, unlockedJoinMessage, usedJoinMessage) VALUES ('" + uuid + "','" + baseline + "', 1000000, 1);");
				JSONObject toAdd = new JSONObject(baseline);
				jsons.put(player, toAdd);
			} else {
				ResultSet rs1 = st.executeQuery("SELECT * FROM HIDE WHERE UUID = '" + uuid + "';");
				rs1.next();
				JSONObject toAdd = new JSONObject(rs1.getString(2));
				jsons.put(player, toAdd);
			}
		} else {
			ResultSet rs = st.executeQuery("SELECT * FROM HIDE WHERE UUID = '" + uuid + "';");
			if(!rs.next()) {
				st.execute("INSERT INTO HIDE (UUID, JSON, unlockedJoinMessage, usedJoinMessage) VALUES ('" + uuid + "','" + baseline + "', 100000000, 0);");
				JSONObject toAdd = new JSONObject(baseline);
				jsons.put(player, toAdd);
			} else {
				ResultSet rs1 = st.executeQuery("SELECT * FROM HIDE WHERE UUID = '" + uuid + "';");
				rs1.next();
				JSONObject toAdd = new JSONObject(rs1.getString(2));
				jsons.put(player, toAdd);
			}
		}
 	}
 	
 	public String getJoinMessage(Player player) throws SQLException {
 		Statement st = con.createStatement();
 		ResultSet rs = st.executeQuery("SELECT * FROM HIDE WHERE UUID = '" + getUUID(player) +"';");
 		int selected = rs.getInt("usedJoinMessage");
 		if(getUUID(player).equals("3c970d33c96148f99e4b48ac7bea1ac6")) return " §a§lis NOT going to hack!";
 		if(getUUID(player).equals("df61af011cb5441981ee63c902c1b956")) return " §c§has been working a lot to re-code the game";
 		if(getUUID(player).equals("80c2713d67f94bc683b0ed967302f711")) return " §ais §6going §cto §fbe a §erubik's §9cube";
 		switch(selected) {
 		case 0:
 			return " §7wants to hide!";
 		case 1:
 			return " §ewants to blend in!";
 		case 2:
 			return " §3is ready to disappear.";
 		case 3:
 			return " §6is ready to hunt!";
 		case 4:
 			return " §dis a hiding master.";
 		case 5:
 			return " §8is going to be a Ninja.";
 		case 6:
 			return " §cwants to be sneaky!";
 		case 7:
 			return " §fis going to spot the difference!";
 		case 8:
 			return " §bis going to do a magic trick.";
 		default:
 			return " §c§lis going to have an error in their code!";
 		}
 	}
 	public int getPoints(Player player) throws JSONException {
 		JSONObject json = jsons.get(player);
 		return json.getInt("total_points");
 	}
 	public String getRankHeader(Player player) throws JSONException {
 		int points = this.getPoints(player);
 		String header = "§cError";
 		if(points < 100) {
			header = "§7Blind";
		}
		if(points >= 100) {
			header = "§3Short Sighted";
		}
		if(points >= 1000) {
			header = "§bSneaker";
		}
		if(points >= 2500) {
			header = "§dSneaky";
		}
		if(points >= 5000) {
			header = "§6Deceptive";
		}
		if(points >= 10000) {
			header = "§eMysterious";
		}
		if(points >= 15000) {
			header = "§aDisguised";
		}
		if(points >= 20000) {
			header = "§2Camouflaged";
		}
		if(points >= 30000) {
			header = "§cChameleon";
		}
		if(points >= 40000) {
			header = "§bStealthy";
		}
		if(points >= 50000) {
			header = "§6Masked";
		}
		if(points >= 75000) {
			header = "§eHunter";
		}
		if(points >= 100000) {
			header = "§dMagician";
		}
		if(points >= 150000) {
			header = "§3Escapist";
		}
		if(points >= 300000) {
			header = "§9Invisible";
		}
		if(points >= 500000) {
			header = "§5Shadow";
		}
		if(points >= 1000000) {
			header = "§b§lHoudini";
		}
		if(points >= 17500000) {
			header = "§8§lNinja";
		}
		if(points >= 2500000) {
			header = "§c§lWally";
		}
		if(points >= 4000000) {
			header = "§f§lGhost";
		}
		if(points >= 6000000) {
			header = "§3§lSilouhette";
		}
		if(points >= 8000000) {
			header = "§5§lPhantom";
		}
		if(points >= 10000000) {
			header = "§1§lVanished";
		}
		return header + " ";
 	}
 	public int getKillsAsSeeker(Player player) throws JSONException {
 		JSONObject json = jsons.get(player);
 		return json.getInt("seekerkills");
 	}
 	public int getKillsAsHider(Player player) throws JSONException {
 		JSONObject json = jsons.get(player);
 		return json.getInt("hiderkills");
 	}
 	public int getVictories(Player player) throws JSONException {
 		JSONObject json = jsons.get(player);
 		return json.getInt("victories");
 	}
 	public int getDeaths(Player player) throws JSONException {
 		JSONObject json = jsons.get(player);
 		return json.getInt("deaths");
 	}
 	public int getGamesPlayed(Player player) throws JSONException {
 		JSONObject json = jsons.get(player);
 		return json.getInt("gamesplayed");
 	}
 	@SuppressWarnings("deprecation")
	public ArrayList<Block> getBlocks(Player player) throws JSONException {
 		JSONObject json = jsons.get(player);
 		String in = json.getString("blocks");
 		String blocks[] = in.split(",");
 		ArrayList<Block> out = new ArrayList<Block>();
 		for(int i = 0; i < blocks.length; i++) {
			if(!blocks[i].equals("") ) {
				out.add(Block.getByJSONName(Material.getMaterial(Integer.parseInt(blocks[i])).toString()));
			}
		}
 		return out;
 	}
 	public int getRawBlockExperience(Player player, Block block) throws JSONException {
 		JSONObject json = jsons.get(player).getJSONObject("rawBlockExperience");
 		return json.getInt(block.getJsonName(block));
 	}
 	private int getBlockLvl(Player player, Block block) throws JSONException {
 		if(jsons.get(player).getJSONObject("blockExperience").has(block.getJsonName(block))) {
 			JSONObject json = jsons.get(player).getJSONObject("blockExperience");
 	 		return json.getInt(block.getJsonName(block));
 		} else return 0;
 		
 	}
 	public String getBlockLevel(Player player, Block block) throws JSONException {
		int l = getBlockLvl(player, block);
		if(l == 0) {
			return "§8Unplayed block";
		}
		if(l < 5) {
			return "§7Lvl " + l + " §r";
		}
		else if(l >= 5 && l < 10) {
			return "§bLvl " + l + " §r";
		}
		else if(l >= 10 && l < 15) {
			return "§aLvl " + l + " §r";
		}
		else if(l >=15 && l < 20) {
			return "§eLvl " + l + " §r";
		}
		else if(l >= 20 && l < 25) {
			return "§6Lvl " + l + " §r";
		}
		else if(l >= 25 && l < 30) {
			return "§cLvl " + l + " §r";
		}
		else if(l >= 30 && l < 35) {
			return "§dLvl " + l + " §r";
		}
		else if(l >=35 && l < 40) {
			return "§3Lvl " + l + " §r";
		}
		else if(l >= 40 && l < 45) {
			return "§2Lvl " + l + " §r";
		}
		else if(l >= 45 && l < 50) {
			return "§5Lvl " + l + " §r";
		}
		else if( l >= 50) {
			return "§9Lvl " + l + " §r";
		}
		else {
			return "§4§lNaN §r";
		}
	}
 	public ArrayList<HideAchievement> getAchievements(Player player) throws JSONException {
 		ArrayList<HideAchievement> out = new ArrayList<HideAchievement>();
 		JSONObject achievements = jsons.get(player).getJSONObject("achievements");
 		Iterator<String> keys = achievements.keys();
 		while(keys.hasNext()) {
 			out.add(this.getAchievement(keys.next()));
 		}
 		return out;
 	}
 	public int getProgress(Player player, HideAchievement achievement) throws JSONException {
 		if(this.getAchievements(player).contains(achievement)) {
 			JSONObject ach = jsons.get(player).getJSONObject("achievements").getJSONObject(achievement.getJsonCode(achievement));
 	 		return ach.getInt("progress");
 		} else return 0; 		
 	}
 	public long getUnlockDate(Player player, HideAchievement achievement) throws JSONException {
 		if(this.getAchievements(player).contains(achievement)) {
 			JSONObject ach = jsons.get(player).getJSONObject("achievements").getJSONObject(achievement.getJsonCode(achievement));
 	 		return ach.getLong("unlockedAt");
 		} else return 0; 		
 	}
 	
 	public void addPoints(Player player, int amount) throws JSONException {
 		JSONObject json = jsons.get(player);
 		int points = this.getPoints(player);
 		json.put("total_points", points+amount);
 		jsons.replace(player, json);
 	}
 	public void setAchievementProgress(Player player, HideAchievement achievement, int progress) throws JSONException {
 		if(this.getAchievements(player).contains(achievement)) {
 			JSONObject ach = jsons.get(player).getJSONObject("achievements").getJSONObject(achievement.getJsonCode(achievement));
 			ach.put("progress", progress);
 			JSONObject aL = jsons.get(player).getJSONObject("achievements");
 			aL.put(achievement.getJsonCode(achievement), ach);
 			JSONObject json = jsons.get(player);
 			json.put("achievements", aL);
 		}
 	}
 	public void unlockAchievement(Player player, HideAchievement achievement) throws JSONException {
 		if(this.getAchievements(player).contains(achievement)) {
 			JSONObject ach = jsons.get(player).getJSONObject("achievements").getJSONObject(achievement.getJsonCode(achievement));
 			ach.put("unlockedAt", new Date().getTime());
 			JSONObject aL = jsons.get(player).getJSONObject("achievements");
 			aL.put(achievement.getJsonCode(achievement), ach);
 			JSONObject json = jsons.get(player);
 			json.put("achievements", aL);
 		}
 	}
 	public void addAchievement(Player player, HideAchievement achievement) throws JSONException {
 			JSONObject ach = new JSONObject();
 			ach.put("progress", 0);
 			ach.put("unlockedAt", 0);
 			JSONObject aL = jsons.get(player).getJSONObject("achievements");
 			aL.put(achievement.getJsonCode(achievement), ach);
 			JSONObject json = jsons.get(player);
 			json.put("achievements", aL);
 			ArrayList<HideAchievement> achs = new ArrayList<HideAchievement>();
 			this.sendAchievementUnlock(player, achievement);
 			for(HideAchievement a : HideAchievement.values()) {
 				if(!(a == HideAchievement.MASTER)) {
 					achs.add(a);
 				}
 			}
 			if(this.arrayListIsEqual(this.getAchievements(player),achs)) {
 				unlockAllAchievements(player);
 			}
 	}
 	private void unlockAllAchievements(Player player) throws JSONException {
 		HideAchievement achievement = HideAchievement.MASTER;
 		JSONObject ach = new JSONObject();
 		ach.put("progress", 0);
 		ach.put("unlockedAt", 0);
 		JSONObject aL = jsons.get(player).getJSONObject("achievements");
 		aL.put(achievement.getJsonCode(achievement), ach);
 		JSONObject json = jsons.get(player);
 		json.put("achievements", aL);
 		this.sendAchievementUnlock(player, achievement);
 	}
 	public void addKills(Player player, int killashider, int killasseeker) throws JSONException {
 		JSONObject json = jsons.get(player);
 		int killshider = this.getKillsAsHider(player) + killashider;
 		int killsseeker = this.getKillsAsHider(player) + killasseeker;
 		json.put("hiderkills", killshider);
 		json.put("seekerkills", killsseeker);
 		jsons.replace(player, json);
 	}
 	public void addDeaths(Player player, int deaths) {
 		
 	}
 	
 	
 	public void save() throws SQLException {
 		Statement st = con.createStatement();
 		for(Player player: jsons.keySet()) {
 			st.executeQuery("UPDATE HIDE SET JSON = '" + jsons.get(player).toString() +"' WHERE UUID = '" + getUUID(player)+"';");
 		}
 	}
 	
 	private String getUUID(Player player) {
 		return player.getUniqueId().toString().replace("-", "");
 	}
 	private String getJSON(String url, int timeout) {
	    HttpURLConnection c = null;
	    try {
	        URL u = new URL(url);
	        c = (HttpURLConnection) u.openConnection();
	        c.setRequestMethod("GET");
	        c.setRequestProperty("Content-length", "0");
	        c.setRequestProperty("content-type", "text/plain; charset=utf-8");
	        c.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36");
	        c.setUseCaches(false);
	        c.setAllowUserInteraction(false);
	        c.setConnectTimeout(timeout);
	        c.setReadTimeout(timeout);
	        c.connect();
	        int status = c.getResponseCode();

	        switch (status) {
	            case 200:
	            case 201:
	                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                return sb.toString();
	        }

	    } catch (MalformedURLException ex) {
	        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
	        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	    } finally {
	       if (c != null) {
	          try {
	              c.disconnect();
	          } catch (Exception ex) {
	             Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	          }
	       }
	    }
	    return null;
	}
 	private HideAchievement getAchievement(String in) {
 		HashMap<String, HideAchievement> list = new HashMap<String, HideAchievement>();
 		for(HideAchievement ach : HideAchievement.values()) {
 			list.put(ach.getJsonCode(ach), ach);
 		}
 		return list.get(in);
 	}
 	/**
 	 * 
 	 * @param arrayList The ArrayList you want to compare from
 	 * @param achs The ArrayList you want to compare to. Should be the biggest ArrayList
 	 * @return if a contains all values of b
 	 */
 	private boolean arrayListIsEqual(ArrayList<HideAchievement> arrayList, ArrayList<HideAchievement> achs) {
 		ArrayList<Integer> i = new ArrayList<Integer>();
 		if(arrayList.size() < achs.size()) {
 			for(Object o : achs) {
 				if(arrayList.contains(o)) i.add(1);
 				else i.add(0);
 			}
 		} else {
 			for(Object o : arrayList) 
 				if(achs.contains(o)) i.add(1);
 				else i.add(0);
 		}
 		if(i.contains(0)) return false;
 		else return true;
 	}
 	private void sendAchievementUnlock(Player player, HideAchievement achievement) {
 		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
 		sendCenteredMessage(player,"§a──────────§b§kFF§r§l Achievement Get! §b§kFF§a──────────");
 		sendCenteredMessage(player,"");
 		sendCenteredMessage(player,"§e§l" + achievement.getName(achievement));
 		sendCenteredMessage(player,"§7" + achievement.getDescription(achievement));
 		sendCenteredMessage(player,"");
 		sendCenteredMessage(player,"§a───────────────────");
 	}
 	private final static int CENTER_PX = 154;
 	 
 	public void sendCenteredMessage(Player player, String message){
 	        if(message == null || message.equals("")) player.sendMessage("");
 	                message = ChatColor.translateAlternateColorCodes('&', message);
 	 
 	                int messagePxSize = 0;
 	                boolean previousCode = false;
 	                boolean isBold = false;
 	 
 	                for(char c : message.toCharArray()){
 	                        if(c == '§'){
 	                                previousCode = true;
 	                                continue;
 	                        }else if(previousCode == true){
 	                                previousCode = false;
 	                                if(c == 'l' || c == 'L'){
 	                                        isBold = true;
 	                                        continue;
 	                                }else isBold = false;
 	                        }else{
 	                                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
 	                                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
 	                                messagePxSize++;
 	                        }
 	                }
 	 
 	                int halvedMessageSize = messagePxSize / 2;
 	                int toCompensate = CENTER_PX - halvedMessageSize;
 	                int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
 	                int compensated = 0;
 	                StringBuilder sb = new StringBuilder();
 	                while(compensated < toCompensate){
 	                        sb.append(" ");
 	                        compensated += spaceLength;
 	                }
 	                player.sendMessage(sb.toString() + message);
 	        }
	
}