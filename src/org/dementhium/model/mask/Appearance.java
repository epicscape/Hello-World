package org.dementhium.model.mask;

import org.dementhium.model.player.Player;

public class Appearance {

	private int npcType;
	private int gender;
	private int[] look = new int[7];
	private int[] colour = new int[5];

	private Player player;

	public Appearance() {
		this.setNpcType(-1);
		this.resetAppearence();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void resetAppearence() {
		/*
		 * look[0] = 3; // Hair look[1] = 14; // Beard look[2] = 18; // Torso
		 * look[3] = 26; // Arms look[4] = 34; // Bracelets look[5] = 38; //
		 * Legs look[6] = 42; // Shoes for (int i = 0; i < 5; i++) { colour[i] =
		 * i * 3 + 2; } colour[2] = 16; colour[1] = 16; colour[0] = 3; gender =
		 * 0;
		 */
		/*
		 * try { database = DatabaseManager.create("66.197.227.168", "userdb",
		 * "root", "demflow31185"); database.establishConnection(); } catch
		 * (SQLException e) { if (Misc.isWindows()) { //So it's only for
		 * Emperor. RS2ServerBootstrap.sqlDisabled = true; }
		 * e.printStackTrace(); } ResultSet result = null; result =
		 * database.executeQuery("SELECT * FROM " + PlayerLoader.PLAYER_TABLE +
		 * " WHERE username='" + getPlayer().getUsername() + "' LIMIT 1"); final
		 * String[] looks = result.getString("looks").split(","); for (int i =
		 * 0; i < looks.length; i++) { int look2 = Integer.parseInt(looks[i]);
		 * look[i] = look2; } final String[]colours =
		 * result.getString("colours").split(","); for (int i = 0; i <
		 * colours.length; i++) { int colour2 = Integer.parseInt(colours[i]);
		 * colour[i] = colour2; }
		 */
	}

	public void female() {
		look[0] = 48; // Hair
		look[1] = 1000; // Beard
		look[2] = 57; // Torso
		look[3] = 64; // Arms
		look[4] = 68; // Bracelets
		look[5] = 77; // Legs
		look[6] = 80; // Shoes
		for (int i = 0; i < 5; i++) {
			colour[i] = i * 3 + 2;
		}
		colour[2] = 16;
		colour[1] = 16;
		colour[0] = 3;
		gender = 1;

	}

	public void setNpcType(int npcType) {
		this.npcType = npcType;
	}

	public int getNpcType() {
		return npcType;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public int getGender() {
		return gender;
	}

	public int[] getLook() {
		return look;
	}

	public int[] getColour() {
		return colour;
	}

}
