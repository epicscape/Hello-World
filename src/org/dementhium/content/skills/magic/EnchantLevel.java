package org.dementhium.content.skills.magic;

import org.dementhium.model.Item;

public enum EnchantLevel {
	SAPPHIRE(7, 29, 17.5, new Item(555, 1)), EMERALD(27, 41, 37, new Item(556,
			1)), RUBY(49, 53, 59, new Item(554, 5)), DIAMOND(57, 61, 67,
			new Item(557, 10)), DRAGONSTONE(68, 76, 78, new Item(557, 15),
			new Item(555, 15)), ONYX(87, 88, 97, new Item(557, 20), new Item(
			554, 20));
	private final int childId;
	private final Item[] runes;
	private final double experience;
	private final int levelReq;

	private EnchantLevel(int levelReq, int childId, double experience,
			Item... runes) {
		this.levelReq = levelReq;
		this.childId = childId;
		this.experience = experience;
		this.runes = runes;
	}

	public int getChildId() {
		return childId;
	}

	public Item[] getRunes() {
		return runes;
	}

	public double getExperience() {
		return experience;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public static EnchantLevel forChild(int childId) {
		for (EnchantLevel level : EnchantLevel.values()) {
			if (level.getChildId() == childId)
				return level;
		}
		return null;
	}
}