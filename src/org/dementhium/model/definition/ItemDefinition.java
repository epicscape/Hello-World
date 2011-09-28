package org.dementhium.model.definition;

import org.dementhium.cache.format.CacheItemDefinition;
import org.dementhium.model.combat.Ammunition;
import org.dementhium.model.combat.RangeWeapon;
import org.dementhium.util.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class ItemDefinition {

    public static int MAX_SIZE = 22430;
    private static ItemDefinition[] definitions;

    public static void init() throws IOException {
        System.out.println("Loading item definitions...");
        FileChannel channel = new RandomAccessFile("data/item/itemDefinitions.bin", "r").getChannel();

        ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());

        int length = buffer.getShort();

        definitions = new ItemDefinition[MAX_SIZE];

        for (int i = 0; i < length; i++) {
            int id = buffer.getShort();
            if (id == -1) {
                continue;
            }
            int equipId = buffer.getShort();
            int renderEmote = buffer.getShort();
            String name = BufferUtils.readRS2String(buffer);
            boolean noted = buffer.get() == 0;
            int[] bonus = new int[15];
            int[] absorptionBonus = new int[3];
            boolean tradeable = true;
            int attackSpeed = 5;
            int equipmentSlot = -1;
            String examine = BufferUtils.readRS2String(buffer);
            double weight = 0;
            int highAlchPrice = 0, lowAlchPrice = 0, storePrice = 0, exchangePrice = 0;
            if (!noted) {
                weight = buffer.getDouble();
                equipmentSlot = buffer.get();
                tradeable = buffer.get() == 1;
                attackSpeed = buffer.get();
                if (buffer.get() == 1) {
                    for (int x = 0; x < 15; x++) {
                        bonus[x] = buffer.getShort();
                    }
                }
                if (buffer.get() == 1) {
                    for (int x = 0; x < 3; x++) {
                        absorptionBonus[x] = buffer.getShort();
                    }
                }
            }
            highAlchPrice = buffer.getInt();
            lowAlchPrice = buffer.getInt();
            storePrice = buffer.getInt();
            exchangePrice = buffer.getInt();
            boolean stackable = buffer.get() == 1;
            boolean hasReq = buffer.get() == 1;
            ArrayList<Integer> skillRequirementId = null;
            ArrayList<Integer> skillRequirementLvl = null;
            if (hasReq) {
                skillRequirementId = new ArrayList<Integer>();
                skillRequirementLvl = new ArrayList<Integer>();
                int size = buffer.get();
                for (int j = 0; j < size; j++) {
                    skillRequirementId.add((int) buffer.get());
                    skillRequirementLvl.add((int) buffer.get());
                }
            }
            boolean dropable = buffer.get() == 1;
            boolean twoHanded = buffer.get() == 1;
            if (id == 15220) {
                bonus[11] = 8;
            }
            if (id == 18819) {
                for (int ii = 0; ii < 15; ii++) {
                    if (ii == 11) continue;
                    bonus[ii] = 0;
                }
                bonus[11] = 0;
            }
            definitions[id] = new ItemDefinition(id, name, examine, equipId, renderEmote, bonus, stackable, noted, tradeable, skillRequirementId, skillRequirementLvl, weight, highAlchPrice, lowAlchPrice, storePrice, exchangePrice, attackSpeed, equipmentSlot, absorptionBonus, dropable, twoHanded);
           // definitions[id].setExtraDefinitions(extraDefinition);
        }
        loadMiscData();
        System.out.println("Loaded " + definitions.length + " item definitions.");
        channel.close();
        channel = null;
        RangeWeapon.initialize();
        Ammunition.initialize();
    }

    private static void loadMiscData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("data/item/poisoningitems.txt"));
        String string;
        while ((string = reader.readLine()) != null) {
            String[] data = string.split(":");
            int id = Integer.parseInt(data[0]);
            int amount = Integer.parseInt(data[1]);
            definitions[id].setPoisonAmount(amount);
        }
        reader = new BufferedReader(new FileReader("data/item/equipIds.txt"));
        while ((string = reader.readLine()) != null) {
            String[] data = string.split(":");
            int id = Integer.parseInt(data[0]);
            int equipId = Integer.parseInt(data[1]);
            definitions[id].setEquipId(equipId);
        }
    }


    public static ItemDefinition forId(int id) {
        if (id == -1)
            return null;
        ItemDefinition def = definitions[id];
        if (def == null) {
            definitions[id] = new ItemDefinition(id, "", "", -1, -1, new int[15], false, false, true, null, null, 0.0, 0, 0, 0, 0, 0, 0, null, true, false);
        }
        return definitions[id];
    }

    public static ItemDefinition forName(String name) {
        for (ItemDefinition definition : definitions) {
            if (definition.name.equalsIgnoreCase(name)) {
                return definition;
            }
        }
        return null;
    }

    public static void clear() {
        definitions = new ItemDefinition[MAX_SIZE];
    }

    private boolean dropable;
    private boolean twoHanded;
    private String name;
    private String examine;
    private int id;
    private int equipId;
    private int renderId;
    private int[] bonus;
    private boolean stackable;
    private boolean noted;
    private boolean tradeable;
    private List<Integer> skillRequirementId;
    private List<Integer> skillRequirementLvl;
    private double weight;
    private boolean members;
    private int highAlchPrice, lowAlchPrice, storePrice, exchangePrice, attackSpeed, equipmentSlot;
    private int poisonAmount;

    private boolean extraDefinitions;
    private CacheItemDefinition cacheDefinition;
    private int[] absorptionBonus;

    public ItemDefinition(int id, String name, String examine, int equipId, int renderId, int[] bonus, boolean stackable, boolean noted, boolean tradeable, List<Integer> skillRequirementId, List<Integer> skillRequirementLvl, double weight, int highAlchPrice, int lowAlchPrice, int storePrice, int exchangePrice, int attackSpeed, int equipmentSlot, int[] absorptionBonus, boolean dropable, boolean twoHanded) {
        this.id = id;
        this.name = name;
        if (examine.length() == 0 || examine == null) {
            if (name.length() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("It's a");
                char c = name.charAt(0);
                if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                    sb.append("n");
                }
                sb.append(" ");
                sb.append(name);
                this.examine = sb.toString();
            }
        } else {
            this.examine = examine;
        }
        this.twoHanded = twoHanded;
        this.dropable = dropable;
        this.equipId = equipId;
        this.renderId = renderId;
        this.bonus = bonus;
        this.stackable = stackable;
        this.noted = noted;
        this.tradeable = tradeable;
        this.skillRequirementId = skillRequirementId;
        this.skillRequirementLvl = skillRequirementLvl;
        this.weight = weight;
        this.highAlchPrice = highAlchPrice;
        this.lowAlchPrice = lowAlchPrice;
        this.storePrice = storePrice;
        this.exchangePrice = exchangePrice;
        this.attackSpeed = attackSpeed;
        this.equipmentSlot = equipmentSlot;
        this.absorptionBonus = absorptionBonus;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getEquipId() {
        return equipId;
    }

    public int getRenderId() {
        return renderId;
    }

    public int[] getBonus() {
        return bonus;
    }

    public boolean isStackable() {
        return stackable || noted;
    }

    public boolean isNoted() {
        return noted;
    }

    public List<Integer> getSkillRequirementId() {
        return skillRequirementId;
    }

    public List<Integer> getSkillRequirementLvl() {
        return skillRequirementLvl;
    }

    public String getExamine() {
        return examine;
    }

    public boolean isTradeable() {
        return tradeable;
    }

    public double getWeight() {
        return weight;
    }

    public int getHighAlchPrice() {
        return highAlchPrice;
    }

    public int getLowAlchPrice() {
        return lowAlchPrice;
    }

    public int getStorePrice() {
        return storePrice;
    }

    public int getExchangePrice() {
        if (exchangePrice < 1 && storePrice < 1)
            return getCacheDefinition().getValue();
        return exchangePrice == 0 ? storePrice : exchangePrice;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getEquipmentSlot() {
        return equipmentSlot;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEquipId(int equipId) {
        this.equipId = equipId;
    }

    public void setRenderId(int renderId) {
        this.renderId = renderId;
    }

    public void setBonus(int[] bonus) {
        this.bonus = bonus;
    }

    public void setBonusAtIndex(int index, int value) {
        this.bonus[index] = value;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public void setNoted(boolean noted) {
        this.noted = noted;
    }

    public void setTradeable(boolean tradeable) {
        this.tradeable = tradeable;
    }

    public void setSkillRequirementId(List<Integer> skillRequirementId) {
        this.skillRequirementId = skillRequirementId;
    }

    public void setSkillRequirementLvl(List<Integer> skillRequirementLvl) {
        this.skillRequirementLvl = skillRequirementLvl;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHighAlchPrice(int highAlchPrice) {
        this.highAlchPrice = highAlchPrice;
    }

    public void setLowAlchPrice(int lowAlchPrice) {
        this.lowAlchPrice = lowAlchPrice;
    }

    public void setStorePrice(int storePrice) {
        this.storePrice = storePrice;
    }

    public void setExchangePrice(int exchangePrice) {
        this.exchangePrice = exchangePrice;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setEquipmentSlot(int equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
    }

    public void setMembers(boolean members) {
        this.members = members;
    }

    public boolean isMembers() {
        return members;
    }

    public void setExtraDefinitions(boolean extraDefinition) {
        this.extraDefinitions = extraDefinition;
    }

    public boolean isExtraDefinitions() {
        return extraDefinitions;
    }

    public CacheItemDefinition getCacheDefinition() {
        if (cacheDefinition == null) {
            cacheDefinition = CacheItemDefinition.getItemDefinition(id);
        }
        return cacheDefinition;
    }

    public static ItemDefinition[] getDefinitions() {
        return definitions;
    }

    public void setAbsorptionBonus(int[] absorptionBonus) {
        this.absorptionBonus = absorptionBonus;
    }

    public int[] getAbsorptionBonus() {
        return absorptionBonus;
    }

    private void setPoisonAmount(int amount) {
        this.poisonAmount = amount;

    }

    public boolean doesPoison() {
        return poisonAmount > 0;
    }

    public int getPoisonAmount() {
        return poisonAmount;
    }

	/**
	 * @return the dropable
	 */
	public boolean isDropable() {
		return dropable;
	}

	/**
	 * @param dropable the dropable to set
	 */
	public void setDropable(boolean dropable) {
		this.dropable = dropable;
	}

	/**
	 * @return the twoHanded
	 */
	public boolean isTwoHanded() {
		return twoHanded;
	}

	/**
	 * @param twoHanded the twoHanded to set
	 */
	public void setTwoHanded(boolean twoHanded) {
		this.twoHanded = twoHanded;
	}

}
