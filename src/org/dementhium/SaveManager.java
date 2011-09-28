package org.dementhium;

import java.io.IOException;

import org.dementhium.io.XMLHandler;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.util.Misc;

public class SaveManager extends Thread {

	private static final SaveManager INSTANCE = new SaveManager();

	public static SaveManager getSaveManager() {
		return INSTANCE;
	}

	private SaveManager() {
		super("SaveManager");
		start();
	}

	@Override
	public void run() {
		while (true) {
			World.getWorld().getBackgroundLoader().submit(new Runnable() {
				public void run() {
					for (final Player player : World.getWorld().getPlayers()) {
						World.getWorld().getPlayerLoader().save(player);
					}
				}
			});
			try {
				if (Misc.isVPS()) {
					XMLHandler.toXML("data/xml/clans.xml", World.getWorld().getClanManager().getClans());
					World.getWorld().getPunishHandler().save();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
