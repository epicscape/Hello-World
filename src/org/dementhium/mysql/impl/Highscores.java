package org.dementhium.mysql.impl;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.mysql.DatabaseManager;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Highscores extends Thread {

	private DatabaseManager database;

	private BlockingQueue<Player> playerQueue = new LinkedBlockingQueue<Player>();

	public Highscores() {
		try {
			database = DatabaseManager.create("127.0.0.1", "userdb", "root", "");
			database.establishConnection();
			start();
		} catch (SQLException e) {
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
				Skills skills = player.getSkills();
				int[] overall = getOverall(player);
				try {
					if(player.getRights() != 2) {
						database.executeUpdate("DELETE FROM `skills` WHERE playerName = '" + player.getUsername() + "';");
						database.executeUpdate("DELETE FROM `skillsoverall` WHERE playerName = '" + player.getUsername() + "';");
						database.executeUpdate("INSERT INTO `skills` (`playerName`,`Attacklvl`,`Attackxp`,`Defencelvl`,`Defencexp`,`Strengthlvl`,`Strengthxp`,`Constitutionlvl`,`Constitutionxp`,`Rangelvl`,`Rangexp`,`Prayerlvl`,`Prayerxp`,`Magiclvl`,`Magicxp`,`Cookinglvl`,`Cookingxp`,`Woodcuttinglvl`,`Woodcuttingxp`,`Fletchinglvl`,`Fletchingxp`,`Fishinglvl`,`Fishingxp`,`Firemakinglvl`,`Firemakingxp`,`Craftinglvl`,`Craftingxp`,`Smithinglvl`,`Smithingxp`,`Mininglvl`,`Miningxp`,`Herblorelvl`,`Herblorexp`,`Agilitylvl`,`Agilityxp`,`Thievinglvl`,`Thievingxp`,`Slayerlvl`,`Slayerxp`,`Farminglvl`,`Farmingxp`,`Runecraftlvl`,`Runecraftxp`, `Hunterlvl`, `Hunterxp`, `Constructionlvl`, `Constructionxp`, `Summoninglvl`, `Summoningxp`) VALUES ('"+player.getUsername()+"',"+skills.getLevel(0)+","+skills.getXp(0)+","+skills.getLevel(1)+","+skills.getXp(1)+","+skills.getLevel(2)+","+skills.getXp(2)+","+skills.getLevel(3)+","+skills.getXp(3)+","+skills.getLevel(4)+","+skills.getXp(4)+","+skills.getLevel(5)+","+skills.getXp(5)+","+skills.getLevel(6)+","+skills.getXp(6)+","+skills.getLevel(7)+","+skills.getXp(7)+","+skills.getLevel(8)+","+skills.getXp(8)+","+skills.getLevel(9)+","+skills.getXp(9)+","+skills.getLevel(10)+","+skills.getXp(10)+","+skills.getLevel(11)+","+skills.getXp(11)+","+skills.getLevel(12)+","+skills.getXp(12)+","+skills.getLevel(13)+","+skills.getXp(13)+","+skills.getLevel(14)+","+skills.getXp(14)+","+skills.getLevel(15)+","+skills.getXp(15)+","+skills.getLevel(16)+","+skills.getXp(16)+","+skills.getLevel(17)+","+skills.getXp(17)+","+skills.getLevel(18)+","+skills.getXp(18)+","+skills.getLevel(19)+","+skills.getXp(19)+","+skills.getLevel(20)+","+skills.getXp(20)+"," + skills.getLevel(21)+"," + skills.getXp(21) + "," +skills.getLevel(22) + "," + skills.getXp(22) + "," + skills.getLevel(23) + "," + skills.getXp(23)+");");
						database.executeUpdate("INSERT INTO `skillsoverall` (`playerName`,`lvl`,`xp`) VALUES ('"+player.getUsername()+"'," + overall[0] +"," + overall[1] +");");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}


	public void saveHighscore(Player player) {
		playerQueue.offer(player);
	}


	public int[] getOverall(Player player) {
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
