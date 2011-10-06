package org.dementhium.mysql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dementhium.content.misc.PunishHandler;
import org.dementhium.model.World;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.mysql.ForumIntegration;
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

	public PlayerLoader() {
		System.out.println("When u see dis u shit brix.");
	}


	public PlayerLoadResult load(GameSession connection, PlayerDefinition def) {
		int code = 2;
		Player player = null;
		if (!accountExists(def.getName(), def.getPassword())) {
			createAccount(def.getName(), def.getPassword());
		}
		if (!passwordMatch(def.getName(), def.getPassword())) {
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
	
	private void createAccount(String name, String password) {
		ForumIntegration.createAccount(name, password);
	}

	private void acquireRights(PlayerDefinition def) {
		ForumIntegration.acquireRights(def);
	}

	public boolean load(Player player) {
		return ForumIntegration.load(player);
	}

	public boolean save(Player player) {
		return ForumIntegration.save(player);
	}

	public boolean passwordMatch(String name, String password) {
		return ForumIntegration.verify(name, password);
	}
	
	public boolean accountExists(String name, String password) {
		return ForumIntegration.accountExists(name, password);
	}

	public void close(ResultSet resultSet) {
		try {
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
