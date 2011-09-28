package org.dementhium.content.skills.herblore;

import org.dementhium.action.ProduceAction;
import org.dementhium.content.skills.SkillAction;
import org.dementhium.model.Item;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;

/**
 * Handles the {@code Herblore} skill.
 *
 * @author Emperor
 */
public class Herblore extends SkillAction {

    /**
     * The vial of water item id.
     */
    public static final short VIAL = 227;

    /**
     * The cup of hot water item id.
     */
    public static final short CUP_OF_HOT_WATER = 4460;

    /**
     * The coconut milk item id.
     */
    public static final short COCONUT_MILK = 5935;

    /**
     * The pestle and mortar item id.
     */
    public static final short PESTLE_AND_MORTAR = 233;

    /**
     * The swamp tar item id.
     */
    public static final short SWAMP_TAR = 1939;

    /**
     * The combining ingredients animation.
     */
    private static final Animation MAKE_POTION = Animation.create(363);

    /**
     * The crushing an item with pestle and mortar animation.
     */
    private static final Animation CRUSH_ITEM = Animation.create(364);

    /**
     * The first item.
     */
    private Item node;

    /**
     * The other item.
     */
    private Item otherItem;

    /**
     * The ingredient used.
     */
    private Ingredients ingredients;

    /**
     * The ingredient to crush.
     */
    private RawIngredient rawIngredient;

    /**
     * The amount of
     */
    private int amount;

    /**
     * The item slot.
     */
    private byte slot;

    /**
     * Constructs a new {@code Herblore} {@code Object}.
     *
     * @param player The player
     * @param node   The herb to clean.
     */
    public Herblore(Player player, Item node, byte slot) {
        super(player);
        this.node = node;
        this.slot = slot;
    }

    /**
     * Constructs a new {@code Herblore} {@code Object}.
     *
     * @param player    The player
     * @param node      The herb or ingredient.
     * @param otherNode The vial item.
     */
    public Herblore(Player player, Item node, Item otherNode, int amount) {
        super(player);
        this.node = node;
        this.otherItem = otherNode;
        this.amount = amount;
        if (node.getId() != PESTLE_AND_MORTAR && this.amount > player.getInventory().numberOf(node.getId())) {
            this.amount = player.getInventory().numberOf(node.getId());
        }
        if (otherNode.getId() != PESTLE_AND_MORTAR && this.amount > player.getInventory().numberOf(otherNode.getId())) {
            this.amount = player.getInventory().numberOf(otherNode.getId());
        }
        if (node.getId() == PESTLE_AND_MORTAR || otherNode.getId() == PESTLE_AND_MORTAR) {
            this.rawIngredient = RawIngredient.forId(node.getId());
            if (rawIngredient == null) {
                rawIngredient = RawIngredient.forId(otherItem.getId());
                this.node = otherNode;
                this.otherItem = node;
            }
            if (rawIngredient == RawIngredient.MUD_RUNES) {
                if (this.amount > player.getInventory().getFreeSlots()) {
                    this.amount = player.getInventory().getFreeSlots();
                }
            }
        } else {
            this.ingredients = Ingredients.forId(node.getId());
            if (ingredients == null) {
                ingredients = Ingredients.forId(otherItem.getId());
                this.node = otherNode;
                this.otherItem = node;
            }
        }
    }

    @Override
    public boolean commence(Player player) {
        if (getPlayer() == null || node == null) {
            return false;
        }
        Herb herb = Herb.forId(node.getId());
        if (herb != null) {
            return cleanHerb(herb);
        }
        if ((ingredients == null && rawIngredient == null) || otherItem == null) {
            return false;
        }
        if (ingredients != null) {
            this.slot = ingredients.getSlot(otherItem.getId());
            if (getPlayer().getSkills().getLevel(Skills.HERBLORE) < ingredients.getLevels()[slot]) {
                getPlayer().sendMessage("You need a herblore level of " + ingredients.getLevels()[slot] + " to combine these ingredients.");
                return false;
            }
            if (ingredients == Ingredients.TORSTOL && otherItem.getId() != VIAL) {
                if (this.amount > player.getInventory().numberOf(15309)) {
                    this.amount = player.getInventory().numberOf(15309);
                }
                if (this.amount > player.getInventory().numberOf(15313)) {
                    this.amount = player.getInventory().numberOf(15313);
                }
                if (this.amount > player.getInventory().numberOf(15317)) {
                    this.amount = player.getInventory().numberOf(15317);
                }
                if (this.amount > player.getInventory().numberOf(15321)) {
                    this.amount = player.getInventory().numberOf(15321);
                }
                if (this.amount > player.getInventory().numberOf(15325)) {
                    this.amount = player.getInventory().numberOf(15325);
                }
                return amount > 0;
            }
            return true;
        }
        return true;
    }

