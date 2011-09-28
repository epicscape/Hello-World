package org.dementhium.content.misc;

import org.dementhium.model.Item;

/**
 * Represents an item with a chance to receive.
 * @author Emperor
 *
 */
public class ChanceItem {

	/**
	 * The item to receive.
	 */
	private final Item item;
	
	/**
	 * The rarity.
	 */
	private final int rarity;
	
	/**
	 * Constructs a new {@code ChanceItem} {@code Object}.
	 * @param item The item to receive.
	 * @param rarity The rarity.
	 */
	public ChanceItem(Item item, int rarity) {
		this.item = item;
		this.rarity = rarity;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the rarity
	 */
	public int getRarity() {
		return rarity;
	}
}