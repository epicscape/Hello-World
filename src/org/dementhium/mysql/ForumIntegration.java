package org.dementhium.mysql;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import org.dementhium.RS2ServerBootstrap;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;

/**
 * @author 'Mystic Flow
 */
public class ForumIntegration {

	private static Connection connection;

	private static long lastConnection = System.currentTimeMillis();

	/*static {
         createConnection();
     }*/

	public static void init() {
		createConnection();
	} // this will auto call static constructor

	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://www.hbproductions.nl/user7065_epicscape", "user7065", "-SNIP!-");
			System.out.println("DONE");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void destroyConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean verify(String username, String password) {
		System.out.println("K lets verify "+username+", "+password);
		try {
			Statement stat = connection.createStatement();
			ResultSet rs = stat.executeQuery("SELECT username, password FROM users WHERE username LIKE '" + username + "'");
			while (rs.next()) {
				System.out.println("It has next, so... ");
				String pass = rs.getString("password"); // our forum password
				System.out.println("I got pass "+pass);
				if (password.equals(pass)) {
					return true;
				}
			}
			System.out.println("were done");
			rs.close();
			return false;
		} catch (Throwable e) {
			e.printStackTrace();
			if (System.currentTimeMillis() - lastConnection > 10000) {
				destroyConnection();
				createConnection();
				lastConnection = System.currentTimeMillis();
			}
		}
		return false;
	}