    /**
     * Sends the chatbox skill interface.
     *
     * @param player The player.
     * @param itemId The item id to produce.
     * @param first  The first item.
     * @param second The other item.
     */
    public static void sendInterface(Player player, int itemId, Item first, Item second) {
        ActionSender.sendBConfig(player, 754, 13);
        ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[0], itemId);
        ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[0], new Item(itemId).getDefinition().getName());
        player.getSettings().setPossibleProductions(new int[]{itemId});
        ActionSender.sendString(player, 916, 1, "Choose how many you wish to make,<br> then click the item to begin.");
        ActionSender.sendInterface(player, 1, 752, 13, 905);
        ActionSender.sendInterface(player, 1, 905, 4, 916);
        int amount;
        if (first.getId() == PESTLE_AND_MORTAR) {
            amount = player.getInventory().numberOf(second.getId());
        } else if (second.getId() == PESTLE_AND_MORTAR) {
            amount = player.getInventory().numberOf(first.getId());
        } else {
            amount = player.getInventory().numberOf(first.getId());
            if (amount > player.getInventory().numberOf(second.getId())) {
                amount = player.getInventory().numberOf(second.getId());
            }
        }
        ActionSender.sendConfig(player, 1363, (amount << 20) | (amount << 26));
        player.getSettings().setDialougeSkill(Skills.HERBLORE);
        player.getSettings().setAmountToProduce(amount);
        player.getSettings().setMaxToProduce(amount);
    }

    /**
     * Cleans a grimy herb.
     *
     * @param herb The herb.
     * @return {@code False}.
     */
    private boolean cleanHerb(Herb herb) {
        if (getPlayer().getSkills().getLevel(Skills.HERBLORE) < herb.getLevel()) {
            getPlayer().sendMessage("You need a herblore level of " + herb.getLevel() + " to clean this herb.");
        } else if (getPlayer().getInventory().contains(node.getId(), node.getAmount())) {
            getPlayer().getInventory().set(slot, herb.getReward());
            getPlayer().getInventory().refresh();
            getPlayer().sendMessage("You clean the dirt from the " + node.getDefinition().getName().toLowerCase().replace("grimy ", "") + ".");
            getPlayer().getSkills().addExperience(Skills.HERBLORE, herb.getExperience());
        }
        return false;
    }

    @Override
    public boolean execute(Player player) {
        if (ingredients == Ingredients.TORSTOL && otherItem.getId() != VIAL) {
            if (!getPlayer().getInventory().contains(15309) || !getPlayer().getInventory().contains(15313) || !getPlayer().getInventory().contains(15317) || !getPlayer().getInventory().contains(15321) || !getPlayer().getInventory().contains(15325)) {
                stop();
                return false;
            }
        } else if (!getPlayer().getInventory().contains(node.getId()) || !getPlayer().getInventory().contains(otherItem.getId())) {
            stop();
            return false;
        }
        if (node.getId() == PESTLE_AND_MORTAR || otherItem.getId() == PESTLE_AND_MORTAR) {
            getPlayer().animate(CRUSH_ITEM);
        } else {
            getPlayer().animate(MAKE_POTION);
        }
        return true;
    }

    @Override
    public boolean finish(Player player) {
        amount--;
        if (ingredients == Ingredients.TORSTOL && otherItem.getId() != VIAL) {
            getPlayer().sendMessage("You combine the torstol with the potions and get an overload.");
            if (getPlayer().getInventory().removeItems(new Item(node.getId()), new Item(15309), new Item(15313), new Item(15317), new Item(15321), new Item(15325))) {
                getPlayer().getInventory().addItem(new Item(ingredients.getRewards()[slot], 1));
                getPlayer().getSkills().addExperience(Skills.HERBLORE, ingredients.getExperience()[slot]);
            }
            return amount < 1;
        }
        if (otherItem.getId() == VIAL || otherItem.getId() == COCONUT_MILK) {
            getPlayer().sendMessage("You add the " + node.getDefinition().getName().toLowerCase().replace("clean", "") + " into the vial of " + (otherItem.getId() == VIAL ? "water." : "milk."));
        } else if (otherItem.getId() == SWAMP_TAR) {
            getPlayer().sendMessage("You add the " + node.getDefinition().getName().toLowerCase().replace("clean ", "") + " on the swamp tar.");
        } else if (otherItem.getId() == PESTLE_AND_MORTAR) {
            getPlayer().sendMessage("You crush the " + node.getDefinition().getName().toLowerCase() + " with your pestle and mortar.");
        } else {
            getPlayer().sendMessage("You mix the " + node.getDefinition().getName().toLowerCase() + " into your potion.");
        }
        if (getPlayer().getInventory().removeItems(new Item(node.getId(), 1), rawIngredient == null ? new Item(otherItem.getId(), 1) : null)) {
            getPlayer().getInventory().addItem(rawIngredient != null ? rawIngredient.getCrushedItem() : new Item(ingredients.getRewards()[slot], 1));
            getPlayer().getSkills().addExperience(Skills.HERBLORE, (rawIngredient != null ? 0 : ingredients.getExperience()[slot]));
        }
        return amount < 1;
    }

    /**
     * Checks if the item on item action performed is for the herblore skill.
     *
     * @param first The item used.
     * @param other The other item.
     * @return {@code True} if so, {@code false} if not.
     */
    public static int isHerbloreSkill(Item first, Item other) {
        Item swap = first;
        Ingredients ingredient = Ingredients.forId(first.getId());
        if (ingredient == null) {
            ingredient = Ingredients.forId(other.getId());
            first = other;
            other = swap;
        }
        if (ingredient != null) {
            int slot = ingredient.getSlot(other.getId());
            return slot > -1 ? ingredient.getRewards()[slot] : -1;
        }
        swap = first;
        RawIngredient raw = RawIngredient.forId(first.getId());
        if (raw == null) {
            raw = RawIngredient.forId(other.getId());
            first = other;
            other = swap;
        }
        if (raw != null) {
            return other.getId() == PESTLE_AND_MORTAR ? raw.getCrushedItem().getId() : -1;
        }
        return -1;
    }
}
