package org.dementhium.content.skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.map.Region;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

public class Prayer {

	private static final Random RANDOM = new Random();

	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1,
	CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3, MYSTIC_WILL = 4,
	ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7,
	RAPID_RESTORE = 8, RAPID_HEAL = 9, PROTECT_ITEM = 10,
	HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13,
	ULTIMATE_STRENGTH = 14, INCREDIBLE_REFLEXES = 15, PROTECT_FROM_SUMMONING = 16,
	PROTECT_FROM_MAGIC = 17, PROTECT_FROM_MISSILES = 18,
	PROTECT_FROM_MELEE = 19, EAGLE_EYE = 20, MYSTIC_MIGHT = 21,
	RETRIBUTION = 22, REDEMPTION = 23, SMITE = 24, CHIVALRY = 25,
	RAPID_RENEWAL = 26, PIETY = 27, RIGOUR = 28, AUGURY = 29;

	public static final int CURSE_PROTECT_ITEM = 0, SAP_WARRIOR = 1,
	SAP_RANGER = 2, SAP_MAGE = 3, SAP_SPIRIT = 4, BERSERKER = 5,
	DEFLECT_SUMMONING = 6, DEFLECT_MAGIC = 7, DEFLECT_MISSILES = 8,
	DEFLECT_MELEE = 9, LEECH_ATTACK = 10, LEECH_RANGE = 11,
	LEECH_MAGIC = 12, LEECH_DEFENCE = 13, LEECH_STRENGTH = 14,
	LEECH_ENERGY = 15, LEECH_SPECIAL_ATTACK = 16, WRATH = 17,
	SOUL_SPLIT = 18, TURMOIL = 19;

	private final Player player;

	private boolean[][] onPrayers;
	private boolean usingQuickPrayer;
	private boolean[][] quickPrayers = {new boolean[30], new boolean[20]};
	private boolean ancientcurses;
	private boolean quickPrayersOn;

	/**
	 * The dynamic bonuses: changes if you get sapped/leeched
	 * or you're using a leech curse/turmoil.
	 */
	private int attackModifier, strengthModifier, defenceModifier, rangeModifier, magicModifier;

	/**
	 * The bonuses gained from activated prayers/curses.
	 */
	private int staticAttack, staticStrength,
	staticDefence, staticRange, staticMagic;

	private List<Curse> curses = new ArrayList<Curse>();

	public double getAttackModifier() {
		return (staticAttack + attackModifier) / 100D;
	}

	public double getStrengthModifier() {
		return (staticStrength + strengthModifier) / 100D;
	}

	public double getDefenceModifier() {
		return (staticDefence + defenceModifier) / 100D;
	}

	public double getRangeModifier() {
		return (staticRange + rangeModifier) / 100D;
	}

	public double getMagicModifier() {
		return (staticMagic + magicModifier) / 100D;
	}

	public Prayer(Player player) {
		this.player = player;
		boolean[][] onPrayers = {new boolean[30], new boolean[20]};
		this.onPrayers = onPrayers;
	}

	/*public boolean usingCorrispondingPrayer(FightType style) {
		int book = ancientcurses ? 1 : 0;
		switch (book) {
		case 0:
			switch (style) {
			case MELEE:
				return onPrayers[0][PROTECT_FROM_MELEE]
				                    || (quickPrayersOn && quickPrayers[0][PROTECT_FROM_MELEE]);
			case RANGE:
				return onPrayers[0][PROTECT_FROM_MISSILES]
				                    || (quickPrayersOn && quickPrayers[0][PROTECT_FROM_MISSILES]);
			case MAGIC:
				return onPrayers[0][PROTECT_FROM_MAGIC]
				                    || (quickPrayersOn && quickPrayers[0][PROTECT_FROM_MAGIC]);
			}
		case 1:
			switch (style) {
			case MELEE:
				return onPrayers[1][DEFLECT_MELEE]
				                    || (quickPrayersOn && quickPrayers[1][DEFLECT_MELEE]);
			case RANGE:
				return onPrayers[1][DEFLECT_MISSILES]
				                    || (quickPrayersOn && quickPrayers[1][DEFLECT_MISSILES]);
			case MAGIC:
				return onPrayers[1][DEFLECT_MAGIC]
				                    || (quickPrayersOn && quickPrayers[1][DEFLECT_MAGIC]);
			}
		}
		return false;
	}*/

	public boolean usingPrayer(int book, int prayerId) {
		if (prayerId < 0) {
			return false;
		}
		return onPrayers[book][prayerId] || (quickPrayersOn && quickPrayers[book][prayerId]);
	}

	public boolean needsProtectAgainstDamage() {
		return this.onPrayers[0][PROTECT_FROM_MELEE] || this.onPrayers[0][PROTECT_FROM_MISSILES] || this.onPrayers[0][PROTECT_FROM_MAGIC];
	}

	private final static int[][] PRAYER_REQS = {
		// normal prayer book
		{1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 35, 37, 40, 43, 44, 45, 46, 49, 52, 60, 65, 70, 74, 77},
		// ancient prayer book
		{50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89, 92, 95}};