	public static boolean accountExists(String name, String password) {
		try {
			Statement stat = connection.createStatement();
			ResultSet result = stat.executeQuery("SELECT password FROM users WHERE username LIKE '" + name +"'");
			if (result.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean load(Player player) {
		System.out.println("lol");
		ResultSet resultSet = null;
		try {
			Statement stat = connection.createStatement();
			resultSet = stat.executeQuery("SELECT * FROM users WHERE username LIKE '" + player.getUsername() + "' LIMIT 1");
			if (resultSet.next()) {
				player.loadSQL(resultSet);
				return true;
			} else {
				System.out.println("Empty resultset");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			resultSet = null;
		}
		return false;
	}

	public static void createAccount(String name, String password) {
		System.out.println("Creating account...");
		try {
			Statement stat = connection.createStatement();
			stat.executeUpdate("INSERT INTO `userdb`.`users` (`id`, `username`, `password`, `forum_id`, `starter`, `locationX`, `locationY`, " +
					"`height`, `hitpoints`, `spellbook`, `ancientcurses`, `banktab`, `privatetextcolor`, `lastXAmount`, `lastPoison`, `lastSelection`, " +
					"`prayerPoints`, `autoretaliate`, `experiencecounter`, `banktabs`, `strongholdFlags`, `specialAmount`, `slayerTask`, `date`, `notes`, " +
					"`isonline`, `skillLvl`, `skillXP`, `items`, `itemsN`, `bankitems`, `bankitemsN`, `equipment`, `equipmentN`, `friends`, `ignores`, " +
					"`usergroup`, `mainacc`, `previousname`, `namedate`, `maindate`, `familiarId`, `familiarTicks`, `doublexptime`, `familiarSpecialPoints`, " +
					"`looks`, `colours`, `gender`, `summoningOrbSetting`, `burdenBeastItems`, `burdenBeastItemsN`) VALUES (NULL, '"+name+"', '"+password+"', '0', " +
					"'0', '-1', '-1', '-1', '100', '192', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1000', '0', '', '', '0', " +
					"'1,1,1,10,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1', '0,0,0,1154,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0', '-1', '-1', " +
					"'-1', '-1', '-1', '-1', '0', '0', '2', '0', '', '', '', '0', '0', '0', '0', '3,14,18,26,34,38,42', '3,16,16,11,14', '0', '0', '-1', '-1');");
			System.out.println("Account created.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Account not created.");
		} finally {
		}
	}
	
	public static boolean save(Player player) {
		if (RS2ServerBootstrap.sqlDisabled) {
			return true;
		}
		if (player.getAttribute("dontSave") == Boolean.TRUE) {
			return true;
		}
		StringBuilder sb = player.saveSQL("users");
		String completeQuery = sb.toString();
		try {
			Statement stat = connection.createStatement();
			stat.executeUpdate(completeQuery);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void acquireRights(PlayerDefinition def) {
		if (RS2ServerBootstrap.sqlDisabled) {
			def.setRights(2);
			return;
		}
		ResultSet sql = null;
		try {
			Statement stat = connection.createStatement();
			sql = stat.executeQuery("SELECT usergroup FROM users WHERE username LIKE '" + def.getName() + "' LIMIT 1");
			if (sql.next()) {
				int usergroup = sql.getInt("usergroup");
				switch (usergroup) {
				case 6:
					def.setRights(2);
					break;
				case 5:
				case 19:
					def.setRights(1);
					break;
				case 16://Respected Donor
					def.setDonor(1);
					break;
				case 17://Grand Donor
					def.setDonor(2);
					break;
				case 18://Elite Donor
					def.setDonor(3);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (sql != null) {
				try {
					sql.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			sql = null;
		}
	}

	private static String convertToHex(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (byte aData : data) {
			int halfbyte = (aData >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = aData & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String generate(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}

	public static void applyHighScores(Player player) {
		Skills skills = player.getSkills();
		int[] overall = getOverall(player);
		try {
			Statement database = connection.createStatement();
			if(player.getRights() != 2) {
				database.executeUpdate("DELETE FROM `skills` WHERE playerName = '" + player.getUsername() + "';");
				database.executeUpdate("DELETE FROM `skillsoverall` WHERE playerName = '" + player.getUsername() + "';");
				database.executeUpdate("INSERT INTO `skills` (`playerName`,`Attacklvl`,`Attackxp`,`Defencelvl`,`Defencexp`,`Strengthlvl`,`Strengthxp`,`Constitutionlvl`,`Constitutionxp`,`Rangelvl`,`Rangexp`,`Prayerlvl`,`Prayerxp`,`Magiclvl`,`Magicxp`,`Cookinglvl`,`Cookingxp`,`Woodcuttinglvl`,`Woodcuttingxp`,`Fletchinglvl`,`Fletchingxp`,`Fishinglvl`,`Fishingxp`,`Firemakinglvl`,`Firemakingxp`,`Craftinglvl`,`Craftingxp`,`Smithinglvl`,`Smithingxp`,`Mininglvl`,`Miningxp`,`Herblorelvl`,`Herblorexp`,`Agilitylvl`,`Agilityxp`,`Thievinglvl`,`Thievingxp`,`Slayerlvl`,`Slayerxp`,`Farminglvl`,`Farmingxp`,`Runecraftlvl`,`Runecraftxp`, `Hunterlvl`, `Hunterxp`, `Constructionlvl`, `Constructionxp`, `Summoninglvl`, `Summoningxp`) VALUES ('"+player.getUsername()+"',"+skills.getLevel(0)+","+skills.getXp(0)+","+skills.getLevel(1)+","+skills.getXp(1)+","+skills.getLevel(2)+","+skills.getXp(2)+","+skills.getLevel(3)+","+skills.getXp(3)+","+skills.getLevel(4)+","+skills.getXp(4)+","+skills.getLevel(5)+","+skills.getXp(5)+","+skills.getLevel(6)+","+skills.getXp(6)+","+skills.getLevel(7)+","+skills.getXp(7)+","+skills.getLevel(8)+","+skills.getXp(8)+","+skills.getLevel(9)+","+skills.getXp(9)+","+skills.getLevel(10)+","+skills.getXp(10)+","+skills.getLevel(11)+","+skills.getXp(11)+","+skills.getLevel(12)+","+skills.getXp(12)+","+skills.getLevel(13)+","+skills.getXp(13)+","+skills.getLevel(14)+","+skills.getXp(14)+","+skills.getLevel(15)+","+skills.getXp(15)+","+skills.getLevel(16)+","+skills.getXp(16)+","+skills.getLevel(17)+","+skills.getXp(17)+","+skills.getLevel(18)+","+skills.getXp(18)+","+skills.getLevel(19)+","+skills.getXp(19)+","+skills.getLevel(20)+","+skills.getXp(20)+"," + skills.getLevel(21)+"," + skills.getXp(21) + "," +skills.getLevel(22) + "," + skills.getXp(22) + "," + skills.getLevel(23) + "," + skills.getXp(23)+");");
				database.executeUpdate("INSERT INTO `skillsoverall` (`playerName`,`lvl`,`xp`) VALUES ('"+player.getUsername()+"'," + overall[0] +"," + overall[1] +");");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int[] getOverall(Player player) {
		int totalLevel = 0;
		int totalExperience = 0;
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			totalLevel += player.getSkills().getLevelForExperience(i);
		}
		for (int i = 0; i < Skills.SKILL_COUNT; i++) {
			totalExperience += player.getSkills().getXp(i);
		}
		return new int[] {totalLevel, totalExperience};
	}

}
