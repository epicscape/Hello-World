package org.dementhium.content.misc;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

import java.util.HashMap;
import java.util.Map;

/**
 * An action for eating food and drinking
 *
 * @author Linux
 * @author 'Mystic Flow
 * @author Wolfey
 */
public class Eating extends EventListener {

    /**
     * Represents types of bones.
     *
     * @author Linux
     * @author Wolfey
     */
    public static enum Food {
        ANCHOVIE(319, 1),
        SHRIMP(315, 3),
        CHICKEN(2140, 3),
        MEAT(2142, 3),
        SARDINE(325, 4),
        CAKE(1891, 4, 1893),
        BREAD(2309, 5),
        HERRING(347, 5),
        FRIED_MUSHROOM(7082, 5),
        TROUT(333, 7),
        COD(339, 7),
        PIKE(351, 8),
        SALMON(329, 9),
        TUNA(361, 10),
        LOBSTER(379, 12),
        BASS(365, 13),
        SWORDFISH(373, 14),
        MONKFISH(7946, 16),
        SHARK(385, 20),
        TURTLE(397, 21),
        MANTA(391, 22),
        ROCKTAIL(15272, 23),
	PINEAPPLE_PIZZA(2301, 11),
	PINEAPPLE_PIZZA_HALF(2303, 11),
	MEAT_PIZZA(2293, 8),
	MEAT_PIZZA_HALF(2295, 8),
	PLAIN_PIZZA(2289, 7),
	PLAIN_PIZZA_HALF(2281, 7),
	ANCHOVY_PIZZA(2297, 9),
	ANCHOVY_PIZZA_HALF(2299, 9),
	STEW(2003, 11),
	CURRY(2011, 19),
	TUNA_POTATO(7060, 22),
	MUSHROOM_POTATO(7058, 20),
	EGG_POTATO(7056, 16),
	POTATO_WITH_CHEESE(6705, 16),
	CHILLI_POTATO(7054, 14),
	POTATO_WITH_BUTTER(6703, 14),
	BAKED_POTATO(6701, 40), 
        SUMMER_PIE_FULL(7218, 11, 7220, Effect.SUMMER_PIE),
        SUMMER_PIE_HALF(7220, 11, 2313, Effect.SUMMER_PIE);

        /**
         * The food id
         */
        private int id;

        /**
         * The healing health
         */
        private int heal;

        /**
         * The new food id if needed
         */
        private int newId;

        /**
         * Our effect
         */
        private Effect effect;

        /**
         * A map of object ids to foods.
         */
        private static Map<Integer, Food> foods = new HashMap<Integer, Food>();

        /**
         * Gets a food by an object id.
         *
         * @param object The object id.
         * @return The food, or <code>null</code> if the object is not a food.
         */
        public static Food forId(int object) {
            return foods.get(object);
        }

        /**
         * Populates the tree map.
         */
        static {
            for (final Food food : Food.values()) {
                foods.put(food.id, food);
            }
        }

        /**
         * Represents a food being eaten
         *
         * @param id     The food id
         * @param health The healing health received
         */
        private Food(int id, int heal) {
            this.id = id;
            this.heal = heal;
        }

        /**
         * Represents a part of a food item being eaten (example: cake)
         *
         * @param id    The food id
         * @param heal  The heal amount
         * @param newId The new food id
         */
        private Food(int id, int heal, int newId) {
            this.id = id;
            this.heal = heal;
            this.newId = newId;
        }

        private Food(int id, int heal, int newId, Effect effect) {
            this.id = id;
            this.heal = heal;
            this.newId = newId;
            this.effect = effect;
        }

        /**
         * Gets the id.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the exp amount.
         *
         * @return The exp amount.
         */
        public int getHeal() {
            return heal;
        }

        /**
         * Gets the new food id
         *
         * @return The new food id.
         */
        public int getNewId() {
            return newId;
        }
    }

    public static enum Effect {
        SUMMER_PIE {
            public void effect(Player player) {

            }

        };

        public void effect(Player player) {

        }
    }

    private static final int EAT_ANIM = 829;

    public boolean interfaceOption(final Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        if (opcode != 6) {
            return false;
        }
        if (player.isDead()) {
            return false;
        }
        if (player.getInventory().get(slot) != null && itemId != player.getInventory().get(slot).getId()) {
            return false;
        }
        if (player.getInventory().get(slot) == null) {
        	return false;
        }
        Food food = Food.forId(itemId);
        if (food != null) {
            if (player.getActivity() instanceof DuelActivity) {
                if (((DuelActivity) player.getActivity()).getDuelConfigurations().getRule(Rules.FOOD)) {
                    player.sendMessage("You aren't allowed to eat during this duel!");
                    return true;
                }
            }
            if (player.getAttribute("consumed") != Boolean.TRUE) {
                player.setAttribute("consumed", Boolean.TRUE);
                ActionSender.sendChatMessage(player, 0, "You eat the " + ItemDefinition.forId(food.getId()).getName().toLowerCase());
                player.animate(EAT_ANIM);
                player.getCombatExecutor().setTicks(player.getCombatExecutor().getTicks() + 3, false);
                if (food.getNewId() == 0) {
                    player.getInventory().getContainer().set(slot, null);
                } else {
                    player.getInventory().getContainer().set(slot, null);
                    player.getInventory().getContainer().set(slot, new Item(food.getNewId()));
                }
                if (food.getId() == 15272) {
                    player.getSkills().healRocktail(food.getHeal() * 10);
                } else {
                    player.getSkills().heal(food.getHeal() * 10);
                }
                player.getInventory().refresh();
                if (food.effect != null) {
                    food.effect.effect(player);
                }
                if (food.getId() == 7218 || food.getId() == 7220) {
                    World.getWorld().submit(new Tick(1) {
                        @Override
                        public void execute() {
                            player.removeAttribute("consumed");
                            this.stop();
                        }
                    });
                } else {
                    World.getWorld().submit(new Tick(3) {
                        @Override
                        public void execute() {
                            player.removeAttribute("consumed");
                            this.stop();
                        }
                    });
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(149, this);
    }
}