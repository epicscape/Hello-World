package org.dementhium.mysql.impl;

import java.sql.SQLException;

import org.dementhium.model.World;
import org.dementhium.mysql.DatabaseManager;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PlayerCount implements Runnable {
	
	private DatabaseManager database;

	public PlayerCount() {
		try {
			database = DatabaseManager.create("127.0.0.1", "userdb", "root", "bartpelle");
			database.establishConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			database.executeUpdate("UPDATE playercount SET count='" + World.getWorld().getPlayers().size() + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
}