	private final static int[][][] closePrayers = {{ // normal prayer book
		{0, 5, 13}, // Skin prayers 0
		{1, 6, 14}, // Strength prayers 1
		{2, 7, 15}, // Attack prayers 2
		{3, 11, 20, 28}, // Range prayers 3
		{4, 12, 21, 29}, // Magic prayers 4
		{8, 9, 26}, // Restore prayers 5
		{10}, // Protect item prayers 6
		{17, 18, 19}, // Protect prayers 7
		{16}, // Other protect prayers 8
		{22, 23, 24}, // Other special prayers 9
		{25, 27}, // Other prayers 10
	}, { // ancient prayer book
		{0}, // Protect item prayers 0
		{1, 2, 3, 4}, // sap prayers 1
		{5}, // other prayers 2
		{7, 8, 9, 17, 18}, // protect prayers 3
		{6}, // other protect prayers 4
		{10, 11, 12, 13, 14, 15, 16}, // leech prayers 5
		{19}, // other prayers
	}};

	private double drainRate(int id) {// I got this perfected
		switch (getPrayerBook()) {
		case 0:
			switch (id) {
			case Prayer.RAPID_RESTORE:
				return 26;
			case Prayer.RAPID_HEAL:
			case Prayer.PROTECT_ITEM:
				return 18;
			case Prayer.THICK_SKIN:
			case Prayer.BURST_OF_STRENGTH:
			case Prayer.CLARITY_OF_THOUGHT:
			case Prayer.SHARP_EYE:
			case Prayer.MYSTIC_WILL:
			case Prayer.RETRIBUTION:
				return 12;
			case Prayer.ROCK_SKIN:
			case Prayer.SUPERHUMAN_STRENGTH:
			case Prayer.IMPROVED_REFLEXES:
			case Prayer.HAWK_EYE:
			case Prayer.MYSTIC_LORE:
			case Prayer.REDEMPTION:
				return 6;
			case Prayer.STEEL_SKIN:
			case Prayer.ULTIMATE_STRENGTH:
			case Prayer.INCREDIBLE_REFLEXES:
			case Prayer.PROTECT_FROM_MAGIC:
			case Prayer.PROTECT_FROM_MISSILES:
			case Prayer.PROTECT_FROM_MELEE:
			case Prayer.EAGLE_EYE:
			case Prayer.MYSTIC_MIGHT:
				return 3;
			case Prayer.SMITE:
				return 2;
			case Prayer.CHIVALRY:
			case Prayer.PIETY:
				return 1.5;
			case Prayer.RIGOUR:
			case Prayer.AUGURY:
				return 2;
			case Prayer.RAPID_RENEWAL:
				return 1.8;
			}
		case 1:
			switch (id) {
			case Prayer.CURSE_PROTECT_ITEM:
				return 18;
			case SAP_WARRIOR:
			case SAP_RANGER:
			case SAP_MAGE:
			case SAP_SPIRIT:
				return 5;
			case BERSERKER:
				return 18;
			case DEFLECT_SUMMONING:
			case DEFLECT_MAGIC:
			case DEFLECT_MISSILES:
			case DEFLECT_MELEE:
				return 3;
			case LEECH_ATTACK:
			case LEECH_RANGE:
			case LEECH_MAGIC:
			case LEECH_DEFENCE:
			case LEECH_STRENGTH:
			case LEECH_ENERGY:
			case LEECH_SPECIAL_ATTACK:
				return 3.6;
			case WRATH:
				return 12;
			case SOUL_SPLIT:
			case TURMOIL:
				return 1.5;
			}
		}
		return 0;
	}

	public int getHeadIcon() {
		int value = -1;
		if (this.usingPrayer(0, 16))
			value += 8;
		if (this.usingPrayer(0, 17))
			value += 3;
		else if (this.usingPrayer(0, 18))
			value += 2;
		else if (this.usingPrayer(0, 19))
			value += 1;
		else if (this.usingPrayer(0, 22))
			value += 4;
		else if (this.usingPrayer(0, 23))
			value += 6;
		else if (this.usingPrayer(0, 24))
			value += 5;
		else if (this.usingPrayer(1, 6)) {
			value += 16;
			if (this.usingPrayer(1, 8))
				value += 2;
			else if (this.usingPrayer(1, 7))
				value += 3;
			else if (this.usingPrayer(1, 9))
				value += 1;
		} else if (this.usingPrayer(1, 7))
			value += 14;
		else if (this.usingPrayer(1, 8))
			value += 15;
		else if (this.usingPrayer(1, 9))
			value += 13;
		else if (this.usingPrayer(1, 17))
			value += 20;
		else if (this.usingPrayer(1, 18))
			value += 21;
		return value;
	}

	public void switchSettingQuickPrayer() {
		if (!this.usingQuickPrayer) {
			ActionSender.sendBConfig(player, 181, 1);
			ActionSender.sendBConfig(player, 168, 6);
			ActionSender.sendAMask(player, 0, 29, 271, 42, 0, 2);
			this.usingQuickPrayer = true;
		} else {
			ActionSender.sendBConfig(player, 181, 0);
			ActionSender.sendBConfig(player, 149, 6);
			this.usingQuickPrayer = false;
		}
		this.recalculatePrayer();
	}

