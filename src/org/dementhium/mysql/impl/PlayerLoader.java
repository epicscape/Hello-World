package org.dementhium.mysql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dementhium.RS2ServerBootstrap;
import org.dementhium.content.misc.PunishHandler;
import org.dementhium.model.World;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.mysql.DatabaseManager;
import org.dementhium.mysql.MD5Encryption;
import org.dementhium.net.GameSession;
import org.dementhium.util.Constants;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PlayerLoader {

	public class PlayerLoadResult {

		private final Player player;
		private final int returnCode;

		private PlayerLoadResult(Player player, int returnCode) {
			this.returnCode = returnCode;
			this.player = player;
		}

		public Player getPlayer() {
			return player;
		}

		public int getReturnCode() {
			return returnCode;
		}

	}

	public static final String PLAYER_TABLE = "users";

	private DatabaseManager database;

	public PlayerLoader() {
		System.out.println("poop");
		try {
			database = DatabaseManager.create("127.0.0.1", "userdb", "root", "");
			database.establishConnection();
		} catch (SQLException e) {
			//if (Misc.isWindows()) { //So it's only for Emperor.
			//	RS2ServerBootstrap.sqlDisabled = true;
			//}
			System.out.println("Cannot connect to PlayerLoader database!!!");
			e.printStackTrace();
		}
	}


	public PlayerLoadResult load(GameSession connection, PlayerDefinition def) {
		System.out.println("pee");
		int code = 2;
		Player player = null;
		if (!accountExists(def.getName(), def.getPassword())) {
			createAccount(def.getName(), def.getPassword());
		}
		if (!passwordMatch(def.getName(), def.getPassword())) {
			System.out.println("gangbang");
			code = Constants.INVALID_PASSWORD;
		}
		if(code == 2) {
			Player lobbyPlayer = World.getWorld().getPlayerOutOfLobby(def.getName());
			if (World.getWorld().isOnList(def.getName()) && lobbyPlayer == null) {
				code = Constants.ALREADY_ONLINE;
			}
			int count = 2;
			String ip = PunishHandler.formatIp(connection.getChannel().getRemoteAddress().toString());
			for (Player pl : World.getWorld().getPlayers()) {
				if (pl != null && pl.getLastConnectIp().equals(ip)) {
					if (--count == 0) {
						code = 9;
					}
				}
			}
		}
		if (code == 2) {
			player = new Player(connection, def);
			if (player.getRights() != 2 && World.getWorld().getPunishHandler().isBanned(player)) {
				code = Constants.BANNED;
			}
			acquireRights(def);
		}
		load(player);
		return new PlayerLoadResult(player, code);
	}
	
	Object lock;

	private void createAccount(String name, String password) {
			System.out.println("Creating account...");
			try {
				database.executeUpdate("INSERT INTO `userdb`.`users` (`id`, `username`, `password`, `forum_id`, `starter`, `locationX`, `locationY`, " +
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


	private void acquireRights(PlayerDefinition def) {
		if (RS2ServerBootstrap.sqlDisabled) {
			def.setRights(2);
			return;
		}
		ResultSet sql = null;
		try {
			sql = database.executeQuery("SELECT usergroup FROM " + PLAYER_TABLE + " WHERE username='" + def.getName() + "' LIMIT 1");
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
				close(sql);
			}
			sql = null;
		}
	}

	public boolean load(Player player) {
		System.out.println("lol");
		ResultSet resultSet = null;
		try {
			System.out.println("hu "+player.getUsername());
			resultSet = database.executeQuery("SELECT * FROM " + PLAYER_TABLE + " WHERE username='" + player.getUsername() + "' LIMIT 1");
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
				close(resultSet);
			}
			resultSet = null;
		}
		return false;
	}

	public boolean save(Player player) {
		if (RS2ServerBootstrap.sqlDisabled) {
			return true;
		}
		if (player.getAttribute("dontSave") == Boolean.TRUE) {
			return true;
		}
		StringBuilder sb = player.saveSQL(PLAYER_TABLE);
		String completeQuery = sb.toString();
		try {
			database.executeUpdate(completeQuery);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean passwordMatch(String name, String password) {
		if (RS2ServerBootstrap.sqlDisabled) {
			return true;
		}
		try {
			ResultSet result = database.executeQuery("SELECT password FROM " + PLAYER_TABLE+ " WHERE username='" + name +"'");
			if (result.next()) {
				String passwordResult = result.getString("password");
				String encryptedPassword = password;
				if (encryptedPassword.equals(passwordResult)) {
					return true;
				}
			} else {
				System.out.println("u no exist");
				return false;
			}
		} catch (SQLException e) {
			//ignore
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean accountExists(String name, String password) {
		try {
			ResultSet result = database.executeQuery("SELECT password FROM " + PLAYER_TABLE+ " WHERE username='" + name +"'");
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

	public void close(ResultSet resultSet) {
		try {
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
