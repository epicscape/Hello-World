package org.dementhium.content.activity.impl.warriorsguild;

import org.dementhium.content.activity.impl.WarriorsGuildMinigame;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;

import java.util.HashMap;

/**
 * @author Steve <golden_32@live.com>
 */
public class DummyHit extends WarriorsGuildMinigame {

    private static HashMap<Integer, Dummy> dummies = new HashMap<Integer, Dummy>();

    public enum Dummy {
        ACCURATE(15624, WeaponInterface.STYLE_ACCURATE, 422),
        STAB(15629, WeaponInterface.TYPE_STAB, 422),
        CRUSH(15628, WeaponInterface.TYPE_CRUSH, 422),
        DEFENCE(15630, WeaponInterface.STYLE_DEFENSIVE, 422),
        SLASH(15625, WeaponInterface.TYPE_SLASH, 422),
        CONTROLLED(15627, WeaponInterface.STYLE_CONTROLLED, 422),
        AGGRESSIVE(15626, WeaponInterface.STYLE_AGGRESSIVE, 423);

        private int style;
        private int anim;
        private int objectId;

        public int getStyle() {
            return style;
        }

        public int getAnim() {
            return anim;
        }

        public int getObjectId() {
            return objectId;
        }

        Dummy(int objectId, int style, int anim) {
            this.objectId = objectId;
            this.style = style;
            this.anim = anim;
        }

    }

    static {
        for (Dummy d : Dummy.values()) {
            dummies.put(d.getObjectId(), d);
        }
    }

    private Dummy dummy;
    private GameObject object;

    public DummyHit(Player p, GameObject object) {
        super(p);
        this.dummy = dummies.get(object.getId());
        this.object = object;
        setCanExecute(true);
    }

    @Override
    public boolean commenceSession() {
        if (object.isHealthSet()) {
            if (getPlayer().getSettings().getCombatStyle() == dummy.getStyle()) {
                player.getSettings().getTokens()[4] += 5;
                player.getSkills().addExperience(Skills.ATTACK, 15);
                player.sendMessage("You whack the dummy with the correct attack style!");
                object.setHealthSet(false); //disable multiclicking on a dead dummy
            } else {
                player.stun(1, "You whack the dummy with the wrong attack style!", true);
            }
            getPlayer().animate(dummy.getAnim());
        }
        return true;
    }

    @Override
    public boolean endSession() {
        return true;
    }

    public static boolean isDummy(int objectId) {
        return dummies.containsKey(objectId);
    }

}
