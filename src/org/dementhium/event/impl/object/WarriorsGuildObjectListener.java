package org.dementhium.event.impl.object;

import org.dementhium.content.DialogueManager;
import org.dementhium.content.activity.ActivityManager;
import org.dementhium.content.activity.impl.warriorsguild.DummyHit;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.impl.WGuildTick;
import org.dementhium.util.Constants;
import org.dementhium.util.InterfaceSettings;

/**
 * @author Steve <golden_32@live.com>
 */
public class WarriorsGuildObjectListener extends EventListener {

    public static final int[] OBJECT_IDS = new int[]{15656, 15650, 15641, 15644, 15653, 15657, 15647};

    @Override
    public void register(EventManager manager) {
        for (int i : OBJECT_IDS)
            manager.registerObjectListener(i, this);
        for (int y = 0; y < WGuildTick.NEW_IDS.length - 1; y++)
            manager.registerObjectListener(WGuildTick.NEW_IDS[y], this);
    }

    @Override
    public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
        if (player.getRights() != 2) {
            for (int i : OBJECT_IDS) {
                if (objectId == i) {
                    player.sendMessage("Not completed yet");
                    return true;
                }
            }
        }
        if (DummyHit.isDummy(objectId)) {
            player.setActivity(new DummyHit(player, gameObject));
            ActivityManager.getSingleton().register(player.getActivity());
            return true;
        }
        switch (objectId) {
            case 15641: //15646
                ObjectManager.removeObjectTemporarily(gameObject.getLocation(), 2, gameObject.getType(), gameObject.getRotation());
                player.requestWalk(2854, player.getLocation().getY() == 3545 ? 3546 : 3545);
                ObjectManager.addObjectTemporarily(2854, 3545, 0, 4, gameObject.getType(), 15646, 2);
                return true;
            case 15644: // 15643
                ObjectManager.removeObjectTemporarily(gameObject.getLocation(), 2, gameObject.getType(), gameObject.getRotation());
                player.requestWalk(2855, player.getLocation().getY() == 3545 ? 3546 : 3545);
                ObjectManager.addObjectTemporarily(2855, 3545, 0, 2, gameObject.getType(), 15643, 2);
                return true;
            case 15653:
                if (player.getSkills().getLevelForExperience(Skills.STRENGTH) == 99 || player.getSkills().getLevelForExperience(Skills.ATTACK) == 99 || (player.getSkills().getLevelForExperience(Skills.STRENGTH) + player.getSkills().getLevelForExperience(Skills.ATTACK) >= 130)) {
                    ObjectManager.removeObjectTemporarily(gameObject.getLocation(), 2, gameObject.getType(), gameObject.getRotation());
                    ObjectManager.addObjectTemporarily(2876, 3546, 0, 3, gameObject.getType(), 15655, 2);
                    player.requestWalk(player.getLocation().getX() == 2877 ? 2876 : 2877, 3546);
                } else {
                    DialogueManager.sendDialogue(player, DialogueManager.MEAN_FACE, 8266, -1, "You're not strong enough to enter this guild.");
                }
                return true;
            case 15647:
                ObjectManager.removeObjectTemporarily(gameObject.getLocation(), 2, gameObject.getType(), gameObject.getRotation());
                if (player.getLocation().getY() == 3542)
                    player.getEquipment().unEquip(player, 8856, 387, Equipment.SLOT_SHIELD);
                else if (player.getEquipment().getSlot(Equipment.SLOT_SHIELD) == 8856 && !player.getAttribute("disabledTabs", false)) {
                    for (int i : Constants.W_GUILD_CATAPULT_TABS)
                        InterfaceSettings.disableTab(player, i);
                    ActionSender.sendInterface(player, 1, player.getConnection().getDisplayMode() >= 2 ? 746 : 548, player.getConnection().getDisplayMode() >= 2 ? 92 : 207, 411);
                    ActionSender.sendBConfig(player, 168, 5);
                    player.setAttribute("disabledTabs", true);
                }
                player.requestWalk(2842, player.getLocation().getY() == 3541 ? 3542 : 3541);
                ObjectManager.addObjectTemporarily(2842, 3541, 2, 2, gameObject.getType(), 15643, 2);
                return true;
            case 15656:
                ActionSender.sendInterface(player, 412);
                return true;
            case 15657:
                ActionSender.sendInterface(player, 410);
                return true;
        }
        return false;

    }

}
