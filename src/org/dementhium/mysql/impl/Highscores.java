package org.dementhium.mysql.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.dementhium.model.player.Player;
import org.dementhium.mysql.ForumIntegration;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Highscores extends Thread {

	private BlockingQueue<Player> playerQueue = new LinkedBlockingQueue<Player>();

	public Highscores() {
		try {
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			Player player;
			try {
				player = playerQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			if (player != null) {
				ForumIntegration.applyHighScores(player);
			}
		}
	}


	public void saveHighscore(Player player) {
		playerQueue.offer(player);
	}

}
