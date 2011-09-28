package org.dementhium.content.misc;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author RS2-Server team
 */
public class Drinking extends EventListener {

    /**
     * All drinkable items.
     * NOTE: For item IDs, you must go potion dose 1, dose 2, dose 3, dose 4. EG: Attack_Potion(1)[125][2428], Attack_Potion(2), Attack_Potion(3)[121][123], Attack_Potion(4)
     *
     * @author Michael Bull
     */
    public static enum Drink {

        ATTACK_POTION(new int[]{125, 123, 121, 2428}, new int[]{Skills.ATTACK}, PotionType.NORMAL_POTION),

        STRENGTH_POTION(new int[]{119, 117, 115, 113}, new int[]{Skills.STRENGTH}, PotionType.NORMAL_POTION),

        DEFENCE_POTION(new int[]{137, 135, 133, 2432}, new int[]{Skills.DEFENCE}, PotionType.NORMAL_POTION),

        RANGE_POTION(new int[]{173, 171, 169, 2444}, new int[]{Skills.RANGE}, PotionType.NORMAL_POTION),

        MAGIC_POTION(new int[]{3046, 3044, 3042, 3040}, new int[]{Skills.MAGIC}, PotionType.PLUS_5),

        RESTORE_POTION(new int[]{131, 129, 127, 2430}, new int[]{Skills.DEFENCE, Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE}, PotionType.RESTORE),

        SUPER_RESTORE_POTION(new int[]{3030, 3028, 3026, 3024}, new int[]{Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.MAGIC, Skills.RANGE, Skills.PRAYER, Skills.AGILITY, Skills.COOKING, Skills.CRAFTING, Skills.FARMING, Skills.FIREMAKING, Skills.FISHING, Skills.FLETCHING, Skills.HERBLORE, Skills.MINING, Skills.RUNECRAFTING, Skills.SLAYER, Skills.SMITHING, Skills.THIEVING, Skills.WOODCUTTING, Skills.SUMMONING}, PotionType.SUPER_RESTORE),

        PRAYER_POTION(new int[]{143, 141, 139, 2434}, new int[]{Skills.PRAYER}, PotionType.PRAYER_POTION),

        SUPER_ATTACK_POTION(new int[]{149, 147, 145, 2436}, new int[]{Skills.ATTACK}, PotionType.SUPER_POTION),

        SUPER_STRENGTH_POTION(new int[]{161, 159, 157, 2440}, new int[]{Skills.STRENGTH}, PotionType.SUPER_POTION),

        SUPER_DEFENCE_POTION(new int[]{167, 165, 163, 2442}, new int[]{Skills.DEFENCE}, PotionType.SUPER_POTION),

        SARADOMIN_BREW(new int[]{6691, 6689, 6687, 6685}, new int[]{Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE, Skills.HITPOINTS}, PotionType.SARADOMIN_BREW),

        ZAMORAK_BREW(new int[]{193, 191, 189, 2450}, new int[]{Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.HITPOINTS, Skills.PRAYER}, PotionType.ZAMORAK_BREW),

        ANTIPOISON(new int[]{179, 177, 175, 2446}, new int[]{}, PotionType.ANTIPOISON),

        SUPER_ANTIPOISON(new int[]{185, 183, 181, 2448}, new int[]{}, PotionType.SUPER_ANTIPOISON),

        SUMMONING_POTION(new int[] { 12146, 12144, 12142, 12140}, new int[] { Skills.SUMMONING }, PotionType.PRAYER_POTION),
        
        BEER(new int[]{1919, 1917}, new int[]{Skills.ATTACK, Skills.STRENGTH}, PotionType.BEER),

        JUG(new int[]{1935, 1993}, new int[]{Skills.ATTACK, Skills.HITPOINTS}, PotionType.WINE),

        OVERLOAD(new int[]{15335, 15334, 15333, 15332}, new int[]{Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE, Skills.MAGIC}, PotionType.OVERLOAD),

        RESTORE_SPECIAL(new int[]{15303, 15302, 15301, 15300}, new int[]{}, PotionType.RESTORE_SPECIAL),

        EXTREME_ATTACK(new int[]{15311, 15310, 15309, 15308}, new int[]{Skills.ATTACK}, PotionType.EXTREME),