	public void switchQuickPrayers() {
		if (!checkPrayer())
			return;
		if (this.quickPrayersOn) {
			this.closeAllPrayers();
			ActionSender.sendBConfig(player, 182, 0);
			this.quickPrayersOn = false;
		} else {
			int index = 0;
			int on = 0;
			for (boolean prayer : this.quickPrayers[this.getPrayerBook()]) {
				if (prayer) {
					if (!onPrayers[getPrayerBook()][index]) 
						usePrayer(index);
					on++;
				}
				index++;
			}
			if (on > 0) {
				this.recalculatePrayer();
				this.quickPrayersOn = true;
				ActionSender.sendBConfig(player, 182, 1);
			} else {
				player.sendMessage("You haven't selected any quick prayers!");
			}
		}
	}

	public void switchPrayer(int prayerId, boolean b) {
		if (!usingQuickPrayer)
			if (!checkPrayer())
				return;
		this.usePrayer(prayerId);
		this.recalculatePrayer();
	}

	public void closeAllPrayers() {
		boolean[][] onPrayers = {new boolean[30], new boolean[20]};
		this.onPrayers = onPrayers;
		for (Curse curse : curses) {
			curse.deactivate();
		}
		curses.clear();
		staticAttack = staticDefence = staticStrength = staticRange = staticMagic = 0;
		ActionSender.sendBConfig(player, 182, 0);
		ActionSender.sendConfig(player, ancientcurses ? 1582 : 1395, 0);
		recalculatePrayer();
		updateTurmoil(null);
		player.getMask().setApperanceUpdate(true);
	}

	private boolean checkPrayer() {
		if (player.getSkills().getPrayerPoints() < 1) {
			player.getSkills().setPrayerPoints(0, true);
			player.sendMessage("You have ran out of prayer points.");
			closeAllPrayers();
			return false;
		}
		return true;
	}

