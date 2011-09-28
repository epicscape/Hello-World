package org.dementhium.model.player;

import org.dementhium.content.misc.GraveStoneManager;
import org.dementhium.content.skills.Prayer;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

public class Skills {

	public static final int SKILL_COUNT = 25;
	public static final double MAXIMUM_EXP = 200000000;

	private final Player player;
	private final int level[] = new int[SKILL_COUNT];
	private final double xp[] = new double[SKILL_COUNT];
	private int hitPoints;
	private int hitPointsRaised;
	private int experienceCounter;


	private boolean dead = false;

	//1801
	private double prayerPoints; //for flashing or w.e it's called

	public static final String[] SKILL_NAME = {"Attack", "Defence",
		"Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking",
		"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
		"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
		"Farming", "Runecrafting", "Hunter", "Construction", "Summoning",
	"Dungeoneering"};

	// 0 - 1, 1 - 4, 2 - 2,

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
	HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
	WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
	CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
	AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
	RUNECRAFTING = 20, CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23,
	DUNGEONEERING = 23;

	public static final int[] COMBAT_SKILLS =
	{
		ATTACK, DEFENCE, STRENGTH, HITPOINTS, RANGE, PRAYER, MAGIC, SUMMONING
	};


	public Skills(Player player) {
		this.player = player;
		for (int i = 0; i < SKILL_COUNT; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		hitPoints = 100;
		prayerPoints = 1;
	}

	public void hit(int hitDiff) {
		if (hitDiff > hitPoints)
			hitDiff = hitPoints;
		hitPoints -= hitDiff;
		if (hitPoints < 1)
			sendDead();
		if (hitPoints > getMaxHitpoints()) {
			hitPoints = getMaxHitpoints();
		}
		ActionSender.sendConfig(player, 1240, hitPoints * 2);
	}

	public boolean isDead() {
		return hitPoints <= 0;
	}

	public int getMaxHitpoints() {
		return (getLevelForExperience(Skills.HITPOINTS) * 10) + hitPointsRaised;
	}

	//@SuppressWarnings("unused")
	public void sendDead() {
		/*if (true) {
              hitPoints = 990;
              return;
          }*/
          if (dead) {
        	  return;
          }
          dead = true;
          Mob last = player.getCombatExecutor().getLastAttacker();
          System.out.println("lelelel0");
          if (last != null) {
          		last.setAttribute("combatTicks", 0);
          }
          System.out.println("lelelel1");
          World.getWorld().submit(new Tick(1) {
        	  public void execute() {
        		  System.out.println("lelelel2");
        		  stop();
        		  if (!player.isOnline() || player.destroyed()) {
        			  return;
        		  }
        		  System.out.println("lelelel3");
        		  player.animate(9055);
        		  Prayer.wrathEffect(player, player.getCombatExecutor().getLastAttacker());
        		  Prayer.retributionEffect(player, player.getCombatExecutor().getLastAttacker());
        		  player.removeTick("nex_virus");
        		  System.out.println("lelelel4");
        		  player.submitTick("death_tick", new Tick(4) {
        			  @Override
        			  public void execute() {
        				  stop();
        				  dead = false;
        				  for (int i = 0; i < SKILL_COUNT; i++) {
        					  set(i, getLevelForExperience(i));
        				  }
        				  hitPoints = getMaxHitpoints();
        				  player.animate(Animation.RESET);
        				  player.getPrayer().closeAllPrayers();
        				  player.sendMessage("Oh dear, you have died.");
        				  ActionSender.sendConfig(player, 1240, hitPoints * 2);
        				  player.setSpecialAmount(1000);
        				  player.resetCombat();
        				  player.getPoisonManager().removePoison();
        				  player.setAttribute("teleblock", 0);
        				  player.setAttribute("freezeTime", 0);
        				  Mob killer = player.getDamageManager().getKiller();
        				  if (!player.getActivity().onDeath(player)) { //In a safe activity we don't drop items or teleport to the DEFAULT_LOCATION, this will be done in the onDeath method.
        					  if (killer != null && killer != player) {
        						  if (player.getRights() != 2) {
        							  GraveStoneManager.appendDeath(player, killer);
        						  }
        					  }
        					  player.teleport(Mob.DEFAULT);
        				  }
        			  }

        		  }, true);
        	  }
          });
	}

	public void heal(int hitDiff) {
		if (isDead()) {
			sendDead();
			return;
		}
		hitPoints += hitDiff;
		int max = getMaxHitpoints();
		if (hitPoints > max) {
			hitPoints = max;
		}
		ActionSender.sendConfig(player, 1240, hitPoints * 2);
	}


	public void healRocktail(int healAmount) {
		hitPoints += healAmount;
		int max = getMaxHitpoints() + 100;
		if (hitPoints > max) {
			hitPoints = max;
		}
		ActionSender.sendConfig(player, 1240, hitPoints * 2);
	}


	public void heal(int hitDiff, int type) {
		hitPoints += hitDiff;
		int max = type;
		if (hitPoints > max) {
			hitPoints = max;
		}
		ActionSender.sendConfig(player, 1240, hitPoints * 2);
	}

	public void raiseTotalHp(int raise) {
		hitPointsRaised += raise;

		heal(0);
	}

	public void lowerTotalHp(int lower) {
		hitPointsRaised -= lower;

		if (hitPoints <= 0) {
			hitPoints = 1;
		}
		heal(0);
	}

	public void restorePray(double restore) {
		setPrayerPoints(prayerPoints + restore, true);
		int max = getLevelForExperience(5);
		if (prayerPoints > max) {
			prayerPoints = max;
		}
		ActionSender.sendSkillLevel(player, 5);
	}

	public void drainPray(double drain) {
		setPrayerPoints(prayerPoints - drain, true);
		if (prayerPoints < 1) {
			prayerPoints = 0;
		}
		ActionSender.sendSkillLevel(player, 5);
	}

	public void reset() {
		for (int i = 0; i < SKILL_COUNT; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		hitPoints = 100;
		prayerPoints = 1;
		refresh();
	}

	public int getCombatLevel() {
		int attack = getLevelForExperience(0);
		int defence = getLevelForExperience(1);
		int strength = getLevelForExperience(2);
		int hp = getLevelForExperience(3);
		int prayer = getLevelForExperience(5);
		int ranged = getLevelForExperience(4);
		int magic = getLevelForExperience(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		int summoning = getLevelForExperience(Skills.SUMMONING);
		summoning /= 8;
		return combatLevel + summoning;
	}

	public int getLevel(int skill) {
		return level[skill];
	}

	public double getXp(int skill) {
		return xp[skill];
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0
					* Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForExperience(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl < (skill == 24 ? 121 : 100); lvl++) {
			points += Math.floor(lvl + 300.0
					* Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return skill == 24 ? 120 : 99;
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		ActionSender.sendSkillLevel(player, skill);
	}

	public static final double XP_MODIFIER = 50.0;

	public void addExperience(int skill, double exp) {
		int oldLevel = getLevelForExperience(skill);
		int oldCombat = getCombatLevel();
		double experience;
		if (skill == ATTACK || skill == DEFENCE || skill == STRENGTH || skill == RANGE || skill == MAGIC || skill == HITPOINTS) {
			experience = exp * XP_MODIFIER * 2;
		} else {
			experience = exp * (player.isDoubleXP() ? (XP_MODIFIER * 1.5) : (XP_MODIFIER));
		}
		xp[skill] += experience;
		experienceCounter += experience;
		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForExperience(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getSettings().getLeveledUp()[skill] = true;
			player.setAttribute("leveledUp", Boolean.TRUE);
			if (skill == HITPOINTS) {
				heal(levelDiff * 10);
			}
			if(skill == PRAYER) {
				restorePray(levelDiff);
			}
			if (oldCombat != getCombatLevel()) {
				player.getMask().setApperanceUpdate(true);
			}
		}
		ActionSender.sendSkillLevel(player, skill);
	}


	public void set(int skill, int val) {
		if (skill == Skills.PRAYER) {
			prayerPoints = val;
		}
		if (val < 0) {
			val = 0;
		}
		level[skill] = val;
		ActionSender.sendSkillLevel(player, skill);
	}

	public void setLevelAndXP(int skill, int level, double xp) {
		if (skill == Skills.PRAYER) {
			prayerPoints = level;
		}
		this.level[skill] = level;
		this.xp[skill] = xp;
	}

	public void sendSkillLevels() {
		for (int i = 0; i < Skills.SKILL_COUNT; i++)
			ActionSender.sendSkillLevel(player, i);
	}

	public void refresh() {
		sendSkillLevels();
		ActionSender.sendConfig(player, 1240, hitPoints * 2);
		this.player.getMask().setApperanceUpdate(true);
	}

	public boolean isLevelBelowOriginal(int skill) {
		return level[skill] < getLevelForExperience(skill);
	}

	public boolean isLevelBelowOriginalModification(int skill, int modification) {
		return level[skill] < (getLevelForExperience(skill) + modification);
	}

	public void increaseLevelToMaximum(int skill, int modification) {
		if (isLevelBelowOriginal(skill)) {
			setLevel(skill, level[skill] + modification >= getLevelForExperience(skill) ? getLevelForExperience(skill) : level[skill] + modification);
		}
	}

	public void increaseLevelToMaximumModification(int skill, int modification) {
		if (isLevelBelowOriginalModification(skill, modification)) {
			setLevel(skill, level[skill] + modification >= (getLevelForExperience(skill) + modification) ? (getLevelForExperience(skill) + modification) : level[skill] + modification);
		}
	}

	public void decreaseLevelToMinimum(int skill, int modification) {
		if (level[skill] > 1) {
			setLevel(skill, level[skill] - modification <= 1 ? 1 : level[skill] - modification);
		}
	}

	public void decreaseLevelToZero(int skill, int modification) {
		if (level[skill] > 0) {
			setLevel(skill, level[skill] - modification <= 0 ? 0 : level[skill] - modification);
		}
	}

	public void decreaseLevelOnce(int skill, int amount) {
		if (level[skill] > (getLevelForExperience(skill) - amount)) { //this stops resetting levels. EG if I have 87/95 str and it decreases 5, normally it would reset it to 90/95, however this line prevents it!
			if (level[skill] - amount <= (getLevelForExperience(skill) - amount)) {
				level[skill] = (getLevelForExperience(skill) - amount);
			} else {
				level[skill] -= amount;
			}
			ActionSender.sendSkillLevel(player, skill);
		}
	}

	public void setLevel(int skill, int level) {
		this.level[skill] = level;
		ActionSender.sendSkillLevel(player, skill);
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public double getPrayerPoints() {
		return prayerPoints;
	}

	public void setPrayerPoints(double prayerPoints, boolean update) {
		int lvlBefore = (int) Math.ceil(this.prayerPoints);
		this.prayerPoints = prayerPoints;
		int lvlAfter = (int) Math.ceil(this.prayerPoints);
		if (update && (lvlBefore - lvlAfter >= 1 || lvlAfter - lvlBefore >= 1)) {
			ActionSender.sendSkillLevel(player, Skills.PRAYER);
		}
	}

	public int getExperienceCounter() {
		return experienceCounter;
	}

	public void setExperienceCounter(int experienceCounter) {
		this.experienceCounter = experienceCounter;
	}

	public void completeRestore() {
		for (int i = 0; i < SKILL_COUNT; i++) {
			set(i, getLevelForExperience(i));
		}
		hitPoints = getMaxHitpoints();
		//		for (int i = 0; i < level.length; i++) {
			//			level[i] = getLevelForExperience(i);
			//			ActionSender.sendSkillLevel(player, i);
		//		}
		//		heal(990);
	}

	public void setHitpointsRaised(int amount) {
		this.hitPointsRaised = amount;
	}

	/**
	 * Drains a skill and returns the amount that couldn't be drained.
	 * @param skill The skill id.
	 * @param drain The value to decrease.
	 * @return The amount left to decrease. (ex. if attackLevel - drain < 0,
	 * <br>		we return drain - attackLevel)
	 */
	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		ActionSender.sendSkillLevel(player, skill);
		return drainLeft;
	}


}