        EXTREME_STRENGTH(new int[]{15315, 15314, 15313, 15312}, new int[]{Skills.STRENGTH}, PotionType.EXTREME),

        EXTREME_DEFENCE(new int[]{15319, 15318, 15317, 15316}, new int[]{Skills.DEFENCE}, PotionType.EXTREME),

        EXTREME_MAGIC(new int[]{15323, 15322, 15321, 15320}, new int[]{Skills.MAGIC}, PotionType.EXTREME),

        EXTREME_RANGING(new int[]{15327, 15326, 15325, 15324}, new int[]{Skills.RANGE}, PotionType.EXTREME),

        SUPER_PRAYER(new int[]{15331, 15330, 15329, 15328}, new int[]{Skills.PRAYER}, PotionType.PRAYER_POTION),

        ANTIFIRE(new int[]{2458, 2456, 2454, 2452}, new int[]{}, PotionType.ANTIFIRE_POTION),

        SUPER_ANTIFIRE(new int[]{15307, 15306, 15305, 15304}, new int[]{}, PotionType.ANTIFIRE_POTION),;

        /**
         * A map of drink Ids.
         */
        private static Map<Integer, Drink> drinks = new HashMap<Integer, Drink>();

        /**
         * Gets a drink by its ID.
         *
         * @param drink The drink id.
         * @return The Drink, or <code>null</code> if the id is not a drink.
         */
        public static Drink forId(int drink) {
            return drinks.get(drink);
        }

        /**
         * Populates the drink map.
         */
        static {
            for (Drink drink : Drink.values()) {
                for (int i = 0; i < drink.id.length; i++) {
                    drinks.put(drink.id[i], drink);
                }
            }
        }

        /**
         * The drink item id.
         */
        private int[] id;

        /**
         * The skill to boost.
         */
        private int skill[];

        /**
         * The potion type.
         */
        private PotionType potionType;

        /**
         * Creates the drink.
         *
         * @param id The drink item id.
         */
        private Drink(int id[], int[] skill, PotionType potionType) {
            this.id = id;
            this.skill = skill;
            this.potionType = potionType;
        }

        /**
         * Gets the drink item id.
         *
         * @return The drink item id.
         */
        public int getId(int index) {
            return id[index];
        }

        /**
         * Gets the drink item id.
         *
         * @return The drink item id.
         */
        public int[] getIds() {
            return id;
        }

        /**
         * Gets the boosted skill.
         *
         * @return The boosted skill.
         */
        public int[] getSkills() {
            return skill;
        }

        /**
         * Gets the boosted skill.
         *
         * @return The boosted skill.
         */
        public int getSkill(int index) {
            return skill[index];
        }

        /**
         * Gets the potion type.
         *
         * @return The potion type.
         */
        public PotionType getPotionType() {
            return potionType;
        }
    }

    public static enum PotionType {
        NORMAL_POTION(0),
        SUPER_POTION(1),
        SARADOMIN_BREW(2),
        ZAMORAK_BREW(3),
        PLUS_5(4),
        RESTORE(5),
        SUPER_RESTORE(6),
        PRAYER_POTION(7),
        ANTIPOISON(8),
        SUPER_ANTIPOISON(9),
        BEER(10),
        WINE(11),
        OVERLOAD(12),
        RESTORE_SPECIAL(13),
        EXTREME(14),
        ANTIFIRE_POTION(15),;

        /**
         * A map of PotionType Ids.
         */
        private static Map<Integer, PotionType> potionTypes = new HashMap<Integer, PotionType>();

        /**
         * Gets a PotionType by its ID.
         *
         * @param potionType The PotionType id.
         * @return The PotionType, or <code>null</code> if the id is not a PotionType.
         */
        public static PotionType forId(int potionType) {
            return potionTypes.get(potionType);
        }

        /**
         * Populates the potion type map.
         */
        static {
            for (PotionType potionType : PotionType.values()) {
                potionTypes.put(potionType.id, potionType);
            }
        }

        /**
         * The potion type id.
         */
        private int id;