	private boolean usePrayer(int prayerId) {
		if (prayerId < 0 || prayerId >= PRAYER_REQS[this.getPrayerBook()].length) {
			return false;
		}
		if (player.getActivity() instanceof DuelActivity) {
			if (((DuelActivity) player.getActivity()).getDuelConfigurations().getRule(Rules.PRAYER)) {
				player.sendMessage("You aren't allowed to use prayer during this duel!");
				return false;
			}
		}
		if (player.getSkills().getLevelForExperience(5) < PRAYER_REQS[this.getPrayerBook()][prayerId]) {
			player.sendMessage("You need a level of " + PRAYER_REQS[this.getPrayerBook()][prayerId] + " prayer to activate this.");
			return false;
		}
		if (!usingQuickPrayer) {
			if (onPrayers[this.getPrayerBook()][prayerId]) {
				modify(prayerId, false);
				onPrayers[this.getPrayerBook()][prayerId] = false;
				player.getMask().setApperanceUpdate(true);
				return true;
			}
		} else {
			if (quickPrayers[this.getPrayerBook()][prayerId]) {
				quickPrayers[this.getPrayerBook()][prayerId] = false;
				return true;
			}
		}
		if (getPrayerBook() == 0) {
			if (prayerId >= PROTECT_FROM_SUMMONING && prayerId <= PROTECT_FROM_MELEE && player.hasTick("nex_drag")) {
				player.sendMessage("You've been injured and can't use that prayer!");
				return true;
			}
			switch (prayerId) {
			case 0:
			case 5:
			case 13:
				this.closePrayers(closePrayers[this.getPrayerBook()][0],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 1:
			case 6:
			case 14:
				this.closePrayers(closePrayers[this.getPrayerBook()][1],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 2:
			case 7:
			case 15:
				this.closePrayers(closePrayers[this.getPrayerBook()][2],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 3:
			case 11:
			case 20:
			case 28:
				this.closePrayers(closePrayers[this.getPrayerBook()][1],
						closePrayers[this.getPrayerBook()][2],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 4:
			case 12:
			case 21:
			case 29:
				this.closePrayers(closePrayers[this.getPrayerBook()][1],
						closePrayers[this.getPrayerBook()][2],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			case 8:
			case 9:
			case 26:
				this.closePrayers(closePrayers[this.getPrayerBook()][5]);
				break;
			case 10:
				this.closePrayers(closePrayers[this.getPrayerBook()][6]);
				break;
			case 17:
			case 18:
			case 19:
				this.closePrayers(closePrayers[this.getPrayerBook()][7],
						closePrayers[this.getPrayerBook()][9]);
				this.player.getMask().setApperanceUpdate(true);
				break;
			case 16:
				this.closePrayers(closePrayers[this.getPrayerBook()][8],
						closePrayers[this.getPrayerBook()][9]);
				this.player.getMask().setApperanceUpdate(true);
				break;
			case 22:
			case 23:
			case 24:
				this.closePrayers(closePrayers[this.getPrayerBook()][7],
						closePrayers[this.getPrayerBook()][8],
						closePrayers[this.getPrayerBook()][9]);
				this.player.getMask().setApperanceUpdate(true);
				break;
			case 25:
			case 27:
				this.closePrayers(closePrayers[this.getPrayerBook()][0],
						closePrayers[this.getPrayerBook()][1],
						closePrayers[this.getPrayerBook()][2],
						closePrayers[this.getPrayerBook()][3],
						closePrayers[this.getPrayerBook()][4],
						closePrayers[this.getPrayerBook()][10]);
				break;
			default:
				return false;
			}
		} else {
			if (prayerId >= Prayer.DEFLECT_SUMMONING && prayerId <= Prayer.DEFLECT_MELEE && player.hasTick("nex_drag")) {
				player.sendMessage("You've been injured and can't use that curse!");
				return true;
			}
			switch (prayerId) {
			case 0:
				if (!usingQuickPrayer) {
					player.animate(12567);
					player.graphics(2213);
				}
				this.closePrayers(closePrayers[this.getPrayerBook()][0]);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				this.closePrayers(closePrayers[this.getPrayerBook()][5], closePrayers[this.getPrayerBook()][6]);
				break;
			case 5:
				if (!usingQuickPrayer) {
					player.animate(12589);
					player.graphics(2266);
				}
				closePrayers(closePrayers[getPrayerBook()][2]);
				break;
			case 7:
			case 8:
			case 9:
			case 17:
			case 18:
				closePrayers(closePrayers[getPrayerBook()][3]);
				if (!usingQuickPrayer) {
					player.getMask().setApperanceUpdate(true);
				}
				break;
			case 6:
				closePrayers(closePrayers[getPrayerBook()][4]);
				if (!usingQuickPrayer) {
					player.getMask().setApperanceUpdate(true);
				}
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
				closePrayers(closePrayers[this.getPrayerBook()][1], closePrayers[this.getPrayerBook()][6]);
				break;
			case 19: //turmoil
			closePrayers(closePrayers[this.getPrayerBook()][1], closePrayers[this.getPrayerBook()][6]);
			closePrayers(closePrayers[this.getPrayerBook()][5], closePrayers[this.getPrayerBook()][6]);
			if (!usingQuickPrayer) {
				player.animate(12565);
				player.graphics(2226);
				player.getMask().setApperanceUpdate(true);
			}
			break;
			default:
				return false;
			}
		}
		if (!usingQuickPrayer) {
			if (isAncientCurses()) {
				Curse curse = createCurse(prayerId);
				if (curse != null) {
					curses.add(curse);
				}
			}
			modify(prayerId, true);
			onPrayers[getPrayerBook()][prayerId] = true;
		} else {
			quickPrayers[getPrayerBook()][prayerId] = true;
		}
		return true;
	}

	public void closePrayers(int[]... prayers) {
		int book = getPrayerBook();
		for (int[] prayer : prayers) {
			for (int prayerId : prayer) {
				if (usingQuickPrayer) {
					this.quickPrayers[book][prayerId] = false;
				} else {
					if (onPrayers[book][prayerId]) {
						if (book == 1) {
							modify(prayerId, false);
						} else {
							modify(prayerId, false);
						}
					}
					this.onPrayers[book][prayerId] = false;
				}
			}
		}
	}

	public void modify(int prayerId, boolean on) {
		if (!isAncientCurses()) {
			switch (prayerId) {
			case Prayer.BURST_OF_STRENGTH:
				staticStrength += on ? 5 : -5;
				break;
			case Prayer.SUPERHUMAN_STRENGTH:
				staticStrength += on ? 10 : -10;
				break;
			case Prayer.ULTIMATE_STRENGTH:
				staticStrength += on ? 15 : -15;
				break;
			case Prayer.THICK_SKIN:
				staticDefence += on ? 5 : -5;
				break;
			case Prayer.ROCK_SKIN:
				staticDefence += on ? 10 : -10;
				break;
			case Prayer.STEEL_SKIN:
				staticDefence += on ? 15 : -15;
				break;
			case Prayer.CHIVALRY:
				staticAttack += on ? 15 : -15;
				staticStrength += on ? 18 : -18;
				break;
			case Prayer.PIETY:
				staticAttack += on ? 20 : -20;
				staticStrength += on ? 23 : -23;
				break;
			case Prayer.CLARITY_OF_THOUGHT:
				staticAttack += on ? 5 : -5;
				break;
			case Prayer.IMPROVED_REFLEXES:
				staticAttack += on ? 10 : -10;
				break;
			case Prayer.INCREDIBLE_REFLEXES:
				staticAttack += on ? 15 : -15;
				break;
			case Prayer.SHARP_EYE:
				staticRange += on ? 5 : -5;
				break;
			case Prayer.HAWK_EYE:
				staticRange += on ? 10 : -10;
				break;
			case Prayer.EAGLE_EYE:
				staticRange += on ? 15 : -15;
				break;
			case Prayer.RIGOUR:
				staticRange += on ? 20 : -20;
				staticDefence += on ? 25 : -25;
				break;
			case Prayer.MYSTIC_WILL:
				staticMagic += on ? 5 : -5;
				break;
			case Prayer.MYSTIC_LORE:
				staticMagic += on ? 10 : -10;
				break;
			case Prayer.MYSTIC_MIGHT:
				staticMagic += on ? 15 : -15;
				break;
			case Prayer.AUGURY:
				staticMagic += on ? 20 : -20;
				staticDefence += on ? 25 : -25;
				break;
			}
		} else {
			switch (prayerId) {
			case Prayer.TURMOIL:
				if (on) {
					staticAttack += 15;
					staticDefence += 15;
					staticStrength += 23;
				} else {
					staticAttack -= 15;
					staticDefence -= 15;
					staticStrength -= 23;
					updateTurmoil(null);
				}
				break;
			case Prayer.LEECH_ATTACK:
				staticAttack += on ? 5 : -5;
				break;
			case Prayer.LEECH_DEFENCE:
				staticDefence += on ? 5 : -5;
				break;
			case Prayer.LEECH_STRENGTH:
				staticStrength += on ? 5 : -5;
				break;
			case Prayer.LEECH_RANGE:
				staticRange += on ? 5 : -5;
				break;
			case Prayer.LEECH_MAGIC:
				staticMagic += on ? 5 : -5;
				break;
			}
			if (!on) {
				removeCurse(prayerId);
			}
		}
	}

	public void removeCurse(int id) {
		for (int i = 0; i < curses.size(); i++) {
			Curse curse = curses.get(i);
			if (curse.id == id) {
				curse.deactivate();
				curses.remove(i);
			}
		}
	}

	public void closeOnPrayers(int book, int[] prayers) {
		for (int prayerId : prayers) {
			if (onPrayers[book][prayerId]) {
				modify(prayerId, false);
			}
			onPrayers[book][prayerId] = false;
		}
	}

	private final static int[] CONFIG_VALUES = {
		1, 2, 4, 262144, 524288, 8,
		16, 32, 64, 128, 256, 1048576, 2097152, 512, 1024, 2048, 16777216,
		4096, 8192, 16384, 4194304, 8388608, 32768, 65536, 131072,
		33554432, 134217728, 67108864, 536870912, 268435456};

	public void recalculatePrayer() {
		ActionSender.sendConfig(player, ancientcurses ? (usingQuickPrayer ? 1587 : 1582) : (usingQuickPrayer ? 1397 : 1395), 0);
		int value = 0;
		int index = 0;
		for (boolean prayer : (!usingQuickPrayer ? onPrayers[getPrayerBook()] : quickPrayers[getPrayerBook()])) {
			if (prayer) {
				value |= ancientcurses ? 1 << index : CONFIG_VALUES[index];
			}
			index++;
		}
		ActionSender.sendConfig(player, ancientcurses ? (usingQuickPrayer ? 1587 : 1582) : (usingQuickPrayer ? 1397 : 1395), value);
	}

	public int getPrayerBook() {
		return !ancientcurses ? 0 : 1;
	}

	public boolean isAncientCurses() {
		return this.ancientcurses;
	}

	public boolean setAnctientCurses(boolean bool) {
		closeAllPrayers();
		return this.ancientcurses = bool;
	}

	public boolean setAncientBook(boolean bool) {
		return this.ancientcurses = bool;
	}

	public void switchPrayBook(boolean book) {
		this.ancientcurses = book;
		ActionSender.sendConfig(player, 1584, book ? 1 : 0);
		ActionSender.sendAMask(player, 0, 27, 271, 6, 0, 2);
		closeAllPrayers();
	}

	public void setQuickPrayers() {
		ActionSender.sendBConfig(player, 181, 0);
		ActionSender.sendBConfig(player, 149, 6);
		this.player.getPrayer().usingQuickPrayer = false;
	}


	public void tick() {
		//credits Scu11
		double amountDrain = 0;
		int book = getPrayerBook();
		for (int i = 0; i < onPrayers[book].length; i++) {
			if (usingPrayer(book, i)) {
				double drain = drainRate(i);
				double bonus = 0.0035 * player.getBonuses().getBonus(Skills.PRAYER);
				drain = drain * (1 + bonus);
				drain = 0.6 / drain;
				amountDrain += drain;
			}
		}
		if (amountDrain > 0) {
			player.getSkills().drainPray(amountDrain);
			checkPrayer();
		}
		if (curses.size() > 0) {
			Mob victim = player.getCombatExecutor().getVictim();
			if (victim != null) {
				if (victim != null && player.getSettings().getHitCounter() >= 3 && RANDOM.nextDouble() < 0.50) {
					player.getSettings().resetHitCounter();
					curses.get(RANDOM.nextInt(curses.size())).curse(victim);
				}
			}
		}
	}

	private Curse createCurse(int id) {
		if (id >= Prayer.SAP_WARRIOR && id <= Prayer.SAP_SPIRIT) {
			return new Curse(id);
		} else if (id >= Prayer.LEECH_ATTACK && id <= Prayer.LEECH_SPECIAL_ATTACK) {
			return new Curse(id);
		}
		return null;
	}

	/**
	 * @author 'Mystic Flow
	 */
	private class Curse {

		private int id;

		private int maximumDrain;
		private int maximumBoost;
		private Map<Mob, Integer> cursedMobs = new HashMap<Mob, Integer>();

		private Curse(int id) {
			this.id = id;
			if (id >= Prayer.SAP_WARRIOR && id <= Prayer.SAP_SPIRIT) {
				this.maximumDrain = 20;
			} else {
				this.maximumDrain = 25;
				this.maximumBoost = 5;
			}
		}

		public void curse(Mob victim) {
			int[] skills = getSkills();
			if (skills == null) {
				if (RANDOM.nextDouble() > 0.30) {
					return;
				}
				if (victim.isPlayer()) { //only players have spec and run
					Player playerVictim = victim.getPlayer();
					int projectileId = -1;
					switch (id) {
					case Prayer.LEECH_SPECIAL_ATTACK:
						int drain = playerVictim.getSpecialAmount() < 100 ? playerVictim.getSpecialAmount() : 100;
						if (drain > 0) {
							int newAmount = player.getSpecialAmount() + drain;
							if (newAmount > 1000) {
								newAmount = 1000;
							}
							player.setSpecialAmount(newAmount);
							player.sendMessage("You leech some special attack energy from your enemy.");
							player.graphics(2223);
							player.animate(12575);

							playerVictim.graphics(2225);
							playerVictim.setSpecialAmount(playerVictim.getSpecialAmount() - drain);
							playerVictim.sendMessage("Your special attack energy has been drained by an enemy curse.");
							projectileId = 2256;
						}
						break;
					case Prayer.LEECH_ENERGY:
						drain = playerVictim.getWalkingQueue().getRunEnergy() < 10 ? playerVictim.getWalkingQueue().getRunEnergy() : 10;
						if (drain > 0) {
							int newAmount = player.getWalkingQueue().getRunEnergy() + drain;
							if (newAmount > 100) {
								newAmount = 100;
							}
							player.getWalkingQueue().setRunEnergy(newAmount);
							player.sendMessage("You leech some run energy from your enemy.");
							player.animate(12575);

							playerVictim.graphics(2254);
							playerVictim.getWalkingQueue().setRunEnergy(playerVictim.getWalkingQueue().getRunEnergy() - drain);
							playerVictim.sendMessage("Your run energy has been drained by an enemy curse.");
							projectileId = 2252;

							ActionSender.sendRunEnergy(player);
							ActionSender.sendRunEnergy(playerVictim);
						}
						break;
					}
					ProjectileManager.sendGlobalProjectile(projectileId, player, victim, 30, 35, 30, 10, 20);
				}
				return;
			}
			int drain = Misc.random(1) + 1;
			boolean b = false;
			if (!cursedMobs.containsKey(victim)) {
				cursedMobs.put(victim, drain);
				b = true;
			}
			double drained = cursedMobs.get(victim);
			if (drained >= maximumDrain) {
				return;
			}
			for (int i : skills) {
				player.sendMessage("Your curse drains " + Skills.SKILL_NAME[i] + " from the enemy, boosting your " + Skills.SKILL_NAME[i] + ".");
				switch (i) {
				case Skills.ATTACK:
					attackModifier += drain;
					if (attackModifier > maximumDrain + maximumBoost) {
						attackModifier = maximumDrain + maximumBoost;
					}
					break;
				case Skills.DEFENCE:
					defenceModifier += drain;
					if (defenceModifier > maximumDrain + maximumBoost) {
						defenceModifier = maximumDrain + maximumBoost;
					}
					break;
				case Skills.STRENGTH:
					strengthModifier += drain;
					if (strengthModifier > maximumDrain + maximumBoost) {
						strengthModifier = maximumDrain + maximumBoost;
					}
					break;
				case Skills.RANGE:
					rangeModifier += drain;
					if (rangeModifier > maximumDrain + maximumBoost) {
						rangeModifier = maximumDrain + maximumBoost;
					}
					break;
				case Skills.MAGIC:
					magicModifier += drain;
					if (magicModifier > maximumDrain + maximumBoost) {
						magicModifier = maximumDrain + maximumBoost;
					}
					break;
				}
				Player playerVictim = victim.getPlayer();
				if (playerVictim != null) {
					playerVictim.sendMessage("Your " + Skills.SKILL_NAME[i] + " has been drained by an enemy curse.");
					switch (i) {
					case Skills.ATTACK:
						playerVictim.getPrayer().attackModifier -= drain;
						break;
					case Skills.DEFENCE:
						playerVictim.getPrayer().defenceModifier -= drain;
						break;
					case Skills.STRENGTH:
						playerVictim.getPrayer().strengthModifier -= drain;
						break;
					case Skills.RANGE:
						playerVictim.getPrayer().rangeModifier -= drain;
						break;
					case Skills.MAGIC:
						playerVictim.getPrayer().magicModifier -= drain;
						break;
					}
					playerVictim.getPrayer().update();
				} else {
					switch (i) {
					case Skills.ATTACK:
						victim.getNPC().decreaseAttackModifier(drain);
						break;
					case Skills.DEFENCE:
						victim.getNPC().decreaseDefenceModifier(drain);
						break;
					case Skills.STRENGTH:
						victim.getNPC().decreaseStrengthModifier(drain);
						break;
					case Skills.RANGE:
						victim.getNPC().decreaseRangeModifier(drain);
						break;
					case Skills.MAGIC:
						victim.getNPC().decreaseMagicModifier(drain);
						break;
					}
				}
			}
			if (skills.length > 1) {
				player.animate(12569);
				player.graphics(2211 + id * 3);
				ProjectileManager.sendGlobalProjectile(2212 + id * 3, player, victim, 30, 35, 30, 10, 20);
				victim.graphics(2213 + id * 3);
			} else {
				int i = id - 9;
				if (i > 1) {
					victim.graphics(2230 + i * 4);
					ProjectileManager.sendGlobalProjectile(2228 + i * 4, player, victim, 30, 35, 30, 10, 20);
				} else {
					victim.graphics(2232);
					ProjectileManager.sendGlobalProjectile(2231, player, victim, 30, 35, 45, 10, 20);
				}
				player.animate(12575);
			}
			if (!b) {
				cursedMobs.put(victim, cursedMobs.get(victim) + drain);
			}
			update();
		}

		public void deactivate() {
			for (Map.Entry<Mob, Integer> entry : cursedMobs.entrySet()) {
				int[] skills = getSkills();
				if (skills == null) {
					return;
				}
				Mob victim = entry.getKey();
				if (victim.destroyed()) {
					continue;
				}
				int drained = entry.getValue();
				for (int i : skills) {
					if (victim.isPlayer()) {
						victim.getPlayer().sendMessage("Your " + Skills.SKILL_NAME[i] + " is now unaffected by sap and leech curses.");
						switch (i) {
						case Skills.ATTACK:
							victim.getPlayer().getPrayer().attackModifier += drained;
							break;
						case Skills.DEFENCE:
							victim.getPlayer().getPrayer().defenceModifier += drained;
							break;
						case Skills.STRENGTH:
							victim.getPlayer().getPrayer().strengthModifier += drained;
							break;
						case Skills.RANGE:
							victim.getPlayer().getPrayer().rangeModifier += drained;
							break;
						case Skills.MAGIC:
							victim.getPlayer().getPrayer().magicModifier += drained;
							break;
						}
					} else {
						switch (i) {
						case Skills.ATTACK:
							victim.getNPC().decreaseAttackModifier(-drained);
							break;
						case Skills.DEFENCE:
							victim.getNPC().decreaseDefenceModifier(-drained);
							break;
						case Skills.STRENGTH:
							victim.getNPC().decreaseStrengthModifier(-drained);
							break;
						case Skills.RANGE:
							victim.getNPC().decreaseRangeModifier(-drained);
							break;
						case Skills.MAGIC:
							victim.getNPC().decreaseMagicModifier(-drained);
							break;
						}
					}
					switch (i) {
					case Skills.ATTACK:
						attackModifier -= drained;
						break;
					case Skills.DEFENCE:
						defenceModifier -= drained;
						break;
					case Skills.STRENGTH:
						strengthModifier -= drained;
						break;
					case Skills.RANGE:
						rangeModifier -= drained;
						break;
					case Skills.MAGIC:
						magicModifier -= drained;
						break;
					}
				}

			}
		}

		public int[] getSkills() {
			int[] skills = null;
			switch (id) {
			case Prayer.SAP_WARRIOR:
				skills = new int[]{
						Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE
				};
				break;
			case Prayer.SAP_RANGER:
				skills = new int[]{
						Skills.RANGE, Skills.DEFENCE
				};
				break;
			case Prayer.SAP_MAGE:
				skills = new int[]{
						Skills.MAGIC, Skills.DEFENCE
				};
				break;
			case Prayer.LEECH_ATTACK:
				skills = new int[]{
						Skills.ATTACK
				};
				break;
			case Prayer.LEECH_STRENGTH:
				skills = new int[]{
						Skills.STRENGTH
				};
				break;
			case Prayer.LEECH_DEFENCE:
				skills = new int[]{
						Skills.DEFENCE
				};
				break;
			case Prayer.LEECH_MAGIC:
				skills = new int[]{
						Skills.MAGIC
				};
				break;
			case Prayer.LEECH_RANGE:
				skills = new int[]{
						Skills.RANGE
				};
				break;
			}
			return skills;
		}

	}

	/**
	 * Updates the dynamic prayer bonuses on the screen.
	 */
	public void update() {
		int stat = 30;
		int value = (stat + attackModifier)
		| ((stat + strengthModifier) << 6)
		| ((stat + defenceModifier) << 12)
		| ((stat + rangeModifier) << 18)
		| ((stat + magicModifier) << 24);
		ActionSender.sendConfig(player, 1583, value);
	}

	public static void wrathEffect(Mob mob, Mob killer) {
		Location center = null;
		int damage = -1;
		int radius = 2;
		if (mob.isPlayer() && mob.getPlayer().getPrayer().usingPrayer(1, Prayer.WRATH)) {
			mob.animate(12583);
			center = mob.getLocation();
			damage = mob.getPlayer().getSkills().getLevelForExperience(Skills.PRAYER) * 3;
		} else if (mob.isNPC() && mob.getNPC().isNex()) {
			center = mob.getLocation().transform(1, 1, 0);
			damage = 120 * 3;
			radius = 10;
		}
		if (center != null) {
			ActionSender.spawnPositionedGraphic(center, 2259);
			for (int x = -2; x <= 2; x += 2) {
				for (int y = -2; y <= 2; y += 2) {
					Location loc = center.transform(x, y, 0);
					if (loc != center) {
						ActionSender.spawnPositionedGraphic(loc, 2260);
					}
				}
			}
			for (int x = -1; x <= 1; x += 2) {
				for (int y = -1; y <= 1; y += 2) {
					Location loc = center.transform(x, y, 0);
					if (loc != center) {
						ActionSender.spawnPositionedGraphic(loc, 2260);
					}
				}
			}
			if (killer != null) {
				if (mob.isMulti()) {
					if (killer.isNPC()) {
						List<NPC> npcs = Region.getLocalNPCs(mob.getLocation(), radius);
						npcs.remove(mob);
						for (NPC n : npcs) {
							if (!n.isDead() && (n == killer || n.isAttackable(mob))) {
								n.getDamageManager().damage(mob, mob.getRandom().nextInt(damage), damage, DamageType.RED_DAMAGE);
							}
						}
						return;
					}
					List<Player> players = Region.getLocalPlayers(mob.getLocation(), radius);
					players.remove(mob);
					for (Player p : players) {
						if (!p.isDead() && (p == killer || p.isAttackable(mob))) {
							p.getDamageManager().damage(mob, mob.getRandom().nextInt(damage), damage, DamageType.RED_DAMAGE);
						}
					}
				} else if (killer.getLocation().getDistance(mob.getLocation()) <= radius) {
					killer.getDamageManager().damage(mob, mob.getRandom().nextInt(damage), damage, DamageType.RED_DAMAGE);
				}
			}
		}
	}

	/**
	 * Submits the retribution effect.
	 * @param mob The mob.
	 */
	 public static void retributionEffect(Mob mob, Mob killer) {
		 if (mob.isNPC() || !mob.getPlayer().getPrayer().usingPrayer(0, Prayer.RETRIBUTION)) {
			 return;
		 }
		 mob.graphics(437);
		 if (killer == null) {
			 return;
		 }
		 final int damage = (int) (mob.getPlayer().getSkills().getLevelForExperience(Skills.PRAYER) * 2.5);
		 if (mob.isMulti()) {
			 if (killer.isNPC()) {
				 List<NPC> npcs = Region.getLocalNPCs(mob.getLocation(), 1);
				 for (NPC n : npcs) {
					 if (!n.isDead() && (n == killer || n.isAttackable(mob))) {
						 n.getDamageManager().damage(mob, mob.getRandom().nextInt(damage), damage, DamageType.RED_DAMAGE);
					 }
				 }
				 return;
			 }
			 List<Player> players = Region.getLocalPlayers(mob.getLocation(), 1);
			 players.remove(mob);
			 for (Player p : players) {
				 if (!p.isDead() && (p == killer || p.isAttackable(mob))) {
					 p.getDamageManager().damage(mob, mob.getRandom().nextInt(damage), damage, DamageType.RED_DAMAGE);
				 }
			 }
		 } else if (killer.getLocation().getDistance(mob.getLocation()) <= 1) {
			 killer.getDamageManager().damage(mob, mob.getRandom().nextInt(damage), damage, DamageType.RED_DAMAGE);
		 }
	 }

	 public boolean[][] getQuickPrayers() {
		 return quickPrayers;
	 }

	 /**
	  * Updates the turmoil effect.
	  *
	  * @param mob The victim.
	  */
	 public void updateTurmoil(Mob mob) {
		 if (mob == null) {
			 player.getAttribute("hasTurmoil", false);
			 player.setAttribute("turmoilEffect", true);
			 attackModifier = 0; //TODO: Check if this player is cursed
			 strengthModifier = 0;
			 defenceModifier = 0;
			 update();
			 return;
		 }
		 if (player.getAttribute("hasTurmoil", false)) {
			 return;
		 }
		 if (!player.getAttribute("turmoilEffect", false)) {
			 player.setAttribute("turmoilEffect", true);
			 return;
		 }
		 player.getAttribute("hasTurmoil", true);
		 player.setAttribute("turmoilEffect", false);
		 boolean p = mob.isPlayer();
		 int strength = p ? mob.getPlayer().getSkills().getLevelForExperience(Skills.STRENGTH) : mob.getNPC().getDefinition().getStrengthLevel();
		 int attack = p ? mob.getPlayer().getSkills().getLevelForExperience(Skills.ATTACK) : mob.getNPC().getDefinition().getAttackLevel();
		 int defence = p ? mob.getPlayer().getSkills().getLevelForExperience(Skills.DEFENCE) : mob.getNPC().getDefinition().getDefenceLevel();
		 int strengthIncrease = (int) (strength * 0.1);
		 int attackIncrease = (int) (attack * 0.15);
		 int defenceIncrease = (int) (defence * 0.15);
		 strengthIncrease = strengthIncrease > 9 ? 9 : strengthIncrease;
		 attackIncrease = attackIncrease > 14 ? 14 : attackIncrease;
		 defenceIncrease = defenceIncrease > 14 ? 14 : defenceIncrease;
		 attackModifier += attackIncrease;
		 strengthModifier += strengthIncrease;
		 defenceModifier += defenceIncrease;
		 if (attackModifier > attackIncrease)
			 attackModifier = attackIncrease;
		 if (defenceModifier > defenceIncrease)
			 defenceModifier = defenceIncrease;
		 if (strengthModifier > strengthIncrease)
			 strengthModifier = strengthIncrease;
		 update();
	 }

}
