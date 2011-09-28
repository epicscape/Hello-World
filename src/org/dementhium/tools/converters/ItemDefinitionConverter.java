package org.dementhium.tools.converters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.dementhium.cache.Cache;
import org.dementhium.model.definition.ItemDefinition;

/**
 * Converts the dumped item definitions to binary format.
 * @author Emperor
 *
 */
public class ItemDefinitionConverter {
	
	/**
	 * The progress bar.
	 */
	//private static ProgressBar progress = new ProgressBar("Item definitions converting.", Cache.getAmountOfItems() << 1);

	/**
	 * The current amount of item definitions done.
	 */
	private static int currentAmount = 0;
	
	/**
	 * The main method.
	 * @param args The arguments.
	 */
	public static void main(String...args) {
        Cache.init();
        try {
			ItemDefinition.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
        //loadDefinitions();
        saveDefinitions();
	}

	/**
	 * Loads all the definitions.
	 */
	@SuppressWarnings("unused")
	private static void loadDefinitions() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./data/item/ItemDump.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		String s;
		try {
			ItemDefinition def;
			StringBuilder sb;
			while ((s = reader.readLine()) != null) {
				int id = getInteger(s);
				if ((def = ItemDefinition.forId(id)) == null) {
					return;
				} else if (s.startsWith("Tradeable")) {
					sb = new StringBuilder("Tradeable").append(id).append(": ");
					def.setTradeable(Boolean.parseBoolean(s.replace(sb.toString(), "")));
				} else if (s.startsWith("High-alch")) {
					sb = new StringBuilder("High-alch").append(id).append(": ");
					def.setHighAlchPrice(Integer.parseInt(s.replace(sb.toString(), "")));
				} else if (s.startsWith("Low-alch")) {
					sb = new StringBuilder("Low-alch").append(id).append(": ");
					def.setLowAlchPrice(Integer.parseInt(s.replace(sb.toString(), "")));
				} else if (s.startsWith("Dropable")) {
					sb = new StringBuilder("Dropable").append(id).append(": ");
					def.setDropable(Boolean.parseBoolean(s.replace(sb.toString(), "")));
				} else if (s.startsWith("StorePrice")) {
					sb = new StringBuilder("StorePrice").append(id).append(": ");
					def.setStorePrice(Integer.parseInt(s.replace(sb.toString(), "")));
				} else if (s.startsWith("EquipmentSlot")) {
					sb = new StringBuilder("EquipmentSlot").append(id).append(": ");
					def.setEquipmentSlot(Integer.parseInt(s.replace(sb.toString(), "")));
				} else if (s.startsWith("TwoHanded")) {
					sb = new StringBuilder("TwoHanded").append(id).append(": ");
					def.setTwoHanded(Boolean.parseBoolean(s.replace(sb.toString(), "")));
				} else if (s.startsWith("Examine")) {
					sb = new StringBuilder("Examine").append(id).append(": ");
					def.setExamine(s.replace(sb.toString(), ""));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets an integer in the string.
	 * @param s The string.
	 * @return The integer.
	 */
	private static int getInteger(String s) {
		StringBuilder sb = new StringBuilder();
		char c;
		boolean foundStart = false;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				sb.append(c);
				foundStart = true;
			} else if (foundStart) {
				break;
			}
		}
		int amount = Integer.parseInt(sb.toString());
		return amount;
	}

	/**
	 * Saves all the definitions.
	 */
	private static void saveDefinitions() {
		File f = new File("./data/item/itemDefinitions.bin");
		f.delete();
		try {
			RandomAccessFile raf = new RandomAccessFile("./data/item/itemDefinitions.bin", "rw");
			raf.writeShort(ItemDefinition.MAX_SIZE);
			for (int x = 0; x < ItemDefinition.MAX_SIZE; x++) {
				currentAmount++;
				//progress.updateStatus(currentAmount);
				if (currentAmount % 500 == 0) {
					System.out.println(currentAmount);
				}
				ItemDefinition itemDef = ItemDefinition.forId(x);
				if (itemDef == null) {
					raf.writeShort(-1);
					continue;
				}
				raf.writeShort(x);
				raf.writeShort(itemDef.getEquipId());
				raf.writeShort(itemDef.getRenderId());
				raf.writeBytes(itemDef.getName());
				raf.writeByte(0);
				raf.writeByte(itemDef.isNoted() ? 0 : 1);
				raf.writeBytes(itemDef.isNoted() ? "Swap this note at any bank for the equivalent item." : itemDef.getExamine() != null ? itemDef.getExamine() : "It's a " + itemDef.getName());
				raf.writeByte(0);
				if (!itemDef.isNoted()) {
					raf.writeDouble(itemDef.getWeight());
					raf.writeByte(itemDef.getEquipmentSlot());
					raf.writeByte(itemDef.isTradeable() ? 1 : 0);
					raf.writeByte(itemDef.getAttackSpeed());
					raf.writeByte(itemDef.getBonus() == null ? 0 : 1);
					if (itemDef.getBonus() != null) {
						for (int i = 0; i < 15; i++) {
							raf.writeShort(itemDef.getBonus()[i]);
						}
					}
					raf.writeByte(itemDef.getAbsorptionBonus() == null ? 0 : 1);
					if (itemDef.getAbsorptionBonus() != null) {
						for (int i = 0; i < 3; i++) {
							raf.writeShort(itemDef.getAbsorptionBonus()[i]);
						}
					}
				}
				raf.writeInt(itemDef.getHighAlchPrice());
				raf.writeInt(itemDef.getLowAlchPrice());
				raf.writeInt(itemDef.getStorePrice() < 1 ? (int) (itemDef.getHighAlchPrice() * 1.2) : itemDef.getStorePrice());
				raf.writeInt(itemDef.getExchangePrice());
				raf.writeByte(itemDef.isStackable() ? 1 : 0);
				//raf.writeByte(itemDef.isNoted() ? 1 : 0);
				raf.writeByte(itemDef.getSkillRequirementId() != null ? 1 : 0);
				if (itemDef.getSkillRequirementId() != null) {
					raf.writeByte(itemDef.getSkillRequirementId().size());
					for (int skillIndex = 0; skillIndex < itemDef.getSkillRequirementId().size(); skillIndex++) {
						raf.writeByte(itemDef.getSkillRequirementId().get(skillIndex));
						if (itemDef.getSkillRequirementLvl().size() > skillIndex) {
							raf.writeByte(itemDef.getSkillRequirementLvl().get(skillIndex));
						} else {
							raf.writeByte(1);
						}
					}
				}
				raf.writeByte(itemDef.isDropable() ? 1 : 0);
				raf.writeByte(itemDef.isTwoHanded() ? 1 : 0);
			}
			raf.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}