        /**
         * Creates the potion type.
         *
         * @param id The potion type id.
         */
        private PotionType(int id) {
            this.id = id;
        }

        /**
         * Gets the potion type id.
         *
         * @return The potion type id.
         */
        public int getId() {
            return id;
        }
    }

    private static final Animation DRINKING_ANIMATION = Animation.create(829);

    @SuppressWarnings("all")
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
        final Drink drink = Drink.forId(itemId);
        if (drink != null) {
            if (player.getActivity() instanceof DuelActivity) {
                if (((DuelActivity) player.getActivity()).getDuelConfigurations().getRule(Rules.DRINKS)) {
                    player.sendMessage("You can't drink during this duel!");
                    return true;
                }
            }
            if (player.getAttribute("consumedPotion") == Boolean.TRUE) {
                return true;
            }
            Item item = player.getInventory().get(slot);
            if (item == null) {
                return false;
            }
            if (item.getId() == 229) {
                return true;
            }
            player.setAttribute("consumedPotion", Boolean.TRUE);
            World.getWorld().submit(new Tick(2) {
                @Override
                public void execute() {
                    player.removeAttribute("consumedPotion");
                    this.stop();
                }
            });
            //player.getCombatState().setAttackDelay(player.getCombatState().getAttackDelay() + 2);
            String potionName = item.getDefinition().getName().toLowerCase().substring(0, item.getDefinition().getName().length() - 3).replaceAll(" potion", "");
            switch (drink.getPotionType()) {
                case NORMAL_POTION:
                    ActionSender.sendChatMessage(player, 0, "You drink some of your " + potionName + " potion.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        int modification = (int) Math.floor((drink == Drink.RANGE_POTION ? 4 : 3) + (player.getSkills().getLevelForExperience(skill) * 0.1));
                        player.getSkills().increaseLevelToMaximumModification(skill, modification);
                    }
                    break;
                case SUPER_POTION:
                    ActionSender.sendChatMessage(player, 0, "You drink some of your " + potionName + " potion.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        int modification = (int) Math.floor(5 + (player.getSkills().getLevelForExperience(skill) * 0.15));
                        player.getSkills().increaseLevelToMaximumModification(skill, modification);
                    }
                    break;
                case PRAYER_POTION:
                    ActionSender.sendChatMessage(player, 0, "You drink some of your restore prayer potion.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        int modification = (int) Math.floor(7 + (player.getSkills().getLevelForExperience(skill) * 0.25));
                        /**
                         * Holy wrench increases prayer restoration.
                         */
                        if (skill == Skills.PRAYER) {
                            if (player.getInventory().contains(6714)) {
                                modification++;
                                if (player.getSkills().getLevelForExperience(Skills.PRAYER) >= 40) {
                                    modification++;
                                }
                                if (player.getSkills().getLevelForExperience(Skills.PRAYER) >= 70) {
                                    modification++;
                                }
                            }
                            if (drink == Drink.SUPER_PRAYER) {
                                modification += 10;
                            }
                            player.getSkills().restorePray(modification);
                        } else {
                            player.getSkills().increaseLevelToMaximum(skill, modification);
                        }
                    }
                    break;
                case RESTORE:
                case SUPER_RESTORE:
                    ActionSender.sendChatMessage(player, 0, "You drink some of your " + potionName + " potion.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        int modification = (int) (player.getSkills().getLevelForExperience(skill) / 3);
                        /**
                         * Holy wrench increases prayer restoration.
                         */
                        if (skill == Skills.PRAYER) {
                            if (player.getInventory().contains(6714)) {
                                modification++;
                                if (player.getSkills().getLevelForExperience(Skills.PRAYER) >= 40) {
                                    modification++;
                                }
                                if (player.getSkills().getLevelForExperience(Skills.PRAYER) >= 70) {
                                    modification++;
                                }
                            }
                            player.getSkills().restorePray(modification);
                        } else {
                            player.getSkills().increaseLevelToMaximum(skill, modification);
                        }
                    }
                    break;
                case PLUS_5:
                    ActionSender.sendChatMessage(player, 0, "You drink some of your " + potionName + " potion.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        int modification = 5;
                        player.getSkills().increaseLevelToMaximumModification(skill, modification);
                    }
                    break;
                case SARADOMIN_BREW:
                    ActionSender.sendChatMessage(player, 0, "You drink some of the foul liquid.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        if (skill == Skills.HITPOINTS) {
                            int hitpointsModification = (int) (player.getSkills().getMaxHitpoints() * 0.15);
                            player.getSkills().heal(hitpointsModification, player.getSkills().getMaxHitpoints() + hitpointsModification);
                        } else if (skill == Skills.DEFENCE) {
                            int defenceModification = (int) (player.getSkills().getLevelForExperience(Skills.DEFENCE) * 0.20);
                            player.getSkills().increaseLevelToMaximumModification(skill, defenceModification);
                        } else {
                            int modification = (int) (player.getSkills().getLevelForExperience(skill) * 0.10);
                            player.getSkills().decreaseLevelOnce(skill, modification);
                        }
                    }
                    break;
                case ZAMORAK_BREW:
                    ActionSender.sendChatMessage(player, 0, "You drink some of the foul liquid.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        if (skill == Skills.ATTACK) {
                            int attackModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.ATTACK)) * 0.20);
                            player.getSkills().increaseLevelToMaximumModification(skill, attackModification);
                        } else if (skill == Skills.STRENGTH) {
                            int strengthModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.STRENGTH) * 0.12));
                            player.getSkills().increaseLevelToMaximumModification(skill, strengthModification);
                        } else if (skill == Skills.PRAYER) {
                            int prayerModification = (int) Math.floor(player.getSkills().getLevelForExperience(Skills.STRENGTH) * 0.10);
                            player.getSkills().restorePray(prayerModification);
                        } else if (skill == Skills.DEFENCE) {
                            int defenceModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.DEFENCE) * 0.10));
                            player.getSkills().decreaseLevelToZero(skill, defenceModification);
                        } else if (skill == Skills.HITPOINTS) {
                            World.getWorld().submit(new Tick(3) {
                                @Override
                                public void execute() {
                                    int hitpointsModification = (int) Math.floor(2 + (player.getSkills().getLevel(Skills.HITPOINTS) * 0.10));
                                    if (player.getSkills().getLevel(Skills.HITPOINTS) - hitpointsModification < 0) {
                                        hitpointsModification = player.getSkills().getLevel(Skills.HITPOINTS);
                                    }
                                    player.getDamageManager().miscDamage(hitpointsModification, DamageType.RED_DAMAGE);
                                    this.stop();
                                }
                            });
                        }
                    }
                    break;
                case ANTIPOISON:
                case SUPER_ANTIPOISON:
                    ActionSender.sendChatMessage(player, 0, "You drink some of your " + item.getDefinition().getName().toLowerCase().substring(0, item.getDefinition().getName().length() - 3) + ".");
                    if (player.getPoisonManager().canBePoisoned()) {
                        player.getPoisonManager().removePoison();
                        player.getPoisonManager().setCanBePoisoned(false);
                        World.getWorld().submit(new Tick(drink.getPotionType() == PotionType.ANTIPOISON ? 150 : 1000) {
                            public void execute() {
                                player.getPoisonManager().setCanBePoisoned(true);
                                this.stop();
                            }
                        });
                    } //steves cool trololol
                    break;
                case BEER:
                    ActionSender.sendChatMessage(player, 0, "You drink the beer. You feel slightly reinvigorated...");
                    ActionSender.sendChatMessage(player, 0, "...and slightly dizzy too.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        if (skill == Skills.ATTACK) {
                            int attackModification = (int) (player.getSkills().getLevelForExperience(Skills.STRENGTH) * 0.07);
                            player.getSkills().decreaseLevelToZero(Skills.ATTACK, attackModification);
                        } else if (skill == Skills.STRENGTH) {
                            int strengthModification = (int) (player.getSkills().getLevelForExperience(Skills.STRENGTH) * 0.04);
                            player.getSkills().increaseLevelToMaximumModification(Skills.STRENGTH, strengthModification);
                        }
                    }
                    break;
                case WINE:
                    ActionSender.sendChatMessage(player, 0, "You drink the wine. You feel slightly reinvigorated...");
                    ActionSender.sendChatMessage(player, 0, "...and slightly dizzy too.");
                    for (int i = 0; i < drink.getSkills().length; i++) {
                        int skill = drink.getSkill(i);
                        if (skill == Skills.ATTACK) {
                            int attackModification = 2;
                            player.getSkills().decreaseLevelToZero(Skills.ATTACK, attackModification);
                        } else if (skill == Skills.HITPOINTS) {
                            player.getSkills().increaseLevelToMaximum(Skills.HITPOINTS, 11);
                        }
                    }
                    break;
                case OVERLOAD:
                    if (!player.inWilderness() && !player.hasTick("overloadTick") && player.getHitPoints() > 500) {
                        /*if(true) {
                          player.sendMessage("Overload has been disabled for now.");
                          return true;
                      }*/
                        ActionSender.sendMessage(player, "You drink some of the foul liquid.");
                        player.setAttribute("overloads", Boolean.TRUE);
                        player.submitTick("overloadTick", new Tick(2) {
                            int count;

                            @Override
                            public void execute() {
                                if (count < 5 && !player.isDead()) {
                                    player.animate(3170);
                                    player.getDamageManager().miscDamage(100, DamageType.RED_DAMAGE);
                                    count++;
                                } else {
                                    this.stop();
                                }
                            }

                        });
                        player.submitTick("overloadRestoreTick", new Tick(2) {
                            int count;

                            @Override
                            public void execute() {
                                if (player.getAttribute("overloads", Boolean.FALSE) == Boolean.FALSE) {
                                    this.stop();
                                    return;
                                }
                                if (player.isDead()) {
                                    player.removeAttribute("overloads");
                                    this.stop();
                                }
                                if (count <= 20 && !player.isDead() && !player.inWilderness()) {
                                    for (int i = 0; i < drink.getSkills().length; i++) { //HOLY SHIT IM DUMBBB
                                        int skill = drink.getSkill(i);
                                        if (skill == Skills.ATTACK) {
                                            int attackModification = (int) Math.floor(5 + (player.getSkills().getLevelForExperience(Skills.ATTACK)) * 0.22);
                                            player.getSkills().increaseLevelToMaximumModification(skill, attackModification);
                                            continue;
                                        }
                                        if (skill == Skills.DEFENCE) {
                                            int defenceModification = (int) Math.floor(5 + (player.getSkills().getLevelForExperience(Skills.DEFENCE)) * 0.22);
                                            player.getSkills().increaseLevelToMaximumModification(skill, defenceModification);
                                            continue;
                                        }
                                        if (skill == Skills.STRENGTH) {
                                            int strengthModification = (int) Math.floor(5 + (player.getSkills().getLevelForExperience(Skills.STRENGTH)) * 0.22);
                                            player.getSkills().increaseLevelToMaximumModification(skill, strengthModification);
                                            continue;
                                        }
                                        if (skill == Skills.RANGE) {
                                            int rangeModification = (int) 4 + Misc.random(19);
                                            player.getSkills().increaseLevelToMaximumModification(skill, rangeModification);
                                            continue;
                                        }
                                        if (skill == Skills.MAGIC) {
                                            int mageModification = (int) 7;
                                            player.getSkills().increaseLevelToMaximumModification(skill, mageModification);
                                            continue;
                                        }
                                    }
                                } else if (!player.isDead()) {
                                    for (int i = 0; i < drink.getSkills().length; i++) { //HOLY SHIT IM DUMBBB
                                        int skill = drink.getSkill(i);
                                        player.getSkills().set(skill, player.getSkills().getLevelForExperience(skill) + 0);
                                    }
                                    player.getSkills().heal(500);
                                    player.removeAttribute("overloads");
                                    this.stop();
                                }
                            }

                        });
                        /*
                           * 					int skill = drink.getSkill(i);
                          int modification = (int) Math.floor(5 + (player.getSkills().getLevelForExperience(skill) * 0.15));
                          player.getSkills().increaseLevelToMaximumModification(skill, modification);
                           */
                        for (int i = 0; i < drink.getSkills().length; i++) { //HOLY SHIT IM DUMBBB
                            int skill = drink.getSkill(i);
                            if (skill == Skills.ATTACK) {
                                int attackModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.ATTACK)) * 0.22);
                                player.getSkills().increaseLevelToMaximumModification(skill, attackModification);
                            }
                            if (skill == Skills.DEFENCE) {
                                int defenceModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.DEFENCE)) * 0.22);
                                player.getSkills().increaseLevelToMaximumModification(skill, defenceModification);
                            }
                            if (skill == Skills.STRENGTH) {
                                int strengthModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.STRENGTH)) * 0.22);
                                player.getSkills().increaseLevelToMaximumModification(skill, strengthModification);
                            }
                            if (skill == Skills.RANGE) {
                                int rangeModification = (int) 4 + Misc.random(19);
                                player.getSkills().increaseLevelToMaximumModification(skill, rangeModification);
                            }
                            if (skill == Skills.MAGIC) {
                                int mageModification = (int) 7;
                                player.getSkills().increaseLevelToMaximumModification(skill, mageModification);
                            }
                        }
                    } else {
                        return false;
                    }
                    break;
                case EXTREME:
                    if (!player.inWilderness()) {
                        for (int i = 0; i < drink.getSkills().length; i++) { //HOLY SHIT IM DUMBBB
                            int skill = drink.getSkill(i);
                            if (skill == Skills.ATTACK) {
                                int attackModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.ATTACK)) * 0.22);
                                player.getSkills().increaseLevelToMaximumModification(skill, attackModification);
                            }
                            if (skill == Skills.DEFENCE) {
                                int defenceModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.DEFENCE)) * 0.22);
                                player.getSkills().increaseLevelToMaximumModification(skill, defenceModification);
                            }
                            if (skill == Skills.STRENGTH) {
                                int strengthModification = (int) Math.floor(2 + (player.getSkills().getLevelForExperience(Skills.STRENGTH)) * 0.22);
                                player.getSkills().increaseLevelToMaximumModification(skill, strengthModification);
                            }
                            if (skill == Skills.RANGE) {
                                int rangeModification = (int) 4 + Misc.random(19);
                                player.getSkills().increaseLevelToMaximumModification(skill, rangeModification);
                            }
                            if (skill == Skills.MAGIC) {
                                int mageModification = (int) 7;
                                player.getSkills().increaseLevelToMaximumModification(skill, mageModification);
                            }
                        }
                    }
                    break;
                case ANTIFIRE_POTION:
                    if (drink == Drink.ANTIFIRE) {
                        player.removeAttribute("santiFire");
                        player.setAttribute("antiFire", System.currentTimeMillis());
                    } else {
                        player.removeAttribute("antiFire");
                        player.setAttribute("santiFire", System.currentTimeMillis());
                    }
                    break;
                case RESTORE_SPECIAL:
                    if (!player.inWilderness() && player.getAttribute("specRestore", -1) < World.getTicks()) {
                        player.setSpecialAmount(player.getSpecialAmount() + 250);
                        if (player.getSpecialAmount() > 1000) {
                            player.setSpecialAmount(1000);
                        }
                        player.setAttribute("specRestore", World.getTicks() + 50);
                    } else {
                        return false;
                    }
                    break;

            }
            player.animate(DRINKING_ANIMATION);
            player.getInventory().set(slot, null);

            int currentPotionDose = 0;
            for (int i = 0; i < drink.getIds().length; i++) {
                if (item.getId() == drink.getId(i)) {
                    currentPotionDose = i + 1;
                    break;
                }
            }
            if (drink.getPotionType() != PotionType.BEER && drink.getPotionType() != PotionType.WINE) {
                ActionSender.sendChatMessage(player, 0, currentPotionDose > 1 ? ("You have " + (currentPotionDose - 1) + " dose" + (currentPotionDose > 2 ? "s" : "") + " of potion left.") : "You have finished your potion.");
            }
            int newPotion = 229;
            if (currentPotionDose > 1) {
                newPotion = drink.getId(currentPotionDose - 2);
            }
            player.getInventory().set(slot, new Item(newPotion));
            player.getInventory().refresh();
            return true;
        }
        return false;
    }

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(149, this);
    }

}
