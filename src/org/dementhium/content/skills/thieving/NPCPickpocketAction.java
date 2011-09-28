package org.dementhium.content.skills.thieving;

import org.dementhium.content.skills.SkillAction;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * Handles the pickpocketing an npc thieving action .
 *
 * @author Emperor
 */
public class NPCPickpocketAction extends SkillAction {

    /**
     * The animation to execute.
     */
    private static final Animation STUN_ANIMATION = Animation.create(422);

    /**
     * The animation to execute.
     */
    private static final Animation PICKPOCKETING_ANIMATION = Animation.create(881);

    /**
     * The double loot animation.
     */
    private static final Animation DOUBLE_LOOT_ANIMATION = Animation.create(5074);

    /**
     * The double loot gfx.
     */
    private static final Graphic DOUBLE_LOOT_GFX = Graphic.create(873);

    /**
     * The triple loot animation.
     */
    private static final Animation TRIPLE_LOOT_ANIMATION = Animation.create(5075);

    /**
     * The triple loot gfx.
     */
    private static final Graphic TRIPLE_LOOT_GFX = Graphic.create(874);

    /**
     * The quadruple loot animation.
     */
    private static final Animation QUADRUPLE_LOOT_ANIMATION = Animation.create(5078);

    /**
     * The quadruple loot gfx.
     */
    private static final Graphic QUADRUPLE_LOOT_GFX = Graphic.create(875);

    /**
     * The NPC to pickpocket from.
     */
    private final NPC npc;

    /**
     * The data for the npc to be pickpocketed.
     */
    private final PickpocketableNPC npcData;

    /**
     * If this pickpocket is succesful.
     */
    private boolean succesful = true;

    /**
     * The message to send.
     */
    private String message;

    /**
     * The slot to use in the levels required arrays.
     */
    private byte slot = 0;

    /**
     * The amount of ticks left.
     */
    private byte ticks = 2;

    /**
     * Constructs a new {@code NPCPickpocketAction} {@code Object}.
     *
     * @param player  The player.
     * @param npc     The npc.
     * @param npcData The pickpocketing data for the npc.
     */
    public NPCPickpocketAction(Player player, NPC npc, PickpocketableNPC npcData) {
        super(player);
        this.npc = npc;
        this.npcData = npcData;
    }

    @Override
    public boolean commence(Player player) {
        if (player.getSkills().getLevel(Skills.THIEVING) < npcData.getThievingLevels()[0]) {
            ActionSender.sendMessage(player, "You need a Thieving level of " + npcData.getThievingLevels()[0] + " to steal this npc's pocket.");
            return false;
        }
        if (player.getAttribute("stunned", false)) {
            return false;
        }
        if (player.getInventory().getFreeSlots() < 1) {
            player.sendMessage("Not enough space in your inventory.");
            return false;
        }
        player.turnTo(npc);
        int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
        int agilityLevel = player.getSkills().getLevel(Skills.AGILITY);
        if (RANDOM.nextInt((thievingLevel + agilityLevel) / 2) > RANDOM.nextInt(npcData.getThievingLevels()[0] + 65)) {
            slot = (byte) RANDOM.nextInt(npcData.getThievingLevels().length);
        }
        while (npcData.getThievingLevels()[slot] > thievingLevel || npcData.getAgilityLevels()[slot] > agilityLevel) {
            slot--;
        }
        player.sendMessage("You attempt to pick the " + npc.getDefinition().getName().toLowerCase() + "'s pocket...");
        message = getMessage(player);
        int increasedChance = getIncreasedChance(player);
        int level = RANDOM.nextInt(thievingLevel + increasedChance) + 1;
        double ratio = level / (RANDOM.nextInt(npcData.getThievingLevels()[0] + 5) + 1);
        if (Math.round(ratio * thievingLevel) < npcData.getThievingLevels()[0]) {
            succesful = false;
            message = "You fail to pick the " + npc.getDefinition().getName().toLowerCase() + "'s pocket.";
        }
        player.setAttribute("cantMove", true);
        player.setAttribute("isStealing", true);
        return true;
    }

    /**
     * Gets the increased chance for succesfully pickpocketing.
     *
     * @param player The player.
     * @return The amount of increased chance.
     */
    private int getIncreasedChance(Player player) {
        int chance = 0;
        if (player.getEquipment().getSlot(Equipment.SLOT_HANDS) == 10075) {
            /*
                * Gloves of silence.
                */
            chance += 12;
        }
        if (player.getEquipment().getSlot(Equipment.SLOT_CAPE) == 15347 && player.getPlayerArea().isInArdougne()) {
            chance += 15;
        } else if (player.getEquipment().getSlot(Equipment.SLOT_CAPE) == 15349) {
            chance += 15;
        }
        if (npc.getDefinition().getName().contains("H.A.M")) {
            for (Item item : player.getEquipment().getContainer().toArray()) {
                if (item != null && item.getDefinition().getName().contains("H.A.M")) {
                    chance += 3;
                }
            }
        }
        return chance;
    }

    @Override
    public boolean execute(Player player) {
        return --ticks == 0;
    }

    @Override
    public boolean finish(final Player player) {
        player.sendMessage(message);
        player.setAttribute("cantMove", false);
        player.setAttribute("isStealing", false);
        if (!succesful) {
            npc.turnTo(player);
            npc.animate(STUN_ANIMATION);
            player.animate(Animation.create(player.getDefenceAnimation()));
            World.getWorld().submit(new Tick(1) {
                @Override
                public void execute() {
                    player.getDamageManager().damage(npc, npcData.getStunDamage(), -1, DamageType.RED_DAMAGE);
                    player.stun(npcData.getStunTime(), "You have been stunned!", true);
                    this.stop();
                }
            });
            if (npcData.equals(PickpocketableNPC.MASTER_FARMER) || npcData.equals(PickpocketableNPC.FARMER)) {
                npc.forceText("Cor blimey mate, what are ye doing in me pockets?");
                return true;
            }
            npc.forceText("What do you think you're doing?");
            return true;
        }
        player.getSkills().addExperience(Skills.THIEVING, npcData.getExperience());
        for (int i = -1; i < slot; i++) {
            Item item = npcData.getLoot()[RANDOM.nextInt(npcData.getLoot().length)];
            player.getInventory().addItem(item.getId(), item.getAmount());
        }
        return true;
    }

    /**
     * Gets the message to send when finishing.
     *
     * @param player The player.
     * @return The message.
     */
    private String getMessage(Player player) {
        if (slot == 0) {
            player.animate(PICKPOCKETING_ANIMATION);
            return "You succesfully pick the " + npc.getDefinition().getName().toLowerCase() + "'s pocket.";
        } else if (slot == 1) {
            player.graphics(DOUBLE_LOOT_GFX);
            player.animate(DOUBLE_LOOT_ANIMATION);
            return "Your lighting-fast reactions allow you to steal double loot.";
        } else if (slot == 2) {
            player.graphics(TRIPLE_LOOT_GFX);
            player.animate(TRIPLE_LOOT_ANIMATION);
            return "Your lighting-fast reactions allow you to steal triple loot.";
        }
        player.graphics(QUADRUPLE_LOOT_GFX);
        player.animate(QUADRUPLE_LOOT_ANIMATION);
        return "Your lighting-fast reactions allow you to steal quadruple loot.";
    }
}
