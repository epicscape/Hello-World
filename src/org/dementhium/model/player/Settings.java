package org.dementhium.model.player;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.content.clans.Clan;
import org.dementhium.model.Mob;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Settings {

    private Clan currentClan;
    private int chances;

    private int spellBook = 192;
    private int privateTextColor;

    private boolean autoRetaliate = false;
    private int specialAmount = 1000;
    private boolean usingSpecial = false;
    
    private int recoilDamage = 400;

    private int summoningOption = 0;
    private int combatType;
    private int combatStyle;
    private int graveStone = 0;
    
    private int[] fairyRingCombination = new int[3];
    
    private int[] tokens = new int[5];
    private int[] killCount = new int[10];
    private boolean[] killedBrothers = new boolean[6];
    private boolean[] leveledUp = new boolean[Skills.SKILL_COUNT];
    private String[] playerOptions = new String[6];

    /**
     * The barrows brothers that are killed.
     */
    private List<Integer> barrowsKilled = new ArrayList<Integer>();
    
    /**
     * The current tunnel index for barrows.
     */
    private int tunnelId = -1;
    
    /**
     * The current tunnel entrance index for barrows.
     */
    private int tunnelEntranceId = -1;
    
    /**
     * The amount of barrows activity kill counts.
     */
    private int barrowsKillcount = 0;
    
    /**
     * The stronghold chests that have been looted.
     */
    private boolean[] strongholdChestLooted = new boolean[4];

    private int amountToProduce;

    private int maxToProduce;

    private int itemToProduce;

    private int dialoguesSkill;
    private int itemUsed;

    private boolean godEntranceRope = false;

    private int[] possibleProductions;
    private int lastXAmount;
    private int lastSelection;

    private int hitCounter;

    private Mob speakingTo;

    private boolean resting;

    public void setCurrentClan(Clan currentClan) {
        this.currentClan = currentClan;
    }

    public Clan getCurrentClan() {
        return currentClan;
    }

    public void setSpellBook(int spellBook) {
        this.spellBook = spellBook;
    }

    public int getSpellBook() {
        return spellBook;
    }

    public void setUsingSpecial(boolean usingSpecial) {
        this.usingSpecial = usingSpecial;
    }

    public boolean isUsingSpecial() {
        return usingSpecial;
    }

    public boolean isAutoRetaliate() {
        return autoRetaliate;
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }

    public int getSpecialAmount() {
        return specialAmount;
    }

    public void setSpecialAmount(int specialAmount) {
        this.specialAmount = specialAmount;
    }

    public void setCombatType(int combatType) {
        this.combatType = combatType;
    }

    public void setCombatStyle(int combatStyle) {
        this.combatStyle = combatStyle;
    }

    public int getCombatType() {
        return combatType;
    }

    public int getCombatStyle() {
        return combatStyle;
    }

    public void setPrivateTextColor(int privateTextColor) {
        this.privateTextColor = privateTextColor;
    }

    public int getPrivateTextColor() {
        return privateTextColor;
    }

    public int[] getKillCount() {
        return killCount;
    }

    public boolean[] getKilledBrothers() {
        return killedBrothers;
    }

    public void setAmountToProduce(int i) {
        this.amountToProduce = i;
    }

    public void decreaseAmountToProduce() {
        if (amountToProduce > 0) {
            amountToProduce--;
        }
    }

    public void increaseAmountToProduce() {
        if (amountToProduce < maxToProduce) {
            amountToProduce++;
        }
    }

    public void setMaxToProduce(int i) {
        maxToProduce = i;
    }

    public int getAmountToProduce() {
        return amountToProduce;
    }

    public void setItemToProduce(int itemUsed) {
        this.itemToProduce = itemUsed;

    }

    public int getItemToProduce() {
        return itemToProduce;

    }

    public void setDialougeSkill(int skillId) {
        this.dialoguesSkill = skillId;

    }

    public int getDialoguesSkill() {
        return dialoguesSkill;

    }

    public void setItemUsed(int id) {
        this.itemUsed = id;
    }

    public int getItemUsed() {
        return itemUsed;
    }

    public boolean[] getLeveledUp() {
        return leveledUp;
    }

    public void setPossibleProductions(int[] items) {
        this.possibleProductions = items;
    }

    public int[] getPossibleProductions() {
        return possibleProductions;
    }

    public void incChances() {
        chances++;
    }

    public void decChances() {
        chances--;
    }

    public int getChances() {
        return chances;
    }

    public void setChances(int i) {
        this.chances = i;
    }

    public int getLastXAmount() {
        return lastXAmount;
    }

    public void setLastXAmount(int lastXAmount) {
        this.lastXAmount = lastXAmount;
    }

    public void setGodEntranceRope(boolean godEntranceRope) {
        this.godEntranceRope = godEntranceRope;
    }

    public boolean hasGodEntranceRope() {
        return godEntranceRope;
    }

    public void setLastSelection(int lastSelection) {
        this.lastSelection = lastSelection;
    }

    public int getLastSelection() {
        return lastSelection;
    }

    public int getHitCounter() {
        return hitCounter;
    }

    public void resetHitCounter() {
        hitCounter = 0;
    }

    public void incrementHitCounter() {
        hitCounter++;
    }

    public void setSpeakingTo(Mob speakingTo) {
        this.speakingTo = speakingTo;
    }

    public Mob getSpeakingTo() {
        return speakingTo;
    }

    public boolean[] getStrongholdChest() {
        return strongholdChestLooted;
    }

    public void setResting(boolean resting) {
        this.resting = resting;
    }

    public boolean isResting() {
        return resting;
    }

    public int[] getTokens() {
        return tokens;
    }

    public void setGraveStone(int id) {
        this.graveStone = id;
    }

    public int getGraveStone() {
        return graveStone;
    }
    
	public String[] getPlayerOptions() {
		return playerOptions;
	}

	/**
	 * @return the recoilDamage
	 */
	public int getRecoilDamage() {
		return recoilDamage;
	}

	/**
	 * @param recoilDamage the recoilDamage to set
	 */
	public void setRecoilDamage(int recoilDamage) {
		this.recoilDamage = recoilDamage;
	}
	
	public int[] getFairyRingCombination() {
		return fairyRingCombination;
	}

	/**
	 * @return the summoningOption
	 */
	public int getSummoningOption() {
		return summoningOption;
	}

	/**
	 * @param summoningOption the summoningOption to set
	 */
	public void setSummoningOption(int summoningOption) {
		this.summoningOption = summoningOption;
	}

	/**
	 * @return the barrowsKilled
	 */
	public List<Integer> getBarrowsKilled() {
		return barrowsKilled;
	}

	/**
	 * @param barrowsKilled the barrowsKilled to set
	 */
	public void setBarrowsKilled(List<Integer> barrowsKilled) {
		this.barrowsKilled = barrowsKilled;
	}

	/**
	 * @return the tunnelId
	 */
	public int getTunnelId() {
		return tunnelId;
	}

	/**
	 * @param tunnelId the tunnelId to set
	 */
	public void setTunnelId(int tunnelId) {
		this.tunnelId = tunnelId;
	}

	/**
	 * @return the tunnelEntranceId
	 */
	public int getTunnelEntranceId() {
		return tunnelEntranceId;
	}

	/**
	 * @param tunnelEntranceId the tunnelEntranceId to set
	 */
	public void setTunnelEntranceId(int tunnelEntranceId) {
		this.tunnelEntranceId = tunnelEntranceId;
	}

	/**
	 * @return the barrowsKillcount
	 */
	public int getBarrowsKillcount() {
		return barrowsKillcount;
	}

	/**
	 * @param barrowsKillcount the barrowsKillcount to set
	 */
	public void setBarrowsKillcount(int barrowsKillcount) {
		this.barrowsKillcount = barrowsKillcount;
	}